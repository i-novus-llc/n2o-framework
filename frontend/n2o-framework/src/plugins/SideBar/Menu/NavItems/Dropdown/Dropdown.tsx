import React from 'react'
import isEmpty from 'lodash/isEmpty'
import map from 'lodash/map'
import classNames from 'classnames'
import { useLocation } from 'react-router-dom'

import { id as generateId } from '../../../../../utils/id'
import { NavItemContainer } from '../../../NavItemContainer'
import { Item } from '../../../../CommonMenuTypes'
import { metaPropsType } from '../../../../utils'
import { DataSourceModels } from '../../../../../core/datasource/const'

import { DropdownWrapper } from './DropdownWrapper'

interface DropdownProps {
    item: Item
    sidebarOpen: boolean
    showContent: boolean
    isMiniView: boolean
    datasources: metaPropsType[]
    datasource: string
    models: DataSourceModels
    activeId: string
    level: number
    isStaticView: boolean
}

function hasActiveSubItem(items: Item['items'], location: { pathname: string }): boolean {
    return items.some((item) => {
        if (item.href === location.pathname) { return true }
        if (item.items?.length) { return hasActiveSubItem(item.items, location) }

        return false
    })
}

export function Dropdown({
    item,
    sidebarOpen,
    showContent,
    isMiniView,
    datasources,
    datasource,
    models,
    activeId,
    level,
    isStaticView,
}: DropdownProps) {
    const dropdownId = generateId()
    const location = useLocation()

    if (isEmpty(item)) { return null }

    const { items = [] } = item

    if (!items.length) { return null }

    const isOpen = hasActiveSubItem(items, location)

    return (
        <DropdownWrapper
            {...item}
            sidebarOpen={sidebarOpen}
            showContent={showContent}
            isMiniView={isMiniView}
            id={dropdownId}
            open={isOpen}
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
                        <NavItemContainer
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
                            isStaticView={isStaticView}
                        />
                    </div>
                ))}
            </div>
        </DropdownWrapper>
    )
}
