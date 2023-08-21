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
// @ts-ignore ignore import error from js file
import { dataProviderResolver } from '../core/dataProviderResolver'
import { addAlert, addMultiAlerts } from '../ducks/alerts/store'
import { CLOSE_BUTTON_PATH, DEFAULT_CLOSE_BUTTON, GLOBAL_KEY, STORE_KEY_PATH, PLACEMENT } from '../ducks/alerts/constants'
import { removeAllModel } from '../ducks/models/store'
import { register } from '../ducks/datasource/store'
import { requestConfigSuccess } from '../ducks/global/store'
import { State } from '../ducks/State'
import { Alert } from '../ducks/alerts/Alerts'

type MessagesType = Alert[]
const mapMessage = (message: Alert) => ({ ...message, id: message?.id || id() })

type AccType = Record<PLACEMENT, MessagesType> | Record<string, MessagesType>
const separateMessagesByPlacement = (
    messages: MessagesType,
): Record<PLACEMENT, MessagesType> => messages.reduce((acc: AccType, message) => {
    if (!acc[message.placement]) {
        acc[message.placement] = []
    }

    acc[message.placement].unshift(message)

    return acc
}, {})

interface AlertEffect {
    meta: {
        alert: {
            messages: MessagesType
        }
    }
}

/* TODO избавиться от alertEffect
    для этого бэку нужно присылать
    структуру как в alert action
    { payload: { key: 'key', alerts: [...] }, type: 'type'}
    alertEffect - обрабатывает alert.meta server responses
    маппит id и выполняет redux action */
export function* alertEffect(action: AlertEffect) {
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
        // @ts-ignore FIXME не знаю как поправить
        const placements: PLACEMENT[] = Object.keys(separatedAlerts)

        for (const placement of placements) {
            const alerts: MessagesType = separatedAlerts[placement]

            yield put(addMultiAlerts(placement, alerts))
        }
    } catch (e) {
        // eslint-disable-next-line no-console
        console.error(e)
    }
}

interface ClearOnSuccessEffect {
    meta: {
        clear?: string
    }
}

export function* clearOnSuccessEffect(action: ClearOnSuccessEffect) {
    const { meta } = action
    const datasourceId = get(meta, 'clear', null)

    if (!datasourceId) { return }

    yield put(removeAllModel(datasourceId))
}

interface Redirect {
    path: string
    pathMapping: Record<string, unknown>
    queryMapping: Record<string, unknown>
    target: string
}
interface RedirectEffect {
    meta: {
        clear?: string,
        modalsToClose?: number
        redirect: Redirect
    }
}

export function* redirectEffect(action: RedirectEffect) {
    try {
        const { path, pathMapping, queryMapping, target } = action.meta.redirect

        const state: State = yield select()

        const { url: newUrl } = dataProviderResolver(state, {
            url: path,
            pathMapping,
            queryMapping,
        })

        if (target === 'application') {
            yield clearOnSuccessEffect(action)
            yield put(push(newUrl))
            yield put(destroyOverlays(action.meta.modalsToClose))
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

interface UserDialogEffect {
    dialog: {
        title: string
        description: string
        toolbar: Record<string, unknown>
    }
}

export function* userDialogEffect({ meta }: { meta: UserDialogEffect }) {
    const { title, description, toolbar, ...rest } = meta.dialog

    yield put(
        insertDialog('dialog', true, 'dialog', {
            title,
            description,
            toolbar,
            ...rest,
        }),
    )
}

interface ClearEffect {
    meta: {
        clear: string
        redirect: Redirect
    }
}
export function* clearEffect(action: ClearEffect) {
    const { meta } = action

    const redirect = get(meta, 'redirect', null)

    if (redirect) {
        return
    }

    yield clearOnSuccessEffect(action)
}

interface DataSourcesRegister {
    payload: {
        menu: {
            datasources: string[]
        }
    }
}
function* dataSourcesRegister(action: DataSourcesRegister) {
    const datasources = get(action, 'payload.menu.datasources')

    if (isEmpty(datasources)) {
        yield cancel()
    }

    const entries = Object.entries(datasources)

    for (const [id, config] of entries) {
        // @ts-ignore FIXME не знаю как поправить
        yield put(register(id, config))
    }
}

export const metaSagas = [
    // @ts-ignore проблемы с типизацией saga
    takeEvery([action => action?.meta && action.meta.alert, CALL_ALERT_META], alertEffect),
    // @ts-ignore проблемы с типизацией saga
    takeEvery((action: { meta: { redirect: IRedirect } }) => action?.meta && action.meta.redirect, redirectEffect),
    // @ts-ignore проблемы с типизацией saga
    takeEvery((action: { meta: IUserDialogEffect }) => action?.meta && action.meta.dialog, userDialogEffect),
    takeEvery(requestConfigSuccess, dataSourcesRegister),
    // @ts-ignore проблемы с типизацией saga
    takeEvery((action: { meta: { clear: string } }) => action.meta?.clear, clearEffect),
]
