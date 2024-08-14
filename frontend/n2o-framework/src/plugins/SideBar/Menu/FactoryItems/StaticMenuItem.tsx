import React from 'react'
import classNames from 'classnames'

import { LinkBody } from '../NavItems/Links/LinkBody'
import { Item } from '../../../CommonMenuTypes'

interface StaticMenuItemProps {
    item: Item
    sidebarOpen: boolean
    isStaticView: boolean
    showContent: boolean
    isMiniView: boolean
    className: string
}

export function StaticMenuItem({ item, className, sidebarOpen, isStaticView, showContent, isMiniView }: StaticMenuItemProps) {
    return (
        <li className={classNames(className, 'static-menu-item')}>
            <LinkBody
                {...item}
                sidebarOpen={sidebarOpen}
                isStaticView={isStaticView}
                showContent={showContent}
                isMiniView={isMiniView}
            />
        </li>
    )
}
