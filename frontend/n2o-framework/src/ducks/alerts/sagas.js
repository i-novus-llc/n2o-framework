import { call, put, select, takeEvery, all, delay } from 'redux-saga/effects'

import { addAlert, addMultiAlerts, removeAlert, alertsByKeySelector } from './store'

function getStopped(alertsByKey, targetId) {
    if (!alertsByKey.length) {
        return null
    }

    const alertProps = alertsByKey
        .find(({ id }) => id === targetId)

    return alertProps.stopped
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
        const { alerts } = action.payload
        let effects = []

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
