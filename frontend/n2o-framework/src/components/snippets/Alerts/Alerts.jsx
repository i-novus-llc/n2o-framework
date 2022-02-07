import React from 'react'
import PropTypes from 'prop-types'

import Alert from './Alert'

/**
 * Маппер для Alert
 * placement supported ('top', 'bottom', 'topLeft', 'topRight', 'bottomLeft', 'bottomRight')
 */

export function Alerts({ alerts = [] }) {
    const placement = alerts.length > 0 ? alerts[0].placement || 'top' : null

    return (
        <div className="n2o-alerts-container">
            {alerts.length > 0 && (
                <div className={`n2o-alerts ${placement}`}>
                    {alerts.map(alert => <Alert {...alert} />)}
                </div>
            )}
        </div>
    )
}

Alerts.propTypes = {
    alerts: PropTypes.array,
}

export default Alerts
