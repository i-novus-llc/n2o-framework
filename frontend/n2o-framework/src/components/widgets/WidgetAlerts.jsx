import React from 'react'
import PropTypes from 'prop-types'
import map from 'lodash/map'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'

import { Alerts } from '../snippets/Alerts/Alerts'
import { removeAlert, alertsByKeySelector } from '../../ducks/alerts/store'

/**
 * Компонент-редакс-обертка над алертами виджета
 * @reactProps {string} widgetId - уникальный индефикатор виджета
 * @reactProps {array} alerts - массив алертов
 */
export function WidgetAlerts(props) {
    const { alerts, onDismiss } = props
    const mapAlertsProps = (alerts, onDismiss) => map(alerts, alert => ({
        ...alert,
        key: alert.id,
        onDismiss: () => onDismiss(alert.id),
        details: alert.stacktrace,
    }))

    return (
        <div className="n2o-alerts">
            <Alerts alerts={mapAlertsProps(alerts, onDismiss)} />
        </div>
    )
}

WidgetAlerts.propTypes = {
    alerts: PropTypes.array,
    onDismiss: PropTypes.func,
}

const mapStateToProps = createStructuredSelector({
    alerts: (state, props) => alertsByKeySelector(props.widgetId)(state, props),
})

function mapDispatchToProps(dispatch, ownProps) {
    return {
        onDismiss: (alertId) => {
            dispatch(removeAlert(ownProps.widgetId, alertId))
        },
    }
}

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(WidgetAlerts)
