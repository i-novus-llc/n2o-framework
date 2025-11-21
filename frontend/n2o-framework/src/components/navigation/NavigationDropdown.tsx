import React from 'react'
import get from 'lodash/get'
import { Dropdown } from '@i-novus/n2o-components/lib/navigation/Dropdown'

import { type Model } from '../../ducks/models/selectors'
import { useLink } from '../../core/router/useLink'

import { FactoryChildren } from './FactoryChildren'
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

    const { visible, disabled } = useLink({ model, visible: propsVisible, enabled })

    if (!visible) { return null }

    return (
        <Dropdown {...rest} disabled={disabled}>
            <FactoryChildren content={content} />
        </Dropdown>
    )
}
