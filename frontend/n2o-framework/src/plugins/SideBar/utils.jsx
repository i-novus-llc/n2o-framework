import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import { ITEM_SRC } from '../constants'

export const getCurrentTitle = (isMiniView, icon, title, imageSrc) => {
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
export const Icon = ({ icon, title, src, sidebarOpen, hasSubItems }) => {
    if (!icon) {
        return null
    }

    let component = <i className={classNames(icon)} />

    if (!sidebarOpen && src === ITEM_SRC.DROPDOWN && !hasSubItems) {
        return title
    } if (!sidebarOpen && !icon) {
        component = title.substring(0, 1)
    }

    return <span className="n2o-sidebar__item-content-icon">{component}</span>
}

Icon.propTypes = {
    icon: PropTypes.string,
    title: PropTypes.string,
    src: PropTypes.string,
    sidebarOpen: PropTypes.bool,
    hasSubItems: PropTypes.bool,
}

export const Title = ({ title, className }) => {
    if (!title) {
        return null
    }

    return <span className={className}>{title}</span>
}

Title.propTypes = {
    title: PropTypes.string,
    className: PropTypes.string,
}
