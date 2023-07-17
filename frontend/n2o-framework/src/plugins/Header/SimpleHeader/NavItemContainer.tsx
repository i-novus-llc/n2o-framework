import React, { useContext } from 'react'
import get from 'lodash/get'
import { ButtonDropdownProps } from 'reactstrap'

// @ts-ignore ignore import error from js file
import { WithDataSource } from '../../../core/datasource/WithDataSource'
import { getFromSource, metaPropsType } from '../../utils'
import { IDataSourceModels } from '../../../core/datasource/const'
import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { IItem, IFactoryComponent } from '../../CommonMenuTypes'

interface INavItemContainer {
    itemProps: IItem
    active: boolean
    datasources: metaPropsType[]
    models: IDataSourceModels
    direction: ButtonDropdownProps['direction']
}

const NavItemContainer = (props: INavItemContainer) => {
    const { itemProps, active, datasources, models, direction } = props
    const datasource = get(itemProps, 'datasource')

    const item = getFromSource(itemProps, datasources, models, datasource)
    const { src } = item

    const { getComponent } = useContext(FactoryContext)
    const FactoryComponent: IFactoryComponent = getComponent(src, FactoryLevels.HEADER_ITEMS)

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
