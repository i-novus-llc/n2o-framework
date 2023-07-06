import { call, put, select, takeEvery, all, delay } from 'redux-saga/effects'
import take from 'lodash/take'

import { addAlert, addMultiAlerts, removeAlert } from './store'
import { alertsByKeySelector } from './selectors'
import { ALLOWED_ALERTS_QUANTITY } from './constants'
import { IAlert, IConfig } from './Alerts'
import { Add, AddMulti } from './Actions'

function getStopped(alertsByKey: IAlert[], targetId: string) {
    const alertProps = alertsByKey?.find(({ id }) => id === targetId)

    return alertProps ? alertProps.stopped : true
}

export function* removeAlertSideEffect(action: Add | AddMulti, alert: IAlert, timeout: number) {
    yield delay(timeout)
    const alertsByKey: IAlert[] = yield select(alertsByKeySelector(action.payload.key))

    const wasStopped = getStopped(alertsByKey, alert.id)

    if (alertsByKey && !wasStopped) {
        yield put(removeAlert(action.payload.key, alert.id))
    }
}

export function* addAlertSideEffect(config: IConfig, action: Add | AddMulti) {
    try {
        const { alerts: incomingAlerts, alert: incomingAlert } = action.payload
        /* FIXME массив генераторов removeAlertSideEffect */
        let effects: unknown[] = []

        const alerts: IAlert[] = incomingAlert
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

export function getTimeout(alert: IAlert, config: IConfig) {
    return alert.timeout || (config.timeout && config.timeout[alert.severity])
}

export default (config: IConfig) => [takeEvery([addAlert, addMultiAlerts], addAlertSideEffect, config)]
