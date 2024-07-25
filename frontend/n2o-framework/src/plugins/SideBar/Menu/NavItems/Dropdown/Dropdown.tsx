import React from 'react'
import isEmpty from 'lodash/isEmpty'
import map from 'lodash/map'
import classNames from 'classnames'

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

export function Dropdown(props: DropdownProps) {
    const {
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
    } = props
    const dropdownId = generateId()

    if (isEmpty(item)) { return null }

    const { items = [] } = item

    if (!items.length) { return null }

    return (
        <>
            <DropdownWrapper
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
        </>
    )
}
