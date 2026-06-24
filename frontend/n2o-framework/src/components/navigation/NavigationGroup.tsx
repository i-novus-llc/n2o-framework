import React, { ReactNode } from 'react'
import { Group } from '@i-novus/n2o-components/lib/navigation/Group'

import { useLinkPropsResolver } from './useLinkPropsResolver'
import { useFactoryChildren } from './FactoryChildren'
import { type NavigationGroupProps } from './types'
import { hasVisibleContent } from './helpers'

export function NavigationGroup({
    content,
    ...props
}: NavigationGroupProps) {
    const { url: href, visible, ...resolved } = useLinkPropsResolver(props)

    const children: ReactNode = useFactoryChildren({ content })

    if (!visible) { return null }

    if (!hasVisibleContent(content)) { return null }

    return <Group {...resolved}>{children}</Group>
}
