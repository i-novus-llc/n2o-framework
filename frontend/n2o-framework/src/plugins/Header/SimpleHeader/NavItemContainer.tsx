import React, { useContext } from 'react'
import { useSelector } from 'react-redux'
import { ButtonDropdownProps } from 'reactstrap'
import classNames from 'classnames'

import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { type Item, type FactoryComponent } from '../../CommonMenuTypes'
import { getModelByPrefixAndNameSelector } from '../../../ducks/models/selectors'
import { ModelPrefix } from '../../../core/models/types'
import { useResolved } from '../../../core/Expression/useResolver'

interface NavItemContainerProps {
    itemProps: Item
    active: boolean
    direction?: ButtonDropdownProps['direction']
    className?: string
    level?: number
    iconPosition?: unknown
    nested?: boolean
}

const hasVisibleChildrenRecursive = (items: Item['items']) => {
    for (const item of items) {
        const { visible = true } = item

        if (visible) { return true }

        if (item.items && hasVisibleChildrenRecursive(item.items)) {
            return true
        }
    }

    return false
}

const NavItemContainer = ({ itemProps, className, ...rest }: NavItemContainerProps) => {
    const { datasource, model: prefix = ModelPrefix.active } = itemProps
    const model = useSelector(getModelByPrefixAndNameSelector(prefix, datasource))
    const item = useResolved(itemProps, model, ['items'])

    const { src, visible, items } = item

    const hasVisibleChildren = items ? hasVisibleChildrenRecursive(items) : true

    const { getComponent } = useContext(FactoryContext)
    const FactoryComponent: FactoryComponent = getComponent(src, FactoryLevels.HEADER_ITEMS)

    if (!FactoryComponent || !visible || !hasVisibleChildren) { return null }

    return (
        <FactoryComponent
            item={item}
            from="HEADER"
            className={classNames('nav-item', className)}
            {...rest}
        />
    )
}

export default NavItemContainer

NavItemContainer.displayName = 'NavItemContainer'

export { NavItemContainer }
