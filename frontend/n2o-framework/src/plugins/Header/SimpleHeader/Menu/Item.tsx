import React from 'react'
import { ButtonDropdownProps } from 'reactstrap'

import { metaPropsType } from '../../../utils'
import NavItemContainer from '../NavItemContainer'
import { Action } from '../../../../ducks/Action'
import { IBadgeProps } from '../../../../components/snippets/Badge/Badge'
import { SrcTypes } from '../../../constants'

export type IContextComponent = React.FunctionComponent<IContextItem> | void

interface ICommon {
    title?: string
    className?: string
    icon?: string
    imageSrc?: string
    imageShape?: string
    active: boolean
    isStaticView?: boolean
    sidebarOpen?: boolean
    showContent?: boolean
    isMiniView?: boolean
}

export interface IContextItemCommon {
    active: boolean
    from: 'HEADER' | 'SIDEBAR'
    className: string
    direction?: ButtonDropdownProps['direction']
}

export interface IItem extends ICommon {
    id: string
    src: SrcTypes
    href: string
    datasource?: string
    datasources?: metaPropsType[]
    target: string
    style: React.CSSProperties
    pathMapping?: metaPropsType[]
    queryMapping?: metaPropsType[]
    action?: Action
    items: IItem[]
    pathname: string
    badge?: IBadgeProps
    linkType: 'outer' | 'inner'
    direction?: string
}

export interface IDropdown extends ICommon {
    items: IItem[]
    nested?: boolean
    direction?: ButtonDropdownProps['direction']
    recursiveClose?: boolean
    onItemClick?(): void
    level?: number
    from?: 'HEADER' | 'SIDEBAR'
}

export interface IContextItem extends IContextItemCommon {
    item: IItem
}

export interface IDropdownContextItem extends IContextItemCommon{
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
