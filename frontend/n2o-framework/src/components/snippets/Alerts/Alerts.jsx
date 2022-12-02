import React, { useContext } from 'react'
import PropTypes from 'prop-types'

import { FactoryContext } from '../../../core/factory/context'
import { SNIPPETS } from '../../../core/factory/factoryLevels'

/**
 * Маппер для Alert
 * supported placements ['topLeft', 'top', 'topRight', 'bottomLeft', 'bottom', 'bottomRight']
 */

function AlertsByPlacement({ alerts, placement }) {
    const { getComponent } = useContext(FactoryContext)

    return alerts.filter(({ placement: alertPlacement }) => alertPlacement === placement)
        .map((props) => {
            const Alert = getComponent('Alert', SNIPPETS)

            return <Alert {...props} />
        })
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
                    <section key={`${placement}`} className={`n2o-alerts ${placement} ${placement.startsWith('bottom') ? 'n2o-alerts_reversed' : ''}`}>
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
