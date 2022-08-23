import { call, put, select, takeEvery, all, delay } from 'redux-saga/effects'
import take from 'lodash/take'

import { addAlert, addMultiAlerts, removeAlert, alertsByKeySelector } from './store'
import { ALLOWED_ALERTS_QUANTITY } from './constants'

function getStopped(alertsByKey, targetId) {
    const alertProps = alertsByKey?.find(({ id }) => id === targetId)

    return alertProps ? alertProps.stopped : true
}

export function* removeAlertSideEffect(action, alert, timeout) {
    yield delay(timeout)
    const alertsByKey = yield select(alertsByKeySelector(action.payload.key))

    const wasStopped = getStopped(alertsByKey, alert.id)

    if (alertsByKey && !wasStopped) {
        yield put(removeAlert(action.payload.key, alert.id))
    }
}

export function* addAlertSideEffect(config, action) {
    try {
        const { alerts: incomingAlerts } = action.payload
        let effects = []

        const alerts = take(incomingAlerts, ALLOWED_ALERTS_QUANTITY)

        for (let i = 0; i < alerts.length; i++) {
            const timeout = yield call(getTimeout, alerts[i], config)

            if (timeout) {
                effects = [
                    ...effects,
                    removeAlertSideEffect(action, alerts[i], timeout),
                ]
            }
        }
        yield all(effects)
    } catch (e) {
        // eslint-disable-next-line no-console
        console.error(e)
    }
}

export function getTimeout(alert, config) {
    return alert.timeout || (config.timeout && config.timeout[alert.severity])
}

export default config => [takeEvery([addAlert, addMultiAlerts], addAlertSideEffect, config)]
