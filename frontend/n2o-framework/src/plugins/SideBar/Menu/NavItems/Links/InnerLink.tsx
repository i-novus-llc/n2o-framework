import React from 'react'
import { NavLink } from 'react-router-dom'

import { id as generateId } from '../../../../../utils/id'
import { ExtendedTooltipComponent } from '../../../../../components/snippets/Tooltip/TooltipHOC'
import { IItem } from '../../../../CommonMenuTypes'

import { LinkBody } from './LinkBody'

interface IInnerLink {
    href: string
    title?: string
    isMiniView: boolean
    sidebarOpen: boolean
    isStaticView: boolean
    showContent: boolean
    item: IItem
}

export function InnerLink(props: IInnerLink) {
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
