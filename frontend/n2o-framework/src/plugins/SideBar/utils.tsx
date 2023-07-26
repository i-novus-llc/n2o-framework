import React from 'react'
import classNames from 'classnames'

import { ITEM_SRC } from '../constants'

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

interface IIcon {
    icon?: string
    title: string
    src: string
    sidebarOpen?: boolean
    hasSubItems?: boolean
}
export const Icon = ({ icon, title, src, sidebarOpen, hasSubItems }: IIcon): JSX.Element | null => {
    if (!icon) {
        return null
    }

    if (!sidebarOpen && src === ITEM_SRC.DROPDOWN && !hasSubItems) {
        return <>title</>
    } if (!sidebarOpen && !icon) {
        const reducedTitle = title.substring(0, 1)

        return <span className="n2o-sidebar__item-content-icon">{reducedTitle}</span>
    }

    return (
        <span className="n2o-sidebar__item-content-icon">
            <i className={classNames(icon)} />
        </span>
    )
}

interface ITitle {
    title: string | null
    className: string
}

export const Title = ({ title, className }: ITitle) => {
    if (!title) {
        return null
    }

    return <span className={className}>{title}</span>
}
