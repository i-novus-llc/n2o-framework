import type { ModelPrefix, SortDirection } from '../../core/datasource/const'
import type { ValidationResult } from '../../core/validation/types'
import { ActionMeta } from '../../sagas/types'
import { ValidationsKey } from '../../core/validation/types'
import { Action, Meta } from '../Action'

import type { DataSourceState } from './DataSource'
import type { Provider, SubmitProvider, QueryResult, Paging } from './Provider'

export interface DatasourcePayload {
    id: string
}

export type DatasourceAction<
    TPayload extends DatasourcePayload,
    TMeta extends Meta = Meta,
> = Action<string, TPayload, TMeta>

export type RegisterAction = DatasourceAction<{
    id: string
    initProps: Partial<DataSourceState>
}>

export type RemoveAction = DatasourceAction<DatasourcePayload>

export type AddComponentAction = DatasourceAction<{
    id: string
    componentId: string
}>

export type RemoveComponentAction = DatasourceAction<{
    id: string
    componentId: string
}>

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

export type SetAdditionalInfoAction = DatasourceAction<{
    id: string
    additionalInfo: unknown
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

export type StartValidateAction = DatasourceAction<{
    id: string
    validationsKey?: ValidationsKey
    prefix: ModelPrefix.active | ModelPrefix.edit | ModelPrefix.filter
    fields?: string[]
}, { touched: boolean }>

export type FailValidateAction = DatasourceAction<{
    id: string
    prefix: ModelPrefix.active | ModelPrefix.edit | ModelPrefix.filter
    fields: Record<string, ValidationResult[]>
}, { touched: boolean, isTriggeredByFieldChange?: boolean }>

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
