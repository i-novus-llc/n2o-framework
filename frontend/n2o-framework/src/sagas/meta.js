import {
    put,
    select,
    takeEvery,
} from 'redux-saga/effects'
import { push } from 'connected-react-router'
import isArray from 'lodash/isArray'
import { reset } from 'redux-form'

import { insertDialog, destroyOverlays } from '../ducks/overlays/store'
import { id } from '../utils/id'
import { CALL_ALERT_META } from '../constants/meta'
import { dataProviderResolver } from '../core/dataProviderResolver'
import { addMultiAlerts, removeAllAlerts } from '../ducks/alerts/store'
import { GLOBAL_KEY } from '../ducks/alerts/constants'

export function* alertEffect(action) {
    try {
        const { alertKey = GLOBAL_KEY, messages, stacked } = action.meta.alert

        if (!stacked) { yield put(removeAllAlerts(alertKey)) }
        const alerts = isArray(messages)
            ? messages.map(message => ({ ...message, id: message.id || id() }))
            : [{ ...messages, id: messages.id || id() }]

        yield put(addMultiAlerts(alertKey, alerts))
    } catch (e) {
        // eslint-disable-next-line no-console
        console.error(e)
    }
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

export const metaSagas = [
    takeEvery(
        [action => action.meta && action.meta.alert, CALL_ALERT_META],
        alertEffect,
    ),
    takeEvery(action => action.meta && action.meta.redirect, redirectEffect),
    takeEvery(action => action.meta && action.meta.clearForm, clearFormEffect),
    takeEvery(action => action.meta && action.meta.dialog, userDialogEffect),
]
