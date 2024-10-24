import React, { useContext } from 'react'

import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { Alert } from '../../../ducks/alerts/Alerts'
import { PLACEMENT } from '../../../ducks/alerts/constants'

interface AlertsByPlacementProps {
    alerts: Alert[]
    placement: PLACEMENT
}

function AlertsByPlacement({ alerts, placement }: AlertsByPlacementProps) {
    const { getComponent } = useContext(FactoryContext)
    const Alert = getComponent('Alert', FactoryLevels.SNIPPETS)

    if (!Alert) { return null }

    return (
        <>
            {alerts
                .filter(({ placement: alertPlacement }) => alertPlacement === placement)
                .map(props => <Alert {...props} key={props.id} />)}
        </>
    )
}

export interface Props extends Omit<AlertsByPlacementProps, 'placement'> {
    placements: PLACEMENT[]
}

export function Alerts({ placements, alerts = [] }: Props) {
    return (
        <div className="n2o-alerts-container">
            {placements.map(
                placement => (
                    <section
                        key={placement}
                        className={`n2o-alerts ${placement} ${placement.startsWith('bottom') ? 'n2o-alerts_reversed' : ''}`}
                    >
                        <AlertsByPlacement alerts={alerts} placement={placement} />
                    </section>
                ),
            )}
        </div>
    )
}

export default Alerts
