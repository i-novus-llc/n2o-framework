import React from 'react'
import PropTypes from 'prop-types'

import Alert from './Alert'

/**
 * Маппер для Alert
 * supported placements ['topLeft', 'top', 'topRight', 'bottomLeft', 'bottom', 'bottomRight']
 */

function AlertsByPlacement({ alerts, placement }) {
    return alerts.filter(({ placement: alertPlacement }) => alertPlacement === placement)
        .map(alert => <Alert {...alert} />)
}

AlertsByPlacement.propTYpes = {
    alerts: PropTypes.array,
    placement: PropTypes.string,
}

export function Alerts({ alerts = [], placements }) {
    return (
        <div className="n2o-alerts-container">
            {placements.map(
                placement => (
                    <section className={`n2o-alerts ${placement}`}>
                        <AlertsByPlacement
                            alerts={alerts}
                            placement={placement}
                        />
                    </section>
                ),
            )}
        </div>
    )
}

Alerts.propTypes = {
    alerts: PropTypes.array,
    placements: PropTypes.array,
}

export default Alerts
