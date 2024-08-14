import React from 'react'

import { OUTER_LINK_TYPE } from '../../../../constants'
import { Item } from '../../../../CommonMenuTypes'
import { Tooltip } from '../../../../../components/snippets/Tooltip/TooltipHOC'
import { id as generateId } from '../../../../../utils/id'

import { OuterLink } from './OuterLink'
import { InnerLink } from './InnerLink'

interface LinkProps {
    item: Item
    sidebarOpen: boolean
    isMiniView: boolean
    isStaticView: boolean
    showContent: boolean
}

export function Link({ item, sidebarOpen, isMiniView, isStaticView, showContent }: LinkProps) {
    const { linkType, href, title } = item

    const hint = isMiniView ? title : null
    const id = generateId()
    const linkProps = {
        sidebarOpen,
        isMiniView,
        item,
        isStaticView,
        showContent,
        href,
        title,
        id,
    }

    return (
        <Tooltip placement="right" hint={hint}>
            {linkType === OUTER_LINK_TYPE ? <OuterLink {...linkProps} /> : <InnerLink {...linkProps} /> }
        </Tooltip>
    )
}
