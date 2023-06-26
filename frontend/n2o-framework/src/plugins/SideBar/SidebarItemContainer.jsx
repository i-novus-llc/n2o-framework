import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import map from 'lodash/map'
import isEmpty from 'lodash/isEmpty'

import { getFromSource } from '../utils'
import { id as generateId } from '../../utils/id'
import { Action } from '../Action/Action'
import { OUTER_LINK_TYPE, ITEM_TYPE } from '../constants'

import SidebarDropdown from './SidebarDropdown'
import { OuterLink } from './OuterLink'
import { InnerLink } from './InnerLink'

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
    itemProps,
    activeId,
    sidebarOpen,
    showContent,
    isMiniView,
    isStaticView,
    datasources,
    datasource,
    models,
    level = 1,
}) {
    const item = getFromSource(itemProps, datasources, datasource, models)
    const { type, linkType, items = [] } = item

    const renderAction = item => (
        <Action
            item={item}
            className="n2o-sidebar__item"
            sidebarOpen={sidebarOpen}
            showContent={showContent}
            isStaticView={isStaticView}
            isMiniView={isMiniView}
            from="SIDEBAR"
        />
    )

    const renderLink = (item) => {
        if (linkType === OUTER_LINK_TYPE) {
            return (
                <OuterLink
                    sidebarOpen={sidebarOpen}
                    isMiniView={isMiniView}
                    item={item}
                    isStaticView={isStaticView}
                    showContent={showContent}
                    {...item}
                />
            )
        }

        return (
            <InnerLink
                sidebarOpen={sidebarOpen}
                isMiniView={isMiniView}
                item={item}
                isStaticView={isStaticView}
                showContent={showContent}
                {...item}
            />
        )
    }

    const renderDropdown = () => {
        const dropdownId = generateId()

        if (isEmpty(item)) {
            return null
        }

        return (
            <>
                <SidebarDropdown
                    {...item}
                    sidebarOpen={sidebarOpen}
                    showContent={showContent}
                    isMiniView={isMiniView}
                    id={dropdownId}
                >
                    <div
                        className={classNames(
                            'n2o-sidebar__sub-items-container',
                            {
                                mini: isMiniView,
                            },
                        )
                        }
                    >
                        {map(items, (item, i) => (
                            <div
                                key={i}
                                className={classNames(
                                    'n2o-sidebar__sub-item',
                                    `n2o-sidebar__sub-item--level-${level}`,
                                )}
                            >
                                <SidebarItemContainer
                                    level={level + 1}
                                    key={i}
                                    activeId={activeId}
                                    itemProps={item}
                                    sidebarOpen={sidebarOpen}
                                    showContent={showContent}
                                    isMiniView={isMiniView}
                                    datasources={datasources}
                                    datasource={datasource}
                                    models={models}
                                />
                            </div>
                        ))}
                    </div>
                </SidebarDropdown>
            </>
        )
    }

    const renderItem = (type) => {
        if (type === ITEM_TYPE.ACTION) {
            return renderAction(item)
        }

        if (type === ITEM_TYPE.LINK) {
            return renderLink(item)
        }

        return renderDropdown()
    }

    return (
        <li
            className={classNames(
                'n2o-sidebar__item-wrapper',
                className,
                {
                    'n2o-sidebar__item--dropdown': type === ITEM_TYPE.DROPDOWN,
                },
            )}
        >
            {renderItem(type)}
        </li>
    )
}
SidebarItemContainer.propTypes = {
    itemProps: PropTypes.object,
    activeId: PropTypes.string,
    level: PropTypes.number,
    className: PropTypes.string,
    sidebarOpen: PropTypes.bool,
    showContent: PropTypes.bool,
    isMiniView: PropTypes.bool,
    isStaticView: PropTypes.bool,
    datasources: PropTypes.object,
    datasource: PropTypes.string,
    models: PropTypes.object,
}

export default SidebarItemContainer
