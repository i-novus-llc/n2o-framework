import React from 'react'

import { OUTER_LINK_TYPE } from '../../../../constants'
import { Item } from '../../../../CommonMenuTypes'

import { OuterLink } from './OuterLink'
import { InnerLink } from './InnerLink'

interface LinkProps {
    item: Item
    sidebarOpen: boolean
    isMiniView: boolean
    isStaticView: boolean
    showContent: boolean
}

export function Link(props: LinkProps) {
    const { item, sidebarOpen, isMiniView, isStaticView, showContent } = props
    const { linkType, href } = item

    if (linkType === OUTER_LINK_TYPE) {
        return (
            <OuterLink
                sidebarOpen={sidebarOpen}
                isMiniView={isMiniView}
                item={item}
                isStaticView={isStaticView}
                showContent={showContent}
                href={href}
            />
        )
    }

    return (
        <InnerLink
            sidebarOpen={sidebarOpen}
            isMiniView={isMiniView}
            item={item}
            isStaticView={isStaticView}
            showContent={showContent}
            href={href}
        />
    )
}
