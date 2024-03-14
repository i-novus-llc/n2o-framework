import React from 'react'
import { ButtonDropdownProps } from 'reactstrap'

import NavItemContainer from '../NavItemContainer'
import { Item as ItemProps, Common, ContextItemCommon } from '../../../CommonMenuTypes'
import { ICON_POSITIONS } from '../../../../components/snippets/IconContainer/IconContainer'

export interface Dropdown extends Common {
    items: ItemProps[]
    nested?: boolean
    direction?: ButtonDropdownProps['direction']
    recursiveClose?: boolean
    onItemClick?(): void
    level?: number
    from?: 'HEADER' | 'SIDEBAR'
    iconPosition?: ICON_POSITIONS
}

export interface DropdownContextItem extends ContextItemCommon {
    item: Dropdown
}

export function Item(props: ItemProps) {
    const { href, id, pathname, datasource, datasources } = props
    const active = href ? pathname.includes(href) : false

    return (
        <NavItemContainer
            // @ts-ignore import from js file WithDataSource
            itemProps={props}
            active={active}
            datasource={datasource}
            id={id}
            datasources={datasources}
            visible
        />
    )
}
