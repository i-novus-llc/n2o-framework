import React from 'react'
import { ButtonDropdownProps } from 'reactstrap'

import NavItemContainer from '../NavItemContainer'
import { IItem, ICommon, IContextItemCommon } from '../../../CommonMenuTypes'

export interface IDropdown extends ICommon {
    items: IItem[]
    nested?: boolean
    direction?: ButtonDropdownProps['direction']
    recursiveClose?: boolean
    onItemClick?(): void
    level?: number
    from?: 'HEADER' | 'SIDEBAR'
}

export interface IDropdownContextItem extends IContextItemCommon {
    item: IDropdown
}

export function Item(props: IItem) {
    const { href, id, pathname, datasource, datasources } = props
    const active = href ? pathname.includes(href) : false

    return (
        <NavItemContainer
            itemProps={props}
            active={active}
            datasource={datasource}
            id={id}
            datasources={datasources}
            visible
        />
    )
}
