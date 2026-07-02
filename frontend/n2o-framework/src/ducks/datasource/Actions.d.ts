import { ModelLink, ModelPrefix } from '../../core/models/types'
import type { SortDirection } from '../../core/datasource/const'
import type { Validation, ValidationResult } from '../../core/validation/types'
import { ActionMeta } from '../../sagas/types'
import { Action, Meta } from '../Action'

import type { DataSourceConfig } from './DataSource'
import type { Provider, SubmitProvider, QueryResult, Paging } from './Provider'

export interface DatasourcePayload {
    id: string
}

export type DatasourceAction<
    TPayload = DatasourcePayload,
    TMeta extends Meta = Meta,
> = Action<string, DatasourcePayload & TPayload, TMeta>

export type RegisterAction = DatasourceAction<{
    id: string
    initProps: DataSourceConfig
}>

export type RemoveAction = DatasourceAction<DatasourcePayload>

export type ResetDatasourceAction = DatasourceAction<{
    id: string
}>

export type DataRequestAction = DatasourceAction<{
    id: string
    options: {
        // FIXME
    }
}, Meta>

export type ResolveRequestAction = DatasourceAction<{
    id: string
    query: QueryResult
}>

export type FailRequestAction = DatasourceAction<{
    id: string
    error?: Error
}>

export type SetSortDirectionAction = DatasourceAction<{
    id: string
    field: string
    direction: SortDirection
}>

export type UpdatePagingAction = DatasourceAction<{
    id: string
    paging: Partial<Paging>
}>

export type ChangePageAction = DatasourceAction<{
    id: string
    page: number
    options?: object
}>

export type ChangeSizeAction = DatasourceAction<{
    id: string
    size: number
}>

export type StartValidateAction = Action<string, {
    modelLink: ModelLink
    fields?: Record<string, Validation[]>
}, Meta & { touched: boolean }>

export type ValidateEndPayload = {
    modelLink: ModelLink
    messages: Record<string, ValidationResult[]>
    fields?: string[]
}

export type ValidateEndAction = DatasourceAction<string, ValidateEndPayload, Meta>

export type SetFieldSubmitAction = DatasourceAction<{
    id: string
    field: string
    provider: Provider
}>

export type SubmitAction = DatasourceAction<{
    id: string
    provider?: SubmitProvider
}, ActionMeta>

export type MapParamPayload = {
    ['paging.page']?: string
    ['paging.size']?: string
    id: string
}

export type MapParamAction = DatasourceAction<MapParamPayload, ActionMeta>
