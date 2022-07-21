import {
    put,
    select,
    takeEvery,
} from 'redux-saga/effects'
import { push } from 'connected-react-router'
import isArray from 'lodash/isArray'
import { reset } from 'redux-form'
import get from 'lodash/get'

import { insertDialog, destroyOverlays } from '../ducks/overlays/store'
import { id } from '../utils/id'
import { CALL_ALERT_META } from '../constants/meta'
import { dataProviderResolver } from '../core/dataProviderResolver'
import { addMultiAlerts, removeAllAlerts } from '../ducks/alerts/store'
import { GLOBAL_KEY, STORE_KEY_PATH } from '../ducks/alerts/constants'
import { removeAllModel } from '../ducks/models/store'

/* TODO избавиться от alertEffect
    для этого бэку нужно присылать
    структуру как в alert action
    { payload: { key: 'key', alerts: [...] }, type: 'type'}
    alertEffect - обрабатывает alert.meta server responses
    маппит id и выполняет redux action */
export function* alertEffect(action) {
    try {
        const { messages, stacked } = action.meta.alert

        const mapMessage = message => ({
            ...message,
            id: messages.id || id(),
        })
        const alerts = isArray(messages)
            ? messages.map(mapMessage)
            : [mapMessage(messages)]

        const alertsKey = get(alerts[0], STORE_KEY_PATH) || GLOBAL_KEY

        /* !stacked 1 alert в каждом поддерживаемом placement  */
        if (!stacked) { yield put(removeAllAlerts(alertsKey)) }

        yield put(addMultiAlerts(alertsKey, alerts))
    } catch (e) {
        // eslint-disable-next-line no-console
        console.error(e)
    }
}

export function* clearOnSuccessEffect(action) {
    const { meta } = action
    const datasourceId = get(meta, 'clear', null)

    if (!datasourceId) { return }

    yield put(removeAllModel(datasourceId))
}

export function* redirectEffect(action) {
    try {
        const { path, pathMapping, queryMapping, target } = action.meta.redirect

        const state = yield select()

        const { url: newUrl } = dataProviderResolver(state, {
            url: path,
            pathMapping,
            queryMapping,
        })

        if (target === 'application') {
            yield clearOnSuccessEffect(action)
            yield put(push(newUrl))
            yield put(destroyOverlays())
        } else if (target === 'self') {
            window.location = newUrl
        } else {
            window.open(newUrl)
        }
    } catch (e) {
        // eslint-disable-next-line no-console
        console.error(e)
    }
}

export function* clearFormEffect(action) {
    yield put(reset(action.meta.clearForm))
}

export function* userDialogEffect({ meta }) {
    const { title, description, toolbar, ...rest } = meta.dialog

    yield put(
        insertDialog('dialog', true, {
            title,
            description,
            toolbar,
            ...rest,
        }),
    )
}

export function* clearEffect(action) {
    const { meta } = action

    const redirect = get(meta, 'redirect', null)

    if (redirect) {
        return
    }

    yield clearOnSuccessEffect(action)
}

export const metaSagas = [
    takeEvery(
        [action => action.meta && action.meta.alert, CALL_ALERT_META],
        alertEffect,
    ),
    takeEvery(action => action.meta && action.meta.redirect, redirectEffect),
    takeEvery(action => action.meta && action.meta.clearForm, clearFormEffect),
    takeEvery(action => action.meta && action.meta.dialog, userDialogEffect),
    takeEvery(action => action.meta?.clear, clearEffect),
]
