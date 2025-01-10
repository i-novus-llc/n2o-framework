import React from 'react'
import classNames from 'classnames'

import { ITEM_SRC } from '../constants'
import { parseExpression } from '../../core/Expression/parse'

import { SIDEBAR_VIEW, SidebarProps } from './types'

export const getCurrentTitle = (isMiniView?: boolean, icon?: string, title?: string, imageSrc?: string) => {
    if (!title) {
        return null
    }

    if (isMiniView) {
        if (icon || imageSrc) {
            return null
        }

        return title.substring(0, 1)
    }

    return title
}

/**
 * Рендер иконки
 * @param icon - иконка
 * @param title - текст итема
 * @param type - тип итема
 * @param sidebarOpen - флаг сжатия сайдбара
 * @returns {*}
 */

interface IconProps {
    icon?: string
    title: string
    src: string
    sidebarOpen?: boolean
    hasSubItems?: boolean
}
export const Icon = ({ icon, title, src, sidebarOpen, hasSubItems }: IconProps): JSX.Element | null => {
    if (!icon) {
        return null
    }

    if (!sidebarOpen && src === ITEM_SRC.DROPDOWN && !hasSubItems) {
        return <>title</>
    }

    if (!sidebarOpen && !icon) {
        const reducedTitle = title.substring(0, 1)

        return <span className="n2o-sidebar__item-content-icon">{reducedTitle}</span>
    }

    return (
        <span className="n2o-sidebar__item-content-icon">
            <i className={classNames(icon)} />
        </span>
    )
}

interface TitleProps {
    title: string | null
    className: string
}

export const Title = ({ title, className }: TitleProps) => {
    if (!title) {
        return null
    }

    return <span className={className}>{title}</span>
}

export const needRender = (text?: string | null) => text && !parseExpression(text)

export const toggleIconClassNames = (visible: SidebarProps['visible'], side: SidebarProps['side']) => {
    const isLeftIcon = (visible && side === 'left') || (!visible && (side === 'right'))

    return isLeftIcon ? 'fa fa-angle-double-left' : 'fa fa-angle-double-right'
}

export const sideBarClasses = (
    isStaticView: boolean,
    defaultState: SidebarProps['defaultState'],
    toggledState: SidebarProps['toggledState'],
    currentVisible: boolean,
    side: SidebarProps['side'],
    className?: string,
) => {
    const viewMode = isStaticView || !currentVisible ? SIDEBAR_VIEW[defaultState] : SIDEBAR_VIEW[toggledState]

    return classNames(
        'n2o-sidebar',
        side,
        className,
        viewMode,
    )
}
