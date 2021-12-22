import {
    put,
    select,
    takeEvery,
} from 'redux-saga/effects'
import last from 'lodash/last'

import { PREFIXES } from '../models/constants'
import { removeModel, setModel } from '../models/store'
import { register, dataRequest } from '../datasource/store'
import { DEPENDENCY_TYPE } from '../../core/datasource/const'

import {
    makeDatasourceIdSelector,
    makeWidgetByIdSelector,
    widgetsSelector,
} from './selectors'
import {
    dataRequestWidget,
    disableWidget,
    resolveWidget,
    registerWidget,
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
    /**
     * Хак для регистрации Datasource из данных виджета
     * FIXME Удалить, после того как бек начнёт присылать данные сам (до закрытия стори)
     */
    takeEvery(registerWidget, function* RegisterWidget({ payload }) {
        const { initProps } = payload
        const { dataProvider, sorting, validation, size, datasource, dependency, form } = initProps
        let dependencies = []

        if (dependency && dependency.fetch) {
            dependencies = dependency.fetch.map(dep => ({
                ...dep,
                type: DEPENDENCY_TYPE.fetch,
            }))
        }

        yield put(register(datasource, {
            provider: dataProvider,
            sorting,
            validation: validation || form?.validation,
            size: size || dataProvider?.size,
            dependencies,
        }))
    }),
    takeEvery(dataRequestWidget, function* redirectRequest({ payload }) {
        const { widgetId } = payload
        const sourceId = yield select(makeDatasourceIdSelector(widgetId))

        yield put(dataRequest(sourceId))
    }),
    takeEvery(resolveWidget, runResolve),
    takeEvery(disableWidget, clearOnDisable),
]
