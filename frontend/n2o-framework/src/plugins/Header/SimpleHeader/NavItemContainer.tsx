import React, { useContext } from 'react'
import { useSelector } from 'react-redux'
import get from 'lodash/get'
import { ButtonDropdownProps } from 'reactstrap'

import { WithDataSource, type WithDataSourceProps } from '../../../core/datasource/WithDataSource'
import { getFromSource } from '../../utils'
import { type metaPropsType } from '../../CommonMenuTypes'
import { ModelPrefix } from '../../../core/models/types'
import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { Item, FactoryComponent } from '../../CommonMenuTypes'
import { getModelByPrefixAndNameSelector } from '../../../ducks/models/selectors'

interface NavItemContainerProps extends WithDataSourceProps {
    itemProps: Item
    active: boolean
    datasources: metaPropsType[]
    direction?: ButtonDropdownProps['direction']
}

const NavItemContainer = ({ itemProps, active, datasources, direction }: NavItemContainerProps) => {
    const datasource = get(itemProps, 'datasource')
    const model = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.active, datasource))

    const item = getFromSource(itemProps, datasources, model, datasource)
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
