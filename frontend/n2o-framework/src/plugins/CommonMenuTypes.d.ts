import React, { MouseEventHandler } from 'react'
import { ButtonDropdownProps } from 'reactstrap'
import { ImageShape } from '@i-novus/n2o-components/lib/display/NavItemImage'

import { Action } from '../ducks/Action'
import { Props } from '../components/snippets/Badge/Badge'
import { LinkTarget } from '../components/core/router/types'
import { DataSourceModels } from '../core/datasource/const'

export type ActionMenuItemSrcType = 'ActionMenuItem'
export type DropdownMenuItemSrcType = 'DropdownMenuItem'
export type LinkMenuItemSrcType = 'LinkMenuItem'
export type StaticMenuItemScrType = 'StaticMenuItem'

export type SrcTypes = ActionMenuItemSrcType | DropdownMenuItemSrcType | LinkMenuItemSrcType | StaticMenuItemScrType

export type metaPropsType = { [key: string]: unknown }

export interface Common {
    title?: string
    className?: string
    icon?: string
    imageSrc?: string
    imageShape?: ImageShape
    active: boolean
    isStaticView?: boolean
    sidebarOpen?: boolean
    showContent?: boolean
    isMiniView?: boolean
    disabled?: boolean
}
export interface Item extends Common {
    id: string
    src: SrcTypes
    href: string
    datasource: string
    datasources: metaPropsType[]
    target: LinkTarget
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
    onClick?: MouseEventHandler
}
