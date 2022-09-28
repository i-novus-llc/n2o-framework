/* eslint-disable @typescript-eslint/no-extraneous-class */
import type { DataSourceDependency, SortDirection } from '../../core/datasource/const'
import { ModelPrefix } from '../../core/datasource/const'
import { ValidationsKey, IValidation, IValidationResult } from '../../core/validation/IValidation'

import type { IProvider, ISubmit, Paging } from './Provider'

export type State = Record<string, DataSourceState>

export interface DataSourceState<
    // TModel extends object = object,
    TKey extends string = string // TKey = keyof TModel
> {
    provider?: IProvider
    [ValidationsKey.Validations]: Record<string, IValidation[]>
    [ValidationsKey.FilterValidations]: Record<string, IValidation[]>
    components: string[]
    dependencies: DataSourceDependency[]
    paging: Paging
    additionalInfo: object
    loading: boolean
    sorting: Partial<Record<TKey, SortDirection>>
    submit?: ISubmit
    fieldsSubmit: Record<TKey, IProvider>
    pageId?: string
    // TODO: rename to "messages"
    errors: Record<
    ModelPrefix.active | ModelPrefix.edit,
    Partial<Record<TKey, IValidationResult[]>>
    >
    error?: Error | object
}

export class DataSource {
    static get defaultState(): DataSourceState {
        return ({
            [ValidationsKey.Validations]: {},
            [ValidationsKey.FilterValidations]: {},
            components: [],
            dependencies: [],
            paging: {
                page: 1,
                size: 1,
                count: 0,
            },
            additionalInfo: {},
            loading: false,
            sorting: {},
            errors: {
                [ModelPrefix.active]: {},
                [ModelPrefix.edit]: {},
            },
            fieldsSubmit: {},
        })
    }
}
