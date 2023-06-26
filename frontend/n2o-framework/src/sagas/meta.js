import {
    put,
    select,
    takeEvery,
    cancel,
    delay,
} from 'redux-saga/effects'
import { push } from 'connected-react-router'
import isArray from 'lodash/isArray'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'

import { insertDialog, destroyOverlays } from '../ducks/overlays/store'
import { id } from '../utils/id'
import { CALL_ALERT_META } from '../constants/meta'
import { dataProviderResolver } from '../core/dataProviderResolver'
import { addAlert, addMultiAlerts } from '../ducks/alerts/store'
import { CLOSE_BUTTON_PATH, DEFAULT_CLOSE_BUTTON, GLOBAL_KEY, STORE_KEY_PATH } from '../ducks/alerts/constants'
import { removeAllModel } from '../ducks/models/store'
import { register } from '../ducks/datasource/store'
import { requestConfigSuccess } from '../ducks/global/store'

const mapMessage = message => ({
    ...message,
    id: message?.id || id(),
})

const separateMessagesByPlacement = messages => messages.reduce((acc, message) => {
    if (!acc[message.placement]) {
        acc[message.placement] = []
    }

    acc[message.placement].unshift(message)

    return acc
}, {})

/* TODO избавиться от alertEffect
    для этого бэку нужно присылать
    структуру как в alert action
    { payload: { key: 'key', alerts: [...] }, type: 'type'}
    alertEffect - обрабатывает alert.meta server responses
    маппит id и выполняет redux action */
export function* alertEffect(action) {
    // FIXME костыльная задержка для автотестов, которые смотрят на алерт инвока как на то что последующее инвоку обновление виджета было выполнено
    yield delay(300)
    try {
        const { messages } = action.meta.alert

        const alerts = isArray(messages)
            ? messages.map(mapMessage)
            : [mapMessage(messages)]

        if (!alerts?.length) { return }

        if (alerts.length === 1) {
            const [alert] = alerts
            const placement = get(alert, STORE_KEY_PATH, GLOBAL_KEY)
            const closeButton = get(alert, CLOSE_BUTTON_PATH, DEFAULT_CLOSE_BUTTON)

            yield put(addAlert(placement, { ...alert, placement, closeButton }))

            yield cancel()
        }

        const separatedAlerts = separateMessagesByPlacement(alerts)
        const placements = Object.keys(separatedAlerts)

        for (const placement of placements) {
            yield put(addMultiAlerts(placement, separatedAlerts[`${placement}`]))
        }
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

function* dataSourcesRegister(action) {
    const datasources = get(action, 'payload.menu.datasources')

    if (isEmpty(datasources)) {
        yield cancel()
    }

    const entries = Object.entries(datasources)

    for (const [id, config] of entries) {
        yield put(register(id, config))
    }
}

export const metaSagas = [
    takeEvery(
        [
            action => action.meta && action.meta.alert,
            CALL_ALERT_META,
        ],
        alertEffect,
    ),
    takeEvery(action => action.meta && action.meta.redirect, redirectEffect),
    takeEvery(action => action.meta && action.meta.dialog, userDialogEffect),
    takeEvery(requestConfigSuccess, dataSourcesRegister),
    takeEvery(action => action.meta?.clear, clearEffect),
]
