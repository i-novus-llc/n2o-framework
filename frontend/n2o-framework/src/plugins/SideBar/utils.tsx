import React from 'react'
import classNames from 'classnames'

import { ITEM_SRC } from '../constants'
import { parseExpression } from '../../core/Expression/parse'

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

export const needRender = (text?: string | null) => text && !parseExpression(text)
