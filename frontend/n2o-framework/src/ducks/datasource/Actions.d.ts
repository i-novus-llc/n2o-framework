import type { ModelPrefix, SortDirection } from '../../core/datasource/const'
import type { IValidationResult } from '../../core/validation/IValidation'
import { IActionMeta } from '../../sagas/types'
import { ValidationsKey } from '../../core/validation/IValidation'
import { Action, Meta } from '../Action'

import type { DataSourceState } from './DataSource'
import type { IProvider, ISubmit, QueryResult } from './Provider'

export interface DatasourcePayload {
    id: string
}

export type DatasourceAction<
    TPayload extends DatasourcePayload,
    TMeta extends Meta = Meta
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
    error?: Error | object
}>

export type SetSortDirectionAction = DatasourceAction<{
    id: string
    field: string
    direction: SortDirection
}>

export type SetAdditionalInfoAction = DatasourceAction<{
    id: string
    additionalInfo: object
}>

export type ChangePageAction = DatasourceAction<{
    id: string
    page: number
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

export type ResetValidateActionMulti = DatasourceAction<{
    id: string
    prefix: ModelPrefix.active | ModelPrefix.edit | ModelPrefix.filter
    field: string
    index: number
    count: number
}>

export type FailValidateAction = DatasourceAction<{
    id: string
    prefix: ModelPrefix.active | ModelPrefix.edit | ModelPrefix.filter
    fields: Record<string, IValidationResult[]>
}, { touched: boolean }>

export type SetFieldSubmitAction = DatasourceAction<{
    id: string
    field: string
    provider: IProvider
}>

export type SubmitAction = DatasourceAction<{
    id: string
    provider?: ISubmit
}, IActionMeta>
