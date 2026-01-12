import React from 'react'

import { Item } from '../../../../CommonMenuTypes'

import { LinkBody } from './LinkBody'

interface OuterLinkProps {
    href: string
    title?: string
    isStaticView: boolean
    showContent: boolean
    sidebarOpen: boolean
    isMiniView: boolean
    item: Item
    id: string
    forwardedRef?: React.Ref<HTMLAnchorElement>
}

export function OuterLink({ href, isStaticView, showContent, sidebarOpen, isMiniView, item, id, forwardedRef }: OuterLinkProps) {
    return (
        <a id={id} className="n2o-sidebar__item" href={href} ref={forwardedRef}>
            <LinkBody
                {...item}
                sidebarOpen={sidebarOpen}
                isStaticView={isStaticView}
                showContent={showContent}
                isMiniView={isMiniView}
            />
        </a>
    )
}
