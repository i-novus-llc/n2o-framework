import React from 'react'
import { Group } from '@i-novus/n2o-components/lib/navigation/Group'

import { useLinkPropsResolver } from './useLinkPropsResolver'
import { useFactoryChildren } from './FactoryChildren'
import { type NavigationGroupProps } from './types'

export function NavigationGroup({
    content,
    ...props
}: NavigationGroupProps) {
    const {
        url: href,
        visible,
        ...resolved
    } = useLinkPropsResolver(props)
    const children = useFactoryChildren({ content })

    if (!visible) { return null }

    return (
        <Group {...resolved}>
            {children}
        </Group>
    )
}
