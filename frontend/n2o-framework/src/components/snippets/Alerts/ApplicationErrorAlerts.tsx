import React from 'react'

// @ts-ignore ignore import error from js file
import { FactoryProvider } from '../../../core/factory/FactoryProvider'
// @ts-ignore ignore import error from js file
import { GlobalAlertsConnected } from '../../core/GlobalAlerts'

export function ApplicationErrorAlerts({ config }: {config: object}): JSX.Element {
    return (
        <FactoryProvider config={config} securityBlackList={['actions']}>
            <GlobalAlertsConnected />
        </FactoryProvider>
    )
}
