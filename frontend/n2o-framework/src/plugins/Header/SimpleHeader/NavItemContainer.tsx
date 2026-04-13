import React, { useContext } from 'react'
import { useSelector } from 'react-redux'
import { ButtonDropdownProps } from 'reactstrap'
import classNames from 'classnames'

import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { Item, FactoryComponent } from '../../CommonMenuTypes'
import { getModelByPrefixAndNameSelector } from '../../../ducks/models/selectors'
import { ModelPrefix } from '../../../core/models/types'
import { propsResolver } from '../../../core/Expression/propsResolver'

interface NavItemContainerProps {
    itemProps: Item
    active: boolean
    direction?: ButtonDropdownProps['direction']
    className?: string
    level?: number
    iconPosition?: unknown
    nested?: boolean
}

const NavItemContainer = ({
    itemProps,
    className,
    ...props
}: NavItemContainerProps) => {
    const { datasource, model: prefix = ModelPrefix.active } = itemProps
    const model = useSelector(getModelByPrefixAndNameSelector(prefix, datasource))
    const item = propsResolver(itemProps, model, model as Record<string, unknown>, ['items'])

    const { src } = item

    const { getComponent } = useContext(FactoryContext)
    const FactoryComponent: FactoryComponent = getComponent(src, FactoryLevels.HEADER_ITEMS)

    if (!FactoryComponent) {
        return null
    }

    return (
        <FactoryComponent
            item={item}
            from="HEADER"
            className={classNames('nav-item', className)}
            {...props}
        />
    )
}

export default NavItemContainer

NavItemContainer.displayName = 'NavItemContainer'

export { NavItemContainer }
