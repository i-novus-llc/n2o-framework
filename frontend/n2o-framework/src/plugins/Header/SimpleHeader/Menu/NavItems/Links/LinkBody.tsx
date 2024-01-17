import React from 'react'
import classNames from 'classnames'
import { BadgeProps } from 'reactstrap'

import { Badge } from '@i-novus/n2o-components/lib/display/Badge/Badge'
import { NavItemImage } from '@i-novus/n2o-components/lib/display/NavItemImage'

import { needRender } from '../../../../../SideBar/utils'

interface LinkBodyProps {
    imageSrc?: string
    icon?: string
    title?: string
    imageShape?: string
    badge?: BadgeProps
}

export function LinkBody(props: LinkBodyProps) {
    const { imageSrc, icon, title, imageShape, badge } = props

    return (
        <>
            {!imageSrc && icon && <i className={classNames('mr-1', icon)} />}
            <NavItemImage imageSrc={imageSrc} title={title} imageShape={imageShape} />
            <Badge {...badge}>{needRender(title) && title}</Badge>
        </>
    )
}
