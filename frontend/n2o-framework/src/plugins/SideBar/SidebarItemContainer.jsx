import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import map from 'lodash/map'

import { getFromSource } from '../Header/SimpleHeader/NavItemContainer'
import { id as generateId } from '../../utils/id'
import { WithDataSource } from '../../core/datasource/WithDataSource'

import SidebarDropdown from './SidebarDropdown'
import { ItemType } from './utils'
import { OuterLink } from './OuterLink'
import { InnerLink } from './InnerLink'

const OUTER_LINK_TYPE = 'outer'

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
    level = 1,
    datasources,
    datasource,
    models,
}) {
    const item = getFromSource(itemProps, datasources, datasource, models)
    const { type, linkType, items = [] } = item

    const renderLink = item => (linkType === OUTER_LINK_TYPE
        ? (
            <OuterLink
                sidebarOpen={sidebarOpen}
                isMiniView={isMiniView}
                item={item}
                {...item}
            />
        )
        : (
            <InnerLink
                sidebarOpen={sidebarOpen}
                isMiniView={isMiniView}
                item={item}
                isStaticView={isStaticView}
                showContent={showContent}
                {...item}
            />
        ))

    const renderDropdown = () => {
        const dropdownId = generateId()

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
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }

    return (
        <div
            className={classNames(className, {
                'n2o-sidebar__item--dropdown': type === ItemType.DROPDOWN,
            })}
        >
            {type === ItemType.LINK ? renderLink(item) : renderDropdown()}
        </div>
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

export default WithDataSource(SidebarItemContainer)
