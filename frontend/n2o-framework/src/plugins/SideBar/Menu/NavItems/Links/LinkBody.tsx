import React from 'react'
import classNames from 'classnames'

import { NavItemImage } from '../../../../../components/snippets/NavItemImage/NavItemImage'
import { Badge } from '../../../../../components/snippets/Badge/Badge'
import { IItem } from '../../../../CommonMenuTypes'
import { Icon, Title, getCurrentTitle } from '../../../utils'

export interface IItemContent extends IItem {
    sidebarOpen: boolean
    isStaticView: boolean
    showContent: boolean
    isMiniView: boolean
}

export function LinkBody(props: IItemContent) {
    const { icon, title, src, sidebarOpen, imageSrc, imageShape, badge, isStaticView, showContent, isMiniView } = props

    const currentTitle = getCurrentTitle(isMiniView, icon, title, imageSrc)

    return (
        <>
            <Icon icon={icon} title={title || ''} src={src} sidebarOpen={sidebarOpen} />
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
