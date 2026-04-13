import React, { useContext } from 'react'
import { useSelector } from 'react-redux'
import classNames from 'classnames'

import { type Item, type FactoryComponent } from '../CommonMenuTypes'
import { ITEM_SRC } from '../constants'
import { FactoryContext } from '../../core/factory/context'
import { FactoryLevels } from '../../core/factory/factoryLevels'
import { parseExpression } from '../../core/Expression/parse'
import { ModelPrefix } from '../../core/models/types'
import { propsResolver } from '../../core/Expression/propsResolver'
import { getModelByPrefixAndNameSelector } from '../../ducks/models/selectors'

export interface SidebarItemContainer {
    className?: string
    itemProps: Item
    activeId: string
    sidebarOpen: boolean
    showContent: boolean
    isMiniView: boolean
    isStaticView: boolean
    level?: number
}

export function NavItemContainer({
    className,
    itemProps,
    activeId,
    sidebarOpen,
    showContent,
    isMiniView,
    isStaticView,
    level = 1,
}: SidebarItemContainer) {
    const { datasource, model: prefix = ModelPrefix.active } = itemProps
    const model = useSelector(getModelByPrefixAndNameSelector(prefix, datasource))
    const item = propsResolver(itemProps, model, model as Record<string, unknown>, ['items'])
    const { src, className: itemClassName, style } = item

    const { getComponent } = useContext(FactoryContext)
    const FactoryComponent: FactoryComponent = getComponent(src, FactoryLevels.SIDEBAR_ITEM)

    if (!FactoryComponent) { return null }

    const { title } = item

    if (parseExpression(title)) { return null }

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
                className={classNames('n2o-sidebar__item', itemClassName)}
                style={style}
                activeId={activeId}
                sidebarOpen={sidebarOpen}
                showContent={showContent}
                isMiniView={isMiniView}
                isStaticView={isStaticView}
                level={level}
                active={false}
            />
        </li>
    )
}

NavItemContainer.displayName = 'NavItemContainer'

export default NavItemContainer
