import React from 'react'
import classNames from 'classnames'

import { LinkBody } from '../NavItems/Links/LinkBody'
import { IItem } from '../../../CommonMenuTypes'

interface IStaticMenuItem {
    item: IItem
    sidebarOpen: boolean
    isStaticView: boolean
    showContent: boolean
    isMiniView: boolean
    className: string
}

export function StaticMenuItem(props: IStaticMenuItem) {
    const { item, className, sidebarOpen, isStaticView, showContent, isMiniView } = props

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
