import { ModelPrefix } from '../../core/datasource/const'
import { Crumb } from '../../components/core/Breadcrumb/const'
import { DataSourceState } from '../datasource/DataSource'
import { DefaultModels } from '../models/Models'
import { Action } from '../Action'

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
    toolbar?: Record<string, object[]>
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
    widget?: string
    models?: DefaultModels
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
}

export type State = Record<string, Page>
