import React from 'react'
import PropTypes from 'prop-types'
import cn from 'classnames'
import map from 'lodash/map'
import { NavLink } from 'react-router-dom'

import SidebarDropdown from './SidebarDropdown'

const ItemType = {
    DROPDOWN: 'dropdown',
    LINK: 'link',
}

const OUTER_LINK_TYPE = 'outer'

/**
 * Рендер иконки
 * @param icon - иконка
 * @param label - текст итема
 * @param type - тип итема
 * @param sidebarOpen - флаг сжатия сайдбара
 * @param subItems
 * @returns {*}
 */
export const renderIcon = (icon, label, type, sidebarOpen, subItems) => {
    let component = <i className={cn(icon)} />

    if (!sidebarOpen && type === ItemType.DROPDOWN && !subItems) {
        return label
    } if (!sidebarOpen && !icon) {
        component = label.substring(0, 1)
    }

    return <span className="n2o-sidebar__item-content-icon">{component}</span>
}

/**
 * Sidebar Item
 * @param className
 * @param item - объект итема
 * @param activeId - активный элемент
 * @param sidebarOpen - флаг сжатия сайдбара
 * @param level - уровень вложенности
 * @returns {*}
 * @constructor
 */
function SidebarItemContainer({
    className,
    item,
    activeId,
    sidebarOpen,
    level = 1,
}) {
    const { type, linkType, subItems } = item

    const renderItem = type => (
        <>
            {type === ItemType.LINK && renderLink(item)}
            {type === ItemType.DROPDOWN && renderDropdown()}
        </>
    )
    const renderLink = item => (linkType === OUTER_LINK_TYPE
        ? renderOuterLink(item)
        : renderInnerLink(item))
    const renderOuterLink = ({ href, label, iconClass }) => (
        <a className="n2o-sidebar__item" href={href}>
            {renderIcon(iconClass, label, type, sidebarOpen)}
            {label}
        </a>
    )
    const renderInnerLink = ({ href, label, iconClass }) => (
        <NavLink
            exact
            to={href}
            className="n2o-sidebar__item"
            activeClassName="active"
        >
            {renderIcon(iconClass, label, type, sidebarOpen)}
            {sidebarOpen && <span>{label}</span>}
        </NavLink>
    )

    const renderDropdown = () => (
        <SidebarDropdown {...item} sidebarOpen={sidebarOpen}>
            {map(subItems, (subItem, i) => (
                <div
                    className={cn(
                        'n2o-sidebar__sub-item',
                        `n2o-sidebar__sub-item--level-${level}`,
                    )}
                >
                    <SidebarItemContainer
                        level={level + 1}
                        key={i}
                        activeId={activeId}
                        item={subItem}
                        sidebarOpen={sidebarOpen}
                    />
                </div>
            ))}
        </SidebarDropdown>
    )

    return (
        <div
            className={cn(className, {
                'n2o-sidebar__item--dropdown': type === ItemType.DROPDOWN,
            })}
        >
            {renderItem(type)}
        </div>
    )
}
SidebarItemContainer.propTypes = {
    item: PropTypes.object,
    activeId: PropTypes.string,
    sidebarOpen: PropTypes.bool,
}

export default SidebarItemContainer
