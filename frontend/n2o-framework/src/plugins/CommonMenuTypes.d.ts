import React from 'react'
import { ButtonDropdownProps } from 'reactstrap'

import { Action } from '../ducks/Action'
import { IBadgeProps } from '../components/snippets/Badge/Badge'
import { IDataSourceModels } from '../core/datasource/const'

import { metaPropsType } from './utils'

export type ActionMenuItemSrcType = 'ActionMenuItem'
export type DropdownMenuItemSrcType = 'DropdownMenuItem'
export type LinkMenuItemSrcType = 'LinkMenuItem'
export type StaticMenuItemScrType = 'StaticMenuItem'

export type SrcTypes = ActionMenuItemSrcType | DropdownMenuItemSrcType | LinkMenuItemSrcType | StaticMenuItemScrType

export interface ICommon {
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
    activeId?: string
}

export type IFactoryComponent = React.FunctionComponent<IContextItem> | void

export interface IContextItemCommon {
    active: boolean
    activeId?: string
    from: 'HEADER' | 'SIDEBAR'
    className: string
    direction?: ButtonDropdownProps['direction']
    isStaticView?: boolean
    sidebarOpen?: boolean
    showContent?: boolean
    isMiniView?: boolean
    datasource?: string
    datasources?: metaPropsType[]
    models?: IDataSourceModels
    level?: number
}
export interface IContextItem extends IContextItemCommon {
    item: IItem
}
