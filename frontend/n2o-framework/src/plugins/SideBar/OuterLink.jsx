import React from 'react'

import { id as generateId } from '../../utils/id'
import { ExtendedTooltipComponent } from '../../components/snippets/Tooltip/TooltipHOC'

import { linkTypes } from './LinkTypes'
import { ItemContent } from './ItemContent'

export function OuterLink({ href, title, isStaticView, showContent, sidebarOpen, isMiniView, item }) {
    const id = generateId()

    const hint = isMiniView ? title : null

    return (
        <ExtendedTooltipComponent hint={hint} placement="right">
            <a id={id} className="n2o-sidebar__item" href={href}>
                <ItemContent
                    {...item}
                    sidebarOpen={sidebarOpen}
                    isStaticView={isStaticView}
                    showContent={showContent}
                    isMiniView={isMiniView}
                />
            </a>
        </ExtendedTooltipComponent>
    )
}

OuterLink.propTypes = linkTypes
