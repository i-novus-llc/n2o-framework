import React from 'react'
import { Group } from '@i-novus/n2o-components/lib/navigation/Group'
import get from 'lodash/get'

import { useLink } from '../../core/router/useLink'
import { type Model } from '../../ducks/models/selectors'

import { FactoryChildren } from './FactoryChildren'
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

    const { visible, disabled } = useLink({ model, visible: propsVisible, enabled })

    if (!visible) { return null }

    return (
        <Group {...rest} disabled={disabled}>
            <FactoryChildren content={content} />
        </Group>
    )
}
