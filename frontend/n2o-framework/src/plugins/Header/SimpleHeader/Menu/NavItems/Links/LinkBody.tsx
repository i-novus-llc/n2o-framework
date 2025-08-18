import React from 'react'
import classNames from 'classnames'
import { BadgeProps } from 'reactstrap'
import { Badge } from '@i-novus/n2o-components/lib/display/Badge/Badge'
import { NavItemImage, type ImageShape } from '@i-novus/n2o-components/lib/display/NavItemImage'

import { IconContainer, ICON_POSITIONS } from '../../../../../../components/snippets/IconContainer/IconContainer'
import { needRender } from '../../../../../SideBar/utils'

interface LinkBodyProps {
    imageSrc?: string
    icon?: string
    title?: string
    imageShape?: ImageShape
    badge?: BadgeProps
    iconPosition?: ICON_POSITIONS
    className?: string
}

export function LinkBody({ imageSrc, icon, title, imageShape, badge, className, iconPosition = ICON_POSITIONS.LEFT }: LinkBodyProps) {
    return (
        <>
            <NavItemImage imageSrc={imageSrc} title={title} imageShape={imageShape} />
            <IconContainer className={classNames('n2o-link-icon-container', className)} icon={icon} iconPosition={iconPosition}>
                {!imageSrc && icon && <i className={classNames('mr-1', icon)} />}
                <Badge {...badge}>{needRender(title) && title}</Badge>
            </IconContainer>
        </>
    )
}
