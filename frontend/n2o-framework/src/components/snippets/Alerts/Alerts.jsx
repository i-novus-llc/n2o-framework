import React from 'react'
import PropTypes from 'prop-types'

import Alert from './Alert'

/**
 * Маппер для Alert
 */

export function Alerts({ alerts = [] }) {
    const placement = alerts.length > 0 ? alerts[0].placement || 'bottom' : null

    return (
        <div className="n2o-alerts-container">
            {alerts.length > 0 && (
                <div className={`n2o-alerts--fixed ${placement}`}>
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
