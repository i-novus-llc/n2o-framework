import React from 'react'
import map from 'lodash/map'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'

// eslint-disable-next-line import/no-named-as-default
import Alerts from '../snippets/Alerts/Alerts'
import { GLOBAL_KEY } from '../../ducks/alerts/constants'
import { alertsByKeySelector, removeAlert } from '../../ducks/alerts/store'

/**
 * Глобальные алерты
 * @param {array} alerts - массив алертов
 * @param {function} onDismiss - функция закрытия
 * @returns {JSX.Element}
 */
export function GlobalAlerts({ alerts, onDismiss }) {
    const handleDismiss = alertId => alertId && onDismiss(alertId)
    const mapAlertsProps = () => map(alerts, alert => ({
        ...alert,
        key: alert.id,
        onDismiss: () => handleDismiss(alert.id),
        className: 'd-inline-flex mb-0 p-2 mw-100',
        details: alert.stacktrace,
        animate: true,
        position: 'relative',
    }))

    return (
        <div className="n2o-global-alerts d-flex justify-content-center">
            <Alerts alerts={mapAlertsProps()} />
        </div>
    )
}

GlobalAlerts.propTypes = {
    alerts: PropTypes.array,
    onDismiss: PropTypes.func,
}

GlobalAlerts.defaultProps = {
    alerts: [],
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
