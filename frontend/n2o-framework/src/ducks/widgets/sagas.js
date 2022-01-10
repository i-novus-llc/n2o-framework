import {
    put,
    select,
    takeEvery,
} from 'redux-saga/effects'
import last from 'lodash/last'

import { PREFIXES } from '../models/constants'
import { removeModel, setModel } from '../models/store'
import { dataRequest } from '../datasource/store'

import {
    makeDatasourceIdSelector,
    makeWidgetByIdSelector,
    widgetsSelector,
} from './selectors'
import {
    dataRequestWidget,
    disableWidget,
    resolveWidget,
} from './store'

export function* runResolve(action) {
    const { modelId, model } = action.payload

    try {
        yield put(setModel(PREFIXES.resolve, modelId, model))
        // eslint-disable-next-line no-empty
    } catch (err) {}
}

export function* clearOnDisable(action) {
    const { modelId } = action.payload

    yield put(setModel(PREFIXES.datasource, modelId, null))
}

const pagesHash = []

// eslint-disable-next-line
function* clearFilters(action) {
    const { widgetId } = action.payload

    const { pageId } = yield select(makeWidgetByIdSelector(widgetId))

    if (last(pagesHash) === pageId) {
        return
    }

    if (pagesHash.includes(pageId)) {
        const currentPageIndex = pagesHash.indexOf(pageId)
        const filterResetIds = pagesHash.splice(currentPageIndex + 1)

        const widgets = Object.values(yield select(widgetsSelector))
            .filter(widget => filterResetIds.includes(widget.pageId))

        // eslint-disable-next-line no-restricted-syntax
        for (const { widgetId } of widgets) {
            yield put(removeModel(PREFIXES.filter, widgetId))
        }
    } else {
        pagesHash.push(pageId)
    }
}

/**
 * Сайд-эффекты для виджет редюсера
 * @ignore
 */
export default () => [
    takeEvery(dataRequestWidget, function* redirectRequest({ payload }) {
        const { widgetId } = payload
        const sourceId = yield select(makeDatasourceIdSelector(widgetId))

        yield put(dataRequest(sourceId))
    }),
    takeEvery(resolveWidget, runResolve),
    takeEvery(disableWidget, clearOnDisable),
]
