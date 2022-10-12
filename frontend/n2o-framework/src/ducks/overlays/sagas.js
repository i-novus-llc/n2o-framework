import { takeEvery, select, put, call, fork } from 'redux-saga/effects'
import { isDirty } from 'redux-form'
import keys from 'lodash/keys'
import has from 'lodash/has'
import get from 'lodash/get'
import { LOCATION_CHANGE } from 'connected-react-router'

import { mapQueryToUrl } from '../pages/sagas/restoreFilters'
import { makePageRoutesByIdSelector, makePageWidgetsByIdSelector } from '../pages/selectors'
import { refreshEffect } from '../../sagas/meta'

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
    const state = yield select()
    let widget = makePageWidgetsByIdSelector(name)(state)

    if (has(widget, 'id')) {
        widget = { [widget.id]: widget }
    }

    const widgetsKeys = keys(widget)

    for (let i = 0; i < widgetsKeys.length; i++) {
        if (widget[widgetsKeys[i]].src === 'FormWidget') {
            someOneDirtyForm = isDirty(widgetsKeys[i])(state)
        }
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
    yield put(destroyOverlays(meta.modalsToClose))
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
                yield refreshEffect({ meta: { refresh } })
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

        for (const [k] of Object.entries(routes.queryMapping)) {
            resetQuery[k] = undefined
        }

        yield mapQueryToUrl(pageId, resetQuery, true)
    }
}

export const overlaysSagas = [
    takeEvery(CLOSE, checkPrompt),

    takeEvery(
        action => (action.meta &&
      action.payload &&
      !action.payload.prompt &&
      action.meta.modalsToClose &&
      action.type !== CLOSE) ||
      (action.type === LOCATION_CHANGE && action.payload.action !== 'REPLACE'),
        closeOverlays,
    ),
    fork(onCloseEffects),
]
