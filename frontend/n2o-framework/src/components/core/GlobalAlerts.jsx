import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'
import moment from 'moment'

import { Alerts } from '../snippets/Alerts/Alerts'
import { STORE_KEY_PATH, SUPPORTED_PLACEMENTS } from '../../ducks/alerts/constants'
import { removeAlert } from '../../ducks/alerts/store'

/**
 * Глобальные алерты
 * @returns {JSX.Element}
 * @param isoTime
 */

export function GlobalAlerts({ alerts = [], onDismiss }) {
    const mappedAlerts = alerts.map((alertsGroup) => {
        /* 1 alert в каждом поддерживаемом placement
           до реализации stacked */
        const alert = alertsGroup[0]

        const { time, id } = alert

        const storeKey = alert[STORE_KEY_PATH]

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

        return {
            ...alert,
            key: id,
            onDismiss: () => id && onDismiss(storeKey, id),
            timestamp: getTimestamp(time),
            className: 'd-inline-flex mb-0 p-2 mw-100',
            animate: true,
            position: 'fixed',
        }
    })

    return (
        <Alerts alerts={mappedAlerts} placements={SUPPORTED_PLACEMENTS} />
    )
}

GlobalAlerts.propTypes = {
    alerts: PropTypes.array,
    onDismiss: PropTypes.func,
}

const mapStateToProps = createStructuredSelector({
    alerts: state => Object.values(state.alerts),
})

const mapDispatchToProps = dispatch => ({
    onDismiss: (storeKey, alertId) => dispatch(removeAlert(storeKey, alertId)),
})

export const GlobalAlertsConnected = connect(
    mapStateToProps,
    mapDispatchToProps,
)(GlobalAlerts)
