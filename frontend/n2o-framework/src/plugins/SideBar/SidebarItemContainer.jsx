import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import map from 'lodash/map'
import { NavLink } from 'react-router-dom'

import { id as generateId } from '../../utils/id'
import { SimpleTooltip } from '../../components/snippets/Tooltip/SimpleTooltip'

// eslint-disable-next-line import/no-cycle
import SidebarDropdown from './SidebarDropdown'

const ItemType = {
    DROPDOWN: 'dropdown',
    LINK: 'link',
}

const OUTER_LINK_TYPE = 'outer'

/**
 * Рендер иконки
 * @param icon - иконка
 * @param title - текст итема
 * @param type - тип итема
 * @param sidebarOpen - флаг сжатия сайдбара
 * @param subItems
 * @returns {*}
 */
export const renderIcon = (icon, title, type, sidebarOpen, subItems) => {
    let component = <i className={classNames(icon)} />

    if (!sidebarOpen && type === ItemType.DROPDOWN && !subItems) {
        return title
    } if (!sidebarOpen && !icon) {
        component = title.substring(0, 1)
    }

    return <span className="n2o-sidebar__item-content-icon">{component}</span>
}

/**
 * Sidebar Item
 * @param className
 * @param item - объект итема
 * @param activeId - активный элемент
 * @param sidebarOpen - флаг сжатия сайдбара
 * @param showContent
 * @param isMiniView
 * @param isStaticView
 * @param level - уровень вложенности
 * @returns {*}
 * @constructor
 */
export function SidebarItemContainer({
    className,
    item,
    activeId,
    sidebarOpen,
    showContent,
    isMiniView,
    isStaticView,
    level = 1,
}) {
    const { type, linkType, items = [] } = item

    const renderItem = type => (
        <>
            {type === ItemType.LINK && renderLink(item)}
            {type === ItemType.DROPDOWN && renderDropdown()}
        </>
    )

    const renderLink = item => (linkType === OUTER_LINK_TYPE
        ? renderOuterLink(item)
        : renderInnerLink(item))

    const renderCurrentTitle = (isMiniView, icon, title) => {
        if (isMiniView) {
            if (icon) {
                return null
            }

            return title.substring(0, 1)
        }

        return title
    }

    // eslint-disable-next-line react/prop-types
    const renderOuterLink = ({ href, title, icon }) => {
        const id = generateId()

        return (
            <a id={id} className="n2o-sidebar__item" href={href}>
                {icon && renderIcon(icon, title, type, sidebarOpen)}
                <span className={classNames(
                    'n2o-sidebar__item__title',
                    {
                        none: isMiniView && icon,
                    },
                )}
                >
                    {renderCurrentTitle(isMiniView, icon, title)}
                </span>
                {isMiniView && <SimpleTooltip id={id} message={title} placement="right" />}
            </a>
        )
    }
    // eslint-disable-next-line react/prop-types
    const renderInnerLink = ({ href, title, icon }) => {
        const id = generateId()

        return (
            <>
                <NavLink
                    exact
                    to={href}
                    className="n2o-sidebar__item"
                    activeClassName="active"
                    id={id}
                >
                    {icon && renderIcon(icon, title, type, sidebarOpen)}
                    <span
                        className={classNames(
                            'n2o-sidebar__item-title',
                            {
                                visible: isStaticView ? true : showContent,
                            },
                        )}
                    >
                        {renderCurrentTitle(isMiniView, icon, title)}
                    </span>
                </NavLink>
                {isMiniView && <SimpleTooltip id={id} message={title} placement="right" />}
            </>
        )
    }

    const renderDropdown = () => {
        const id = generateId()

        return (
            <>
                <SidebarDropdown
                    {...item}
                    sidebarOpen={sidebarOpen}
                    showContent={showContent}
                    isMiniView={isMiniView}
                    id={id}
                >
                    {map(items, (item, i) => (
                        <div
                            className={classNames(
                                'n2o-sidebar__sub-item',
                                `n2o-sidebar__sub-item--level-${level}`,
                            )}
                        >
                            <SidebarItemContainer
                                level={level + 1}
                                key={i}
                                activeId={activeId}
                                item={item}
                                sidebarOpen={sidebarOpen}
                                showContent={showContent}
                                isMiniView={isMiniView}
                            />
                        </div>
                    ))}
                </SidebarDropdown>
                {isMiniView && <SimpleTooltip id={id} message={item.title} placement="right" />}
            </>
        )
    }

    return (
        <div
            className={classNames(className, {
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
    level: PropTypes.number,
    className: PropTypes.string,
    sidebarOpen: PropTypes.bool,
    showContent: PropTypes.bool,
    isMiniView: PropTypes.bool,
    isStaticView: PropTypes.bool,
}

export default SidebarItemContainer
