import React from 'react'
import { NavLink } from 'react-router-dom'

import { Item } from '../../../../CommonMenuTypes'

import { LinkBody } from './LinkBody'

interface InnerLinkProps {
    href: string
    title?: string
    isMiniView: boolean
    sidebarOpen: boolean
    isStaticView: boolean
    showContent: boolean
    item: Item
    id: string
    forwardedRef?: React.Ref<HTMLAnchorElement>
}

export function InnerLink(props: InnerLinkProps) {
    const {
        href,
        isMiniView,
        sidebarOpen,
        isStaticView,
        showContent,
        item,
        id,
        forwardedRef,
    } = props

    return (
        <NavLink
            exact
            to={href}
            className="n2o-sidebar__item"
            activeClassName="active"
            id={id}
            ref={forwardedRef}
        >
            <LinkBody
                {...item}
                sidebarOpen={sidebarOpen}
                isStaticView={isStaticView}
                showContent={showContent}
                isMiniView={isMiniView}
            />
        </NavLink>
    )
}
