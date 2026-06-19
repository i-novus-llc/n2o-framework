import { type DataSourceDependency, SortDirection } from '../../core/datasource/const'
import { ModelPrefix } from '../../core/models/types'
import { ValidationsKey, Validation, ValidationResult } from '../../core/validation/types'
import { type ErrorContainerError } from '../../core/error/types'

import type { Provider, SubmitProvider, Paging, ServiceSubmit } from './Provider'

export type State = Record<string, DataSourceState>

type ValidationConfig = Omit<Validation, 'on'> & {
    on?: Array<string | RegExp>
}

export interface DefaultDataSourceProps {
    sorting?: Record<string, SortDirection>,
    paging?: Paging
}

export enum DataSourceCacheKeys {
    SIZE = 'size',
    PAGE = 'page',
    SORTING = 'sorting',
}

export type DataSourceCache = {
    paging: Partial<Pick<Paging, DataSourceCacheKeys.SIZE | DataSourceCacheKeys.PAGE>>
    sorting: Record<string, SortDirection>
}

export type DataSourceSaveSettings = DataSourceCacheKeys[]

export type DataSourceState = {
    provider?: Provider
    [ValidationsKey.Validations]: Record<string, Validation[]>
    [ValidationsKey.FilterValidations]: Record<string, Validation[]>
    dependencies: DataSourceDependency[]
    defaultDatasourceProps?: DefaultDataSourceProps
    paging: Paging
    additionalInfo: unknown
    loading: boolean
    sorting: Record<string, SortDirection>
    submit?: SubmitProvider
    saveSettings?: DataSourceSaveSettings
    fieldsSubmit: Record<string, ServiceSubmit>
    pageId?: string
    // TODO: rename to "messages"
    errors: Record<
    ModelPrefix,
    Record<string, ValidationResult[]>
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

// eslint-disable-next-line @typescript-eslint/no-extraneous-class
export class DataSource {
    static get defaultState(): DataSourceState {
        return ({
            [ValidationsKey.Validations]: {},
            [ValidationsKey.FilterValidations]: {},
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
