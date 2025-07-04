import { takeEvery, select, put, call, fork, cancel } from 'redux-saga/effects'
import get from 'lodash/get'
import { LOCATION_CHANGE } from 'connected-react-router'

import { makePageRoutesByIdSelector } from '../pages/selectors'
import { makeWidgetsByPageIdSelector } from '../widgets/selectors'
import { dataRequest } from '../datasource/store'
import { mapQueryToUrl } from '../pages/sagas/restoreFilters'
import { makeFormByName } from '../form/selectors'
import { Routes } from '../pages/sagas/types'
import { State as WidgetsState } from '../widgets/Widgets'
import { Form } from '../form/types'
import { EffectWrapper } from '../api/utils/effectWrapper'
import { stopTheSequence } from '../api/utils/stopTheSequence'
import { resetPage } from '../pages/store'
import { type Reset } from '../pages/Actions'

import { State as OverlaysState } from './Overlays'
import { CLOSE } from './constants'
import {
    showPrompt,
    destroyOverlays,
    insertOverlay,
    insertDrawer,
    remove,
} from './store'
import { overlaysSelector } from './selectors'

interface Refresh {
    datasources: string[]
}

interface Meta {
    onClose: {
        refresh: Refresh
    }
    modalsToClose: number
}

/**
 * Проверка на изменение данных в формах
 */
export function* checkOnDirtyForm(name: string) {
    let someOneDirtyForm = false

    const widgets: WidgetsState = yield select(makeWidgetsByPageIdSelector(name))

    for (const widgetName of Object.keys(widgets)) {
        const form: Form = yield select(makeFormByName(widgetName))

        someOneDirtyForm = someOneDirtyForm || form.dirty
    }

    return someOneDirtyForm
}

/**
 * Функция показа подтверждения закрытия
 */
export function* checkPrompt(action: { payload: { name: string, prompt: boolean } }) {
    const { name, prompt } = action.payload
    let needToShowPrompt = false

    if (prompt) {
        needToShowPrompt = yield call(checkOnDirtyForm, name)
    }

    if (!needToShowPrompt) {
        yield put(remove())
        yield call(resetQuerySaga, name)
    } else {
        yield put(showPrompt(name))
    }
}

export function* closeOverlays({ meta }: { meta: Meta }) {
    if (meta.modalsToClose) {
        yield put(destroyOverlays(meta.modalsToClose))
    }
}

function* onCloseEffects() {
    const onCloseHandlers: Record<string, { refresh: Refresh, [key: string]: unknown }> = {}

    function* getClose({ meta, payload }: { meta: Meta, payload: { name: string } }) {
        const { name } = payload

        if (get(meta, 'onClose')) {
            yield onCloseHandlers[name] = meta.onClose
        }
    }

    function* onClose({ payload }: { payload: { name: string, prompt: boolean } }) {
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

    // @ts-ignore проблемы с типизацией saga
    yield takeEvery([insertOverlay, insertDrawer], getClose)
    // @ts-ignore проблемы с типизацией saga
    yield takeEvery(CLOSE, onClose)
}

export function* resetQuerySaga(pageId: string) {
    const routes: Routes = yield select(makePageRoutesByIdSelector(pageId))

    if (routes) {
        const resetQuery: Record<string, undefined> = {}

        for (const key of Object.keys(routes.queryMapping)) {
            resetQuery[key] = undefined
        }

        yield mapQueryToUrl(pageId, resetQuery)
    }
}

export function* closePageOverlays({ payload: pageId }: Reset) {
    const overlays: OverlaysState = yield select(overlaysSelector)

    if (!overlays.length) {
        yield cancel()
    }

    for (const { parentPage, id } of overlays) {
        if (parentPage === pageId) {
            yield put(remove(id))
        }
    }
}

export const overlaysSagas = [
    // @ts-ignore проблемы с типизацией saga
    takeEvery(CLOSE, checkPrompt),
    takeEvery([insertOverlay, insertDrawer], EffectWrapper(stopTheSequence)),
    takeEvery(
        // @ts-ignore проблемы с типизацией saga
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
    takeEvery(resetPage, closePageOverlays),
    fork(onCloseEffects),
]
