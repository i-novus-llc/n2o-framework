import { CSSProperties } from 'react'

import { ModelPrefix } from '../../core/datasource/const'
import { Crumb } from '../../components/core/Breadcrumb/const'
import { DataSourceState } from '../datasource/DataSource'
import { DefaultModels } from '../models/Models'
import { Action } from '../Action'
import { Places } from '../../components/pages/types'
import { ToolbarProps } from '../../components/buttons/Toolbar'

import { Routes } from './sagas/types'

export interface Events {
    datasource: string
    model: ModelPrefix
    field: string
    action: Action
}

export interface Metadata {
    src?: string
    id?: string
    routes?: Routes
    /* FIXME */
    searchBar?: {
        className: string
        datasource: string
        fieldId: string
        placeholder: string
        throttleDelay: number
        trigger: 'CHANGE' | 'CLICK'
    }
    toolbar?: {
        topLeft?: ToolbarProps
        topCenter?: ToolbarProps
        topRight?: ToolbarProps
        bottomLeft?: ToolbarProps
        bottomCenter?: ToolbarProps
        bottomRight?: ToolbarProps
    }
    breadcrumb?: Crumb[]
    datasources?: Record<string, DataSourceState>
    /* FIXME */
    regions?: Record<string, object[]>
    width?: { [key: string]: string }
    page?: {
        datasource: string
        model: ModelPrefix
        htmlTitle: string
        title: string
    }
    events?: Events[]
    widget?: Record<string, unknown>
    models?: DefaultModels
    style?: CSSProperties
    className?: string
    places?: Places
    needScrollButton?: boolean
}

export type Location = {
    pathname: string,
    hash: '',
    search: string,
    state: undefined
}

export type Page = {
    id: string
    loading: boolean
    error: object | boolean
    disabled: boolean
    metadata: Metadata
    spinner?: boolean
    pageUrl: string
    parentId?: string
    rootPage?: boolean
    location?: Location
    scroll: boolean
}

export type State = Record<string, Page>
