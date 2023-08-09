import React from 'react'
import { NavLink } from 'react-router-dom'

import { id as generateId } from '../../../../../utils/id'
import { ExtendedTooltipComponent } from '../../../../../components/snippets/Tooltip/TooltipHOC'
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
}

export function InnerLink(props: InnerLinkProps) {
    const {
        href,
        title,
        isMiniView,
        sidebarOpen,
        isStaticView,
        showContent,
        item,
    } = props

    const id = generateId()

    const hint = isMiniView ? title : null

    return (
        <ExtendedTooltipComponent
            hint={hint}
            placement="right"
        >
            <NavLink
                exact
                to={href}
                className="n2o-sidebar__item"
                activeClassName="active"
                id={id}
            >
                <LinkBody
                    {...item}
                    sidebarOpen={sidebarOpen}
                    isStaticView={isStaticView}
                    showContent={showContent}
                    isMiniView={isMiniView}
                />
            </NavLink>
        </ExtendedTooltipComponent>
    )
}
