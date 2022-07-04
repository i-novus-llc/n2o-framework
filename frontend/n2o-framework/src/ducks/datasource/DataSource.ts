/* eslint-disable @typescript-eslint/no-extraneous-class */
import type { DataSourceDependency, SortDirection } from '../../core/datasource/const'
import { ModelPrefix } from '../../core/datasource/const'
import type { IValidation, IValidationResult } from '../../core/validation/IValidation'

import type { IProvider } from './Provider'

export type State = Record<string, DataSourceState>

export interface DataSourceState<
    // TModel extends object = object,
    TKey extends string = string // TKey = keyof TModel
> {
    provider?: IProvider
    validations: Record<string, IValidation[]>
    components: string[]
    dependencies: DataSourceDependency[]
    size: number
    page: number
    count: number
    loading: boolean
    sorting: Partial<Record<TKey, SortDirection>>
    submit?: IProvider // FIXME
    fieldsSubmit: Record<TKey, IProvider>
    errors: Record<
    ModelPrefix.active | ModelPrefix.edit,
    Partial<Record<TKey, IValidationResult[]>>
    >
}

export class DataSource {
    static get defaultState(): DataSourceState {
        return ({
            validations: {},
            components: [],
            dependencies: [],
            size: 0,
            count: 0,
            page: 1,
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
