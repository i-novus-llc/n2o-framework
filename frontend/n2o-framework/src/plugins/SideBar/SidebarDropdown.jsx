import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import { compose, withState, withHandlers } from 'recompose'

import { NavItemImage } from '../../components/snippets/NavItemImage/NavItemImage'

// eslint-disable-next-line import/no-cycle
import { renderIcon } from './SidebarItemContainer'

/**
 * Sidebar Dropdown Item
 * @param sidebarOpen - флаг сжатия сайдбара
 * @param title - текст дропдауна
 * @param children - subItems
 * @param isOpen - флаг видимости subItems
 * @param toggle - переключене видимости
 * @param iconClass - класс
 * @param type - тип
 * @param id - id
 * @returns {*}
 * @constructor
 */
function SidebarDropdown({
    sidebarOpen,
    title,
    children,
    isOpen,
    toggle,
    icon,
    type,
    showContent,
    isMiniView,
    id,
    imageSrc,
    imageShape,
}) {
    const itemDropdownClass = classNames(
        'n2o-sidebar__item-dropdown-label',
        {
            'pl-3': !isMiniView,
        },
    )

    const subItemsClass = classNames(
        'n2o-sidebar__subitems',
        {
            visible: showContent,
        },
    )

    return (
        <div className="n2o-sidebar__item-dropdown">
            <div
                onClick={toggle}
                className={itemDropdownClass}
                id={id}
            >
                {icon && renderIcon(icon, title, type, sidebarOpen, true)}
                {imageSrc && <NavItemImage imageSrc={imageSrc} title={title} imageShape={imageShape} />}
                <span className={classNames('n2o-sidebar__item-title', { mini: isMiniView, visible: showContent })}>{title}</span>
            </div>
            {isOpen && (<div className={subItemsClass}>{children}</div>
            )}
        </div>
    )
}

SidebarDropdown.propTypes = {
    sidebarOpen: PropTypes.bool,
    title: PropTypes.string,
    children: PropTypes.node,
    isOpen: PropTypes.bool,
    showContent: PropTypes.bool,
    isMiniView: PropTypes.bool,
    toggle: PropTypes.func,
    icon: PropTypes.string,
    type: PropTypes.string,
    id: PropTypes.string,
    imageSrc: PropTypes.string,
    imageShape: PropTypes.string,
}

export default compose(
    withState('isOpen', 'setOpen', false),
    withHandlers({
        toggle: ({ isOpen, setOpen }) => () => setOpen(!isOpen),
    }),
)(SidebarDropdown)
