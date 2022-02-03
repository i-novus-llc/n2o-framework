import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'

import { Alerts } from '../snippets/Alerts/Alerts'
import { GLOBAL_KEY } from '../../ducks/alerts/constants'
import { alertsByKeySelector, removeAlert } from '../../ducks/alerts/store'

/**
 * Глобальные алерты
 * @param {array} alerts - массив алертов
 * @param {function} onDismiss - функция закрытия
 * @param getTimeAgo
 * @returns {JSX.Element}
 */

export function GlobalAlerts({ alerts = [], onDismiss, getTimeAgo }) {
    const mappedAlerts = alerts.map(alert => ({
        ...alert,
        key: alert.id,
        onDismiss: () => alert.id && onDismiss(alert.id),
        timestamp: getTimeAgo(alert.timestamp),
        className: 'd-inline-flex mb-0 p-2 mw-100',
        animate: true,
        position: 'fixed',
    }))

    return (
        <Alerts alerts={mappedAlerts} />
    )
}

GlobalAlerts.propTypes = {
    alerts: PropTypes.array,
    onDismiss: PropTypes.func,
    getTimeAgo: PropTypes.func,
}

GlobalAlerts.defaultProps = {
    getTimeAgo: () => {},
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
