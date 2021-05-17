import { takeEvery, select, put, call, fork } from 'redux-saga/effects'
import { isDirty } from 'redux-form'
import keys from 'lodash/keys'
import has from 'lodash/has'
import get from 'lodash/get'

import { CLOSE, INSERT_MODAL, INSERT_DRAWER } from '../constants/overlays'
import { makePageWidgetsByIdSelector } from '../selectors/pages'
import {
    showPrompt,
    destroyOverlay,
    destroyOverlays,
} from '../actions/overlays'

import { refreshEffect } from './meta'

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

    yield takeEvery([INSERT_MODAL, INSERT_DRAWER], getClose)
    yield takeEvery(CLOSE, onClose)
}

export const overlaysSagas = [
    takeEvery(CLOSE, checkPrompt),

    takeEvery(
        action => action.meta &&
      action.payload &&
      !action.payload.prompt &&
      action.meta.modalsToClose &&
      action.type !== CLOSE,
        closeOverlays,
    ),
    fork(onCloseEffects),
]
