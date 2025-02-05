import React, { useContext } from 'react'
import get from 'lodash/get'
import { ButtonDropdownProps } from 'reactstrap'

import { WithDataSource, type WithDataSourceProps } from '../../../core/datasource/WithDataSource'
import { getFromSource, metaPropsType } from '../../utils'
import { DataSourceModels } from '../../../core/datasource/const'
import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { Item, FactoryComponent } from '../../CommonMenuTypes'

interface NavItemContainerProps extends WithDataSourceProps {
    itemProps: Item
    active: boolean
    datasources: metaPropsType[]
    models?: DataSourceModels
    direction?: ButtonDropdownProps['direction']
    visible?: boolean
}

const NavItemContainer = ({ itemProps, active, datasources, models, direction, visible }: NavItemContainerProps) => {
    const datasource = get(itemProps, 'datasource')

    const item = getFromSource(itemProps, datasources, models, datasource)
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
            className="nav-item"
            direction={direction}
            active={active}
        />
    )
}

export default WithDataSource(NavItemContainer)

export { NavItemContainer }
