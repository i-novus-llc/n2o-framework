import { takeEvery, select, put, call, fork } from 'redux-saga/effects'
import get from 'lodash/get'
import { LOCATION_CHANGE } from 'connected-react-router'

import { makePageRoutesByIdSelector } from '../pages/selectors'
import { makeWidgetsByPageIdSelector } from '../widgets/selectors'
import { dataRequest } from '../datasource/store'
import { mapQueryToUrl } from '../pages/sagas/restoreFilters'
import { makeFormByName } from '../form/selectors'

import { CLOSE } from './constants'
import {
    showPrompt,
    destroyOverlay,
    destroyOverlays,
    insertOverlay,
    insertDrawer,
} from './store'

/**
 * Проверка на изменение данных в формах
 * @param name
 * @returns {IterableIterator<*>}
 */
export function* checkOnDirtyForm(name) {
    let someOneDirtyForm = false

    const widgets = yield select(makeWidgetsByPageIdSelector(name))

    for (const widgetName of Object.keys(widgets)) {
        const form = yield select(makeFormByName(widgetName))

        someOneDirtyForm = someOneDirtyForm || form.dirty
    }

    return someOneDirtyForm
}

/**
 * Функция показа подтверждения закрытия
 * @param action
 * @returns {IterableIterator<*>}
 */
export function* checkPrompt(action) {
    const { name, prompt } = action.payload
    let needToShowPrompt = false

    if (prompt) {
        needToShowPrompt = yield call(checkOnDirtyForm, name)
    }
    if (!needToShowPrompt) {
        yield put(destroyOverlay())
        yield call(resetQuerySaga, name)
    } else {
        yield put(showPrompt(name))
    }
}

export function* closeOverlays({ meta }) {
    if (meta.modalsToClose) {
        yield put(destroyOverlays(meta.modalsToClose))
    }
}

function* onCloseEffects() {
    const onCloseHandlers = {}

    function* getClose({ meta, payload }) {
        const { name } = payload

        if (get(meta, 'onClose')) {
            yield onCloseHandlers[name] = meta.onClose
        }
    }

    function* onClose({ payload }) {
        const { name } = payload

        if (onCloseHandlers[name]) {
            const { refresh } = onCloseHandlers[name]

            if (refresh) {
                const { datasources } = refresh

                for (const datasource of datasources) {
                    yield put(dataRequest(datasource))
                }
            }

            delete onCloseHandlers[name]
        }
    }

    yield takeEvery([insertOverlay, insertDrawer], getClose)
    yield takeEvery(CLOSE, onClose)
}

export function* resetQuerySaga(pageId) {
    const routes = yield select(makePageRoutesByIdSelector(pageId))

    if (routes) {
        const resetQuery = {}

        for (const key of Object.keys(routes.queryMapping)) {
            resetQuery[key] = undefined
        }

        yield mapQueryToUrl(pageId, resetQuery, true)
    }
}

export const overlaysSagas = [
    takeEvery(CLOSE, checkPrompt),
    takeEvery(
        action => (
            action.meta &&
            action.payload &&
            !action.payload.prompt &&
            action.meta.modalsToClose &&
            action.type !== CLOSE
        ) || (
            action.type === LOCATION_CHANGE &&
            action.payload.action !== 'REPLACE'
        ),
        closeOverlays,
    ),
    fork(onCloseEffects),
]
