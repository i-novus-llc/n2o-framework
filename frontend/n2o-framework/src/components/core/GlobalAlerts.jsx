import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'
import moment from 'moment/moment'

import { Alerts } from '../snippets/Alerts/Alerts'
import { STORE_KEY_PATH, SUPPORTED_PLACEMENTS } from '../../ducks/alerts/constants'
import { removeAlert, stopRemoving } from '../../ducks/alerts/store'

const getTimestamp = (time) => {
    if (!time) {
        return null
    }

    let timestamp = moment(time).fromNow()

    if (timestamp === 'несколько секунд назад') {
        timestamp = 'только что'
    }

    return timestamp
}

/**
 * Глобальные алерты
 * @returns {JSX.Element}
 * @param isoTime
 */

export function GlobalAlerts({ alerts = [[]], onDismiss, stopRemoving }) {
    const mappedAlerts = alerts.flat().map((alert) => {
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

    return (
        <Alerts alerts={mappedAlerts} placements={SUPPORTED_PLACEMENTS} />
    )
}

GlobalAlerts.propTypes = {
    alerts: PropTypes.array,
    onDismiss: PropTypes.func,
    stopRemoving: PropTypes.func,
}

const mapStateToProps = createStructuredSelector({
    alerts: state => Object.values(state.alerts),
})

const mapDispatchToProps = dispatch => ({
    onDismiss: (storeKey, alertId) => dispatch(removeAlert(storeKey, alertId)),
    stopRemoving: (storeKey, alertId) => dispatch(stopRemoving(storeKey, alertId)),
})

export const GlobalAlertsConnected = connect(
    mapStateToProps,
    mapDispatchToProps,
)(GlobalAlerts)
