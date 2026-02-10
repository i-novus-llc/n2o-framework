import React from 'react'
import { Dropdown } from '@i-novus/n2o-components/lib/navigation/Dropdown'

import { useLinkPropsResolver } from './useLinkPropsResolver'
import { useFactoryChildren } from './FactoryChildren'
import { type NavigationDropdownProps } from './types'

export function NavigationDropdown({
    content,
    ...props
}: NavigationDropdownProps) {
    const {
        url: href,
        visible,
        ...resolved
    } = useLinkPropsResolver(props)
    const children = useFactoryChildren({ content })

    if (!visible) { return null }

    return (
        <Dropdown {...resolved}>
            {children}
        </Dropdown>
    )
}
