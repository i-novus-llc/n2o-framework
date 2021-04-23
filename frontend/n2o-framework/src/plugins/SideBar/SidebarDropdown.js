import React from 'react'
import PropTypes from 'prop-types'
import cn from 'classnames'
import { compose, withState, withHandlers } from 'recompose'

import { renderIcon } from './SidebarItemContainer'

/**
 * Sidebar Dropdown Item
 * @param sidebarOpen - флаг сжатия сайдбара
 * @param label - текст дропдауна
 * @param children - subItems
 * @param isOpen - флаг видимости subItems
 * @param toggle - переключене видимости
 * @param iconClass - класс
 * @param type - тип
 * @returns {*}
 * @constructor
 */
function SidebarDropdown({
    sidebarOpen,
    label,
    children,
    isOpen,
    toggle,
    iconClass,
    type,
}) {
    return (
        <div
            onMouseOver={!sidebarOpen && !isOpen && toggle}
            onMouseLeave={!sidebarOpen && isOpen && toggle}
            className="n2o-sidebar__item-dropdown"
        >
            <div
                onClick={sidebarOpen && toggle}
                className={cn('n2o-sidebar__item-dropdown-label', {
                    'n2o-sidebar__item-dropdown-label--up': isOpen,
                })}
            >
                {renderIcon(iconClass, label, type, sidebarOpen, true)}
                {sidebarOpen && <span>{label}</span>}
            </div>
            {isOpen && <div className="n2o-sidebar__subitems">{children}</div>}
        </div>
    )
}

SidebarDropdown.propTypes = {
    sidebarOpen: PropTypes.bool,
    label: PropTypes.string,
    children: PropTypes.node,
    isOpen: PropTypes.bool,
    toggle: PropTypes.func,
    iconClass: PropTypes.string,
    type: PropTypes.string,
}

export default compose(
    withState('isOpen', 'setOpen', false),
    withHandlers({
        toggle: ({ isOpen, setOpen }) => () => setOpen(!isOpen),
    }),
)(SidebarDropdown)
