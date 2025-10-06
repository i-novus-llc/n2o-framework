import React from 'react'
import { Group } from '@i-novus/n2o-components/lib/navigation/Group'
import get from 'lodash/get'

import { useLink } from '../../core/router/useLink'
import { WithDataSource } from '../../core/datasource/WithDataSource'
import { type Model } from '../../ducks/models/selectors'

import { FactoryChildren } from './FactoryChildren'
import { type NavigationGroupProps } from './types'

export function Component({
    model: modelPrefix,
    models,
    content,
    visible = true,
    enabled = true,
    ...rest
}: NavigationGroupProps) {
    const model = get(models, modelPrefix, {}) as Model

    const resolvedProps = useLink({ model, visible, enabled })

    return (
        <Group
            {...rest}
            visible={resolvedProps.visible}
            enabled={resolvedProps.enabled}
        >
            <FactoryChildren content={content} />
        </Group>
    )
}

export const NavigationGroup = WithDataSource<NavigationGroupProps>(Component)
