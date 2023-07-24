import React from 'react'
import classNames from 'classnames'
import { BadgeProps } from 'reactstrap'

import { Badge } from '@i-novus/n2o-components/lib/display/Badge/Badge'

import { NavItemImage } from '../../../../../../components/snippets/NavItemImage/NavItemImage'

interface ILinkBody {
    imageSrc?: string
    icon?: string
    title?: string
    imageShape?: string
    badge?: BadgeProps
}

export function LinkBody(props: ILinkBody) {
    const { imageSrc, icon, title, imageShape, badge } = props

    return (
        <>
            {!imageSrc && icon && <i className={classNames('mr-1', icon)} />}
            <NavItemImage imageSrc={imageSrc} title={title} imageShape={imageShape} />
            <Badge {...badge}>{title}</Badge>
        </>
    )
}
