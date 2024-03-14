import React from 'react'
import classNames from 'classnames'
import { BadgeProps } from 'reactstrap'

import { Badge } from '@i-novus/n2o-components/lib/display/Badge/Badge'
import { NavItemImage } from '@i-novus/n2o-components/lib/display/NavItemImage'

import { IconContainer, ICON_POSITIONS } from '../../../../../../components/snippets/IconContainer/IconContainer'
import { needRender } from '../../../../../SideBar/utils'

interface LinkBodyProps {
    imageSrc?: string
    icon?: string
    title?: string
    imageShape?: string
    badge?: BadgeProps
    iconPosition?: ICON_POSITIONS
}

export function LinkBody(props: LinkBodyProps) {
    const { imageSrc, icon, title, imageShape, badge, iconPosition = ICON_POSITIONS.LEFT } = props

    return (
        <>
            <NavItemImage imageSrc={imageSrc} title={title} imageShape={imageShape} />
            <IconContainer className="n2o-link-icon-container" icon={icon} iconPosition={iconPosition}>
                {!imageSrc && icon && <i className={classNames('mr-1', icon)} />}
                <Badge {...badge}>{needRender(title) && title}</Badge>
            </IconContainer>
        </>
    )
}
