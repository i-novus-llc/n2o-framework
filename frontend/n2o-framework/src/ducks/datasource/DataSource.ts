/* eslint-disable @typescript-eslint/no-extraneous-class */
import type { DataSourceDependency, SortDirection } from '../../core/datasource/const'
import { ModelPrefix } from '../../core/datasource/const'
import { ValidationsKey, Validation, ValidationResult } from '../../core/validation/types'
import { type ErrorContainerError } from '../../core/error/types'

import type { Provider, SubmitProvider, Paging, ServiceSubmit } from './Provider'

export type State = Record<string, DataSourceState>

type ValidationConfig = Omit<Validation, 'on'> & {
    on?: string[]
}

export type DataSourceState = {
    provider?: Provider
    [ValidationsKey.Validations]: Record<string, Validation[]>
    [ValidationsKey.FilterValidations]: Record<string, Validation[]>
    components: string[]
    dependencies: DataSourceDependency[]
    defaultDatasourceProps?: { sorting?: Record<string, SortDirection>, paging?: Paging }
    paging: Paging
    additionalInfo: unknown
    loading: boolean
    sorting: Record<string, SortDirection>
    submit?: SubmitProvider
    fieldsSubmit: Record<string, ServiceSubmit>
    pageId?: string
    // TODO: rename to "messages"
    errors: Record<
    ModelPrefix,
    Partial<Record<string, ValidationResult[]>>
    >
    error?: ErrorContainerError
    fetchOnInit?: boolean
}

type Prettify<T> = {
    [K in keyof T]: T[K];
}

export type DataSourceConfig = Prettify<Omit<DataSourceState, ValidationsKey> & {
    [k in ValidationsKey]?: Record<string, ValidationConfig[]>
}>

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
            additionalInfo: undefined,
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
