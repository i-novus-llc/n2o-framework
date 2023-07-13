import React from 'react'
import classNames from 'classnames'

import { NavItemImage } from '../../components/snippets/NavItemImage/NavItemImage'
import { Badge } from '../../components/snippets/Badge/Badge'
import { IItem } from '../Header/SimpleHeader/Menu/Item'

// @ts-ignore ignore import error from js file
import { Icon, Title, getCurrentTitle } from './utils'

export function ItemContent(props: IItem) {
    const { icon, title, src, sidebarOpen, imageSrc, imageShape, badge, isStaticView, showContent, isMiniView } = props

    const currentTitle = getCurrentTitle(isMiniView, icon, title, imageSrc)

    return (
        <>
            <Icon icon={icon} title={title} type={src} sidebarOpen={sidebarOpen} />
            <NavItemImage imageSrc={imageSrc} title={title} imageShape={imageShape} />
            <Badge {...badge}>
                <Title
                    title={currentTitle}
                    className={
                        classNames(
                            'n2o-sidebar__item-title',
                            { visible: isStaticView ? true : showContent },
                        )
                    }
                />
            </Badge>
        </>
    )
}
