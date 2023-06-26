import React from 'react'
import { NavLink } from 'react-router-dom'

import { id as generateId } from '../../utils/id'
import { ExtendedTooltipComponent } from '../../components/snippets/Tooltip/TooltipHOC'

import { linkTypes } from './LinkTypes'
import { ItemContent } from './ItemContent'

export function InnerLink({
    href,
    title,
    isMiniView,
    sidebarOpen,
    isStaticView,
    showContent,
    item,
}) {
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
                <ItemContent
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

InnerLink.propTypes = linkTypes
