import React from 'react'

import { id as generateId } from '../../../../../utils/id'
import { ExtendedTooltipComponent } from '../../../../../components/snippets/Tooltip/TooltipHOC'
import { IItem } from '../../../../CommonMenuTypes'

import { LinkBody } from './LinkBody'

interface IOuterLink {
    href: string
    title?: string
    isStaticView: boolean
    showContent: boolean
    sidebarOpen: boolean
    isMiniView: boolean
    item: IItem
}

export function OuterLink(props: IOuterLink) {
    const { href, title, isStaticView, showContent, sidebarOpen, isMiniView, item } = props
    const id = generateId()

    const hint = isMiniView ? title : null

    return (
        <ExtendedTooltipComponent hint={hint} placement="right">
            <a id={id} className="n2o-sidebar__item" href={href}>
                <LinkBody
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
