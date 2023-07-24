import React from 'react'
import classNames from 'classnames'
import { Action as ReduxAction } from 'redux'

import { NavItemImage } from '../../components/snippets/NavItemImage/NavItemImage'
import { Badge, Props } from '../../components/snippets/Badge/Badge'

// @ts-ignore ignore import error from js file
import { Icon, Title, getCurrentTitle } from './utils'

export interface IItemContent{
    id: string
    title: string
    className?: string
    icon?: string
    type: string
    sidebarOpen?: boolean
    imageSrc?: string
    imageShape?: string
    badge?: Props
    isStaticView?: boolean
    showContent?: boolean
    isMiniView?: boolean
    action?: ReduxAction
}

export function ItemContent(props: IItemContent) {
    const { icon, title, type, sidebarOpen, imageSrc, imageShape, badge, isStaticView, showContent, isMiniView } = props

    const currentTitle = getCurrentTitle(isMiniView, icon, title, imageSrc)

    return (
        <>
            <Icon icon={icon} title={title} type={type} sidebarOpen={sidebarOpen} />
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
