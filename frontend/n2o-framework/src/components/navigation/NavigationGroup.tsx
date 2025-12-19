import React from 'react'
import { Group } from '@i-novus/n2o-components/lib/navigation/Group'
import get from 'lodash/get'

import { type Model } from '../../ducks/models/selectors'

import { useLinkPropsResolver } from './useLinkPropsResolver'
import { useFactoryChildren } from './FactoryChildren'
import { type NavigationGroupProps } from './types'

export function NavigationGroup({
    model: modelPrefix,
    models,
    content,
    visible: propsVisible = true,
    enabled = true,
    ...rest
}: NavigationGroupProps) {
    const model = get(models, modelPrefix, {}) as Model

    const { visible, disabled } = useLinkPropsResolver({ model, visible: propsVisible, enabled })
    const children = useFactoryChildren({ content })

    if (!visible) { return null }

    return (
        <Group {...rest} disabled={disabled}>
            {children}
        </Group>
    )
}
