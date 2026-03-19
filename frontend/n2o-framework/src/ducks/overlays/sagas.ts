import { takeEvery, select, put, fork, cancel } from 'redux-saga/effects'
import get from 'lodash/get'
import { LOCATION_CHANGE } from 'connected-react-router'

import { dataRequest } from '../datasource/store'
import { EffectWrapper } from '../api/utils/effectWrapper'
import { stopTheSequence } from '../api/utils/stopTheSequence'
import { resetPage } from '../pages/store'
import { type Reset } from '../pages/Actions'
import { type N2OMeta, type Action } from '../Action'
import { closePageCreator, type ClosePagePayload } from '../api/page'

import { State as OverlaysState } from './Overlays'
import {
    destroyOverlays,
    destroyAllOverlays,
    insertOverlay,
    insertDrawer,
    remove,
} from './store'
import { overlaysSelector } from './selectors'

export function* closeOverlays({ meta, type }: { meta: N2OMeta, type: Action['type'] }) {
    if (meta.modalsToClose) {
        yield put(destroyOverlays(meta.modalsToClose))

        return
    }

    if (type === LOCATION_CHANGE) {
        const { overlays = [] } = meta?.prevState || {}

        if (overlays.length > 0) {
            yield put(destroyAllOverlays())
        }
    }
}

type RefreshHandler = { refresh: { datasources: string[] } }

function* onCloseEffects() {
    const onCloseHandlers: Record<string, RefreshHandler> = {}

    function* getClose({ meta, payload }: { meta: N2OMeta, payload: { name: string } }) {
        const { name } = payload

        if (get(meta, 'onClose')) {
            yield onCloseHandlers[name] = meta.onClose as RefreshHandler
        }
    }

    function* onClose(action: Action<string, ClosePagePayload>) {
        const { pageId } = action.payload

        if (onCloseHandlers[pageId]) {
            const { refresh } = onCloseHandlers[pageId]

            if (refresh) {
                const { datasources } = refresh

                for (const datasource of datasources) {
                    yield put(dataRequest(datasource))
                }
            }

            delete onCloseHandlers[pageId]
        }
    }

    // @ts-ignore проблемы с типизацией saga
    yield takeEvery([insertOverlay, insertDrawer], getClose)

    yield takeEvery(closePageCreator.type, EffectWrapper(onClose))
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
    takeEvery([insertOverlay, insertDrawer], EffectWrapper(stopTheSequence)),
    takeEvery(
        // @ts-ignore проблемы с типизацией saga
        action => (
            action.meta &&
            action.payload &&
            !action.payload.prompt &&
            action.meta.modalsToClose &&
            action.type !== closePageCreator.type
        ) || (
            action.type === LOCATION_CHANGE &&
            action.payload.action !== 'REPLACE'
        ),
        closeOverlays,
    ),
    takeEvery(resetPage, closePageOverlays),
    fork(onCloseEffects),
]
