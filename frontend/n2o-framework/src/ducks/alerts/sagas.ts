import { call, put, select, takeEvery, all, delay } from 'redux-saga/effects'
import take from 'lodash/take'

import { addAlert, addMultiAlerts, removeAlert } from './store'
import { alertsByKeySelector } from './selectors'
import { ALLOWED_ALERTS_QUANTITY } from './constants'
import { Alert, Config } from './Alerts'
import { Add, AddMulti } from './Actions'

function getStopped(alertsByKey: Alert[], targetId: string) {
    const alertProps = alertsByKey?.find(({ id }) => id === targetId)

    return alertProps ? alertProps.stopped : true
}

export function* removeAlertSideEffect(action: Add | AddMulti, alert: Alert, timeout: number) {
    yield delay(timeout)
    const alertsByKey: Alert[] = yield select(alertsByKeySelector(action.payload.key))

    const wasStopped = getStopped(alertsByKey, alert.id)

    if (alertsByKey && !wasStopped) {
        yield put(removeAlert(action.payload.key, alert.id))
    }
}

export function* addAlertSideEffect(config: Config, action: Add | AddMulti) {
    try {
        const { alerts: incomingAlerts, alert: incomingAlert } = action.payload
        /* FIXME массив генераторов removeAlertSideEffect */
        let effects: unknown[] = []

        const alerts: Alert[] = incomingAlert
            ? take([incomingAlert], ALLOWED_ALERTS_QUANTITY)
            : take(incomingAlerts, ALLOWED_ALERTS_QUANTITY)

        for (let i = 0; i < alerts.length; i++) {
            const timeout: number = yield call(getTimeout, alerts[i], config)

            if (timeout) {
                effects = [...effects, removeAlertSideEffect(action, alerts[i], timeout)]
            }
        }

        yield all(effects)
    } catch (e) {
        // eslint-disable-next-line no-console
        console.error(e)
    }
}

export function getTimeout(alert: Alert, config: Config) {
    return alert.timeout || (config.timeout?.[alert.severity])
}

export default (config: Config) => [takeEvery([addAlert, addMultiAlerts], addAlertSideEffect, config)]
