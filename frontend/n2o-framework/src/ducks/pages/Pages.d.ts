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
    width?: {[key: string]: string}
    page?: { model: string, htmlTitle: string, title: string }
    events?: Events[]
    widget?: string
    models?: DefaultModels
}

export interface Page {
    loading: boolean
    error: object | boolean
    disabled: boolean
    status: number | null
    metadata: Metadata
    spinner?: boolean
}

export type State = Record<string, Page>
