/* eslint-disable @typescript-eslint/no-extraneous-class */
import type { DataSourceDependency, SortDirection } from '../../core/datasource/const'
import { ModelPrefix } from '../../core/datasource/const'
import { ValidationsKey, Validation, ValidationResult } from '../../core/validation/types'

import type { Provider, ISubmit, Paging } from './Provider'

export type State = Record<string, DataSourceState>

export interface DataSourceState {
    provider?: Provider
    [ValidationsKey.Validations]: Record<string, Validation[]>
    [ValidationsKey.FilterValidations]: Record<string, Validation[]>
    components: string[]
    dependencies: DataSourceDependency[]
    paging: Paging
    additionalInfo: object
    loading: boolean
    sorting: Partial<Record<string, SortDirection>>
    submit?: ISubmit
    fieldsSubmit: Record<string, Provider>
    pageId?: string
    // TODO: rename to "messages"
    errors: Record<
    ModelPrefix,
    Partial<Record<string, ValidationResult[]>>
    >
    error?: object
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
                [ModelPrefix.filter]: {},
                [ModelPrefix.selected]: {},
                [ModelPrefix.source]: {},
            },
            fieldsSubmit: {},
        })
    }
}
