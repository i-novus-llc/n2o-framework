import React from 'react'
import { connect } from 'react-redux'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import { Dispatch } from 'redux'

import { Alerts } from '../snippets/Alerts/Alerts'
import { PLACEMENT, STORE_KEY_PATH, SUPPORTED_PLACEMENTS } from '../../ducks/alerts/constants'
import { removeAlert, stopRemovingAlert } from '../../ducks/alerts/store'
import { State } from '../../ducks/State'
import { Alert } from '../../ducks/alerts/Alerts'

dayjs.extend(relativeTime)

interface Props {
    alerts: Alert[][];
    onDismiss(storeKey: string, alertId: string): void;
    stopRemoving(storeKey: string, alertId: string): void;
}

const getTimestamp = (time?: string): string | null => {
    if (!time) { return null }

    let timestamp = dayjs(time).fromNow()

    if (timestamp === 'несколько секунд назад') { timestamp = 'только что' }

    return timestamp
}

/**
 * Глобальные алерты
 */
export const GlobalAlerts = ({
    onDismiss,
    stopRemoving,
    alerts: reduxAlerts = [[]],
}: Props) => {
    const alerts = reduxAlerts.flat().map((alert) => {
        const { time, id } = alert

        const storeKey = alert[STORE_KEY_PATH]

        return {
            ...alert,
            key: id,
            onDismiss: () => id && onDismiss(storeKey, id),
            stopRemoving: () => id && stopRemoving(storeKey, id),
            timestamp: getTimestamp(time),
            className: 'd-inline-flex mb-0 p-2 mw-100',
            animate: true,
            position: 'fixed',
            id,
        }
    })

    return <Alerts alerts={alerts} placements={SUPPORTED_PLACEMENTS as PLACEMENT[]} />
}

const mapStateToProps = (state: State) => ({ alerts: Object.values(state.alerts) })

const mapDispatchToProps = (dispatch: Dispatch) => ({
    onDismiss: (storeKey: string, alertId: string) => dispatch(removeAlert(storeKey, alertId)),
    stopRemoving: (storeKey: string, alertId: string) => dispatch(stopRemovingAlert(storeKey, alertId)),
})

export const GlobalAlertsConnected = connect(mapStateToProps, mapDispatchToProps)(GlobalAlerts)
