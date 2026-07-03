import React, { useContext } from 'react'
import { useSelector } from 'react-redux'
import { ButtonDropdownProps } from 'reactstrap'
import classNames from 'classnames'

import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { ContextItem, Item } from '../../CommonMenuTypes'
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

const hasVisibleChildrenRecursive = (items: Item['items']): boolean => items?.some(item => (item.items
    ? hasVisibleChildrenRecursive(item.items)
    : (item.visible ?? true))) ?? false

const NavItemContainer = ({ itemProps, className, ...rest }: NavItemContainerProps) => {
    const { datasource, model: prefix = ModelPrefix.active } = itemProps
    const model = useSelector(getModelByPrefixAndNameSelector(prefix, datasource))
    const item = useResolved(itemProps, model, ['items'])

    const { src, visible, items } = item

    const hasVisibleChildren = items ? hasVisibleChildrenRecursive(items) : true

    const { getComponent } = useContext(FactoryContext)
    const FactoryComponent = getComponent<ContextItem>(src, FactoryLevels.HEADER_ITEMS)

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
