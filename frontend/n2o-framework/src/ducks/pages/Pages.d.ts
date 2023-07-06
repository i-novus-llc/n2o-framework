import { Action } from 'redux'

import { ICrumb } from '../../components/core/Breadcrumb/const'
import { DataSourceState } from '../datasource/DataSource'
import { DefaultModels } from '../models/Models'

import { IRoutes } from './sagas/types'

export interface IEvents {
    datasource: string
    model: string
    field: string
    action: Action
}

export interface IMetadata {
    src?: string
    id?: string
    routes?: IRoutes
    /* FIXME */
    toolbar?: Record<string, object[]>
    breadcrumb?: ICrumb[]
    datasources?: Record<string, DataSourceState>
    /* FIXME */
    regions?: Record<string, object[]>
    width?: {[key: string]: string}
    page?: { model: string, htmlTitle: string, title: string }
    events?: IEvents[]
    widget?: string
    models?: DefaultModels
}

export interface IPage {
    loading: boolean
    error: object | boolean
    disabled: boolean
    status: number | null
    metadata: IMetadata
    spinner?: boolean
}

export type State = Record<string, IPage>
