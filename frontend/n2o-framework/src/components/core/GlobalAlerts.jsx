import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'
import moment from 'moment'

import { Alerts } from '../snippets/Alerts/Alerts'
import { GLOBAL_KEY } from '../../ducks/alerts/constants'
import { alertsByKeySelector, removeAlert } from '../../ducks/alerts/store'

/**
 * Глобальные алерты
 * @returns {JSX.Element}
 * @param isoTime
 */

export function GlobalAlerts({ alerts = [], onDismiss }) {
    const mappedAlerts = alerts.map((alert) => {
        const { time, id } = alert

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
            onDismiss: () => id && onDismiss(id),
            timestamp: getTimestamp(time),
            className: 'd-inline-flex mb-0 p-2 mw-100',
            animate: true,
            position: 'fixed',
        }
    })

    return (
        <Alerts alerts={mappedAlerts} />
    )
}

GlobalAlerts.propTypes = {
    alerts: PropTypes.array,
    onDismiss: PropTypes.func,
}

const mapStateToProps = createStructuredSelector({
    alerts: (state, props) => alertsByKeySelector(GLOBAL_KEY)(state, props),
})

const mapDispatchToProps = dispatch => ({
    onDismiss: alertId => dispatch(removeAlert(GLOBAL_KEY, alertId)),
})

export const GlobalAlertsConnected = connect(
    mapStateToProps,
    mapDispatchToProps,
)(GlobalAlerts)
