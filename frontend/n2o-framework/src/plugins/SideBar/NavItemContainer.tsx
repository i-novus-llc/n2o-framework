import React, { useContext } from 'react'
import classNames from 'classnames'

import { getFromSource, metaPropsType } from '../utils'
import { ITEM_SRC } from '../constants'
import { Item, FactoryComponent } from '../CommonMenuTypes'
import { DataSourceModels } from '../../core/datasource/const'
import { FactoryContext } from '../../core/factory/context'
import { FactoryLevels } from '../../core/factory/factoryLevels'

interface SidebarItemContainer {
    className?: string
    itemProps: Item
    activeId: string
    sidebarOpen: boolean
    showContent: boolean
    isMiniView: boolean
    isStaticView: boolean
    datasources: metaPropsType[]
    datasource: string
    models: DataSourceModels
    level?: number
}

export function NavItemContainer(props: SidebarItemContainer) {
    const {
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
    } = props
    const item = getFromSource(itemProps, datasources, models, datasource)
    const { src } = item

    const { getComponent } = useContext(FactoryContext)
    const FactoryComponent: FactoryComponent = getComponent(src, FactoryLevels.SIDEBAR_ITEM)

    if (!FactoryComponent) {
        return null
    }

    return (
        <li
            className={classNames(
                'n2o-sidebar__item-wrapper',
                className,
                {
                    'n2o-sidebar__item--dropdown': src === ITEM_SRC.DROPDOWN,
                },
            )}
        >
            <FactoryComponent
                from="SIDEBAR"
                item={item}
                className="n2o-sidebar__item"
                activeId={activeId}
                sidebarOpen={sidebarOpen}
                showContent={showContent}
                isMiniView={isMiniView}
                isStaticView={isStaticView}
                datasources={datasources}
                datasource={datasource}
                models={models}
                level={level}
                active={false}
            />
        </li>
    )
}

export default NavItemContainer
