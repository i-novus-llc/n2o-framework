import React from 'react'
import classNames from 'classnames'
import { Panel } from '@i-novus/n2o-components/lib/navigation/Panel'

import { type NavigationPanelProps } from './types'
import { FactoryChildren } from './FactoryChildren'

export function NavigationPanel({ content, className, ...rest }: NavigationPanelProps) {
    return (
        <Panel className={classNames('n2o-navigation-panel', className)} {...rest} childrenClassName="n2o-navigation-panel__children">
            <FactoryChildren
                rootClassName="n2o-navigation-panel__children"
                content={content}
            />
        </Panel>
    )
}
