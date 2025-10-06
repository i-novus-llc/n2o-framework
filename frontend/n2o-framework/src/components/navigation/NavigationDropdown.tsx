import React from 'react'
import get from 'lodash/get'
import { Dropdown } from '@i-novus/n2o-components/lib/navigation/Dropdown'

import { WithDataSource } from '../../core/datasource/WithDataSource'
import { type Model } from '../../ducks/models/selectors'
import { useLink } from '../../core/router/useLink'

import { FactoryChildren } from './FactoryChildren'
import { type NavigationDropdownProps } from './types'

export function Component({
    model: modelPrefix,
    models,
    content,
    visible = true,
    enabled = true,
    ...rest
}: NavigationDropdownProps) {
    const model = get(models, modelPrefix, {}) as Model

    const resolvedProps = useLink({ model, visible, enabled })

    return (
        <Dropdown
            {...rest}
            visible={resolvedProps.visible}
            enabled={resolvedProps.enabled}
        >
            <FactoryChildren content={content} />
        </Dropdown>
    )
}

export const NavigationDropdown = WithDataSource<NavigationDropdownProps>(Component)
