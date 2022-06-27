import type { ModelPrefix, SortDirection } from '../../core/datasource/const'
import type { IValidationResult } from '../../core/validation/IValidation'

import type { DataSourceState } from './DataSource'
import type { IProvider, QueryResult } from './Provider'

export interface DatasourcePayload {
    id: string
}

export interface DatasourceAction<
    TPayload extends DatasourcePayload,
    TMeta extends object = object
> {
    type: string
    payload: TPayload
    meta?: TMeta
}

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

export type DataRequestAction = DatasourceAction<{
    id: string
    options: {
        // FIXME
    }
}>

export type ResolveRequestAction = DatasourceAction<{
    id: string
    query: QueryResult
}>

export type FailRequestAction = DatasourceAction<{
    id: string
}>

export type SetSortDirectionAction = DatasourceAction<{
    id: string
    field: string
    direction: SortDirection
}>

export type ChangePageAction = DatasourceAction<{
    id: string
    page: number
}>

export type ChangeCountAction = DatasourceAction<{
    id: string
    count: number
}>

export type ChangeSizeAction = DatasourceAction<{
    id: string
    size: number
}>

export type StartValidateAction = DatasourceAction<{
    id: string
    prefix: ModelPrefix.active | ModelPrefix.edit
    fields?: string[]
}, { touched: boolean }>

export type FailValidateAction = DatasourceAction<{
    id: string
    prefix: ModelPrefix.active | ModelPrefix.edit
    fields: Record<string, IValidationResult[]>
}, { touched: boolean }>

export type SetModelAction<
    TModel extends object | object[] = object
> = DatasourceAction<{
    id: string
    prefix: ModelPrefix
    model: TModel
}>

export type SetFieldSubmitAction = DatasourceAction<{
    id: string
    field: string
    provider: IProvider
}>
