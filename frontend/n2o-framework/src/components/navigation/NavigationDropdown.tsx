import React from 'react'
import get from 'lodash/get'
import { Dropdown } from '@i-novus/n2o-components/lib/navigation/Dropdown'

import { type Model } from '../../ducks/models/selectors'

import { useLinkPropsResolver } from './useLinkPropsResolver'
import { useFactoryChildren } from './FactoryChildren'
import { type NavigationDropdownProps } from './types'

export function NavigationDropdown({
    model: modelPrefix,
    models,
    content,
    visible: propsVisible = true,
    enabled = true,
    ...rest
}: NavigationDropdownProps) {
    const model = get(models, modelPrefix, {}) as Model

    const { visible, disabled } = useLinkPropsResolver({ model, visible: propsVisible, enabled })
    const children = useFactoryChildren({ content })

    if (!visible) { return null }

    return (
        <Dropdown {...rest} disabled={disabled}>
            {children}
        </Dropdown>
    )
}
