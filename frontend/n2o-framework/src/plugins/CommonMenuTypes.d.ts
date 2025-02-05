import React from 'react'
import { ButtonDropdownProps } from 'reactstrap'

import { Action } from '../ducks/Action'
import { Props } from '../components/snippets/Badge/Badge'
import { DataSourceModels } from '../core/datasource/const'

import { metaPropsType } from './utils'

export type ActionMenuItemSrcType = 'ActionMenuItem'
export type DropdownMenuItemSrcType = 'DropdownMenuItem'
export type LinkMenuItemSrcType = 'LinkMenuItem'
export type StaticMenuItemScrType = 'StaticMenuItem'

export type SrcTypes = ActionMenuItemSrcType | DropdownMenuItemSrcType | LinkMenuItemSrcType | StaticMenuItemScrType

export interface Common {
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
export interface Item extends Common {
    id: string
    src: SrcTypes
    href: string
    datasource: string
    datasources: metaPropsType[]
    target: string
    style?: React.CSSProperties
    pathMapping?: metaPropsType[]
    queryMapping?: metaPropsType[]
    action?: Action
    items: Item[]
    pathname: string
    badge?: Props
    linkType: 'outer' | 'inner'
    direction?: string
    activeId?: string
}

export type FactoryComponent = React.FunctionComponent<ContextItem> | void

export interface ContextItemCommon {
    active: boolean
    activeId?: string
    from: 'HEADER' | 'SIDEBAR'
    className: string
    style?: React.CSSProperties
    direction?: ButtonDropdownProps['direction']
    isStaticView?: boolean
    sidebarOpen?: boolean
    showContent?: boolean
    isMiniView?: boolean
    datasource?: string
    datasources?: metaPropsType[]
    models?: DataSourceModels
    level?: number
}
export interface ContextItem extends ContextItemCommon {
    item: Item
}
