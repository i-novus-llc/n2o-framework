import type { ModelPrefix } from '../../core/datasource/const'

export enum ProviderType {
    storage = 'browser',
    service = 'service',
    inherited = 'inherited',
}

export enum FilterType {
    Equal = 'eq'
}

export interface IProvider {
    type: ProviderType
}

export interface IFilter {
    type: FilterType
    fieldId: string
    value: string
    link?: string
}

export interface IMappingParam {
    link: string
    observe: boolean
    required: boolean
    value: string
}

export interface ServiceProvider extends IProvider {
    type: ProviderType.service
    url: string
    pathMapping: Record<string, IMappingParam>
    queryMapping: Record<string, IMappingParam>
    headerMapping: Record<string, IMappingParam>
    size: number
}

export enum StorageType {
    local = 'localStorage',
    session = 'sessionStorage',
}

export interface StorageProvider extends IProvider {
    type: ProviderType.storage
    key: string,
    storage: StorageType
}

export interface InheritedProvider extends IProvider {
    type: ProviderType.inherited
    sourceDs: string
    sourceModel: ModelPrefix
    sourceField?: string
    filters?: IFilter[]
}

export interface Paging {
    page: number
    size: number
    count: number
}

export interface QueryResult<TModel extends object = object> {
    list: TModel[]
    additionalInfo?: object
    paging: Paging
    meta?: object
}

export type QueryOptions = { page?: number }

export type Query<TProvider extends IProvider> = (
    id: string,
    provider: TProvider,
    options: QueryOptions,
    apiProvider: unknown) => unknown

export interface ISubmitBase extends IProvider {
    auto: boolean
}

export interface ServiceSubmit {
    type: ProviderType.service
    autoSubmitOn?: 'change' | 'blur'
    url: string
    pathMapping: Record<string, IMappingParam>
    queryMapping: Record<string, IMappingParam>
    headerMapping: Record<string, IMappingParam>
    formMapping: Record<string, IMappingParam>
    submitForm: boolean
    method: string
}

export interface StorageSubmit extends ISubmitBase {
    type: ProviderType.storage
    key: string,
    storage: StorageType
    model: ModelPrefix
}

export interface InheritedSubmit extends ISubmitBase {
    type: ProviderType.inherited
    model: ModelPrefix
    targetDs: string,
    targetModel: ModelPrefix
    targetField?: string
}

export type ISubmit = StorageSubmit | InheritedSubmit | ServiceSubmit
