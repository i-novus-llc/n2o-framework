import React from 'react'
import classNames from 'classnames'
import { Panel } from '@i-novus/n2o-components/lib/navigation/Panel'

import { type NavigationPanelProps } from './types'
import { useFactoryChildren } from './FactoryChildren'

export function NavigationPanel({ content, className, ...rest }: NavigationPanelProps) {
    const children = useFactoryChildren({ content, className: 'n2o-navigation-panel__children' })

    return (
        <Panel className={classNames('n2o-navigation-panel', className)} {...rest}>
            {children}
        </Panel>
    )
}
