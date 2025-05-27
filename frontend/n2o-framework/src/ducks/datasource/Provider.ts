import type { ModelPrefix } from '../../core/datasource/const'
import { Meta } from '../Action'

export enum ProviderType {
    storage = 'browser',
    service = 'service',
    inherited = 'inherited',
    cached = 'cached',
    autoSaveCache = 'autoSaveCache',
}

export enum FilterType {
    Equal = 'eq',
}

export interface Provider {
    type: ProviderType
    sourceDs: string
    url: string
    sourceField?: string
    sourceModel?: ModelPrefix
    sorting?: Record<string, unknown>
}

export interface Filter {
    type: FilterType
    fieldId: string
    value: string
    link?: string
}

export interface MappingParam {
    link: string
    observe: boolean
    required: boolean
    value: string
}

export type Mapping = Record<string, MappingParam>

export interface ServiceProvider extends Provider {
    type: ProviderType.service
    url: string
    pathMapping: Mapping
    queryMapping: Mapping
    headerMapping: Mapping
    size: number
}

export enum StorageType {
    local = 'localStorage',
    session = 'sessionStorage',
}

export interface StorageProvider extends Provider {
    type: ProviderType.storage
    key: string
    storage: StorageType
}

export interface CachedProvider extends Provider {
    type: ProviderType.cached
    key: string
    storage: StorageType
    cacheExpires: string
    pathMapping: Record<string, MappingParam>
    queryMapping: Record<string, MappingParam>
    size: number
}

export interface InheritedProvider extends Provider {
    type: ProviderType.inherited
    sourceDs: string
    sourceModel: ModelPrefix
    sourceField?: string
    filters?: Filter[]
    fetchValueExpression?: string
}

export interface Paging {
    page: number
    size: number
    count: number
    withCount?: boolean
}

export interface QueryResult<TModel extends object = Record<string, unknown>> {
    list: TModel[]
    active?: TModel
    additionalInfo?: unknown
    paging: Paging
    meta?: Meta
}

export type QueryOptions = { page?: number, withCount?: boolean }

export type Query<TProvider extends Provider> = (
    id: string,
    provider: TProvider,
    options: QueryOptions,
    apiProvider: unknown) => unknown

export interface SubmitBase extends Provider {
    auto?: boolean
    // FIXME remove legacy field
    autoSubmitOn?: 'change' | 'blur'
    model: ModelPrefix
}

export interface ServiceSubmit extends SubmitBase {
    type: ProviderType.service
    url: string
    pathMapping: Record<string, MappingParam>
    queryMapping: Record<string, MappingParam>
    headerMapping: Record<string, MappingParam>
    formMapping: Record<string, MappingParam>
    submitForm: boolean
    method: string
}

export interface StorageSubmit extends SubmitBase {
    type: ProviderType.storage
    key: string,
    storage: StorageType
}

export interface CachedSubmit extends Omit<ServiceSubmit, 'type'> {
    type: ProviderType.cached
    clearCache: boolean
    key: string
    storage: StorageType
}
export interface CachedAutoSubmit extends SubmitBase {
    type: ProviderType.autoSaveCache
    key: string
    storage: StorageType
}

export interface InheritedSubmit extends SubmitBase {
    type: ProviderType.inherited
    targetDs: string,
    targetModel: ModelPrefix
    targetField?: string
    submitValueExpression?: string
}

export type SubmitProvider = StorageSubmit | InheritedSubmit | ServiceSubmit | CachedSubmit | CachedAutoSubmit
