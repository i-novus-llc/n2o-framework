import type { ModelPrefix } from '../../core/datasource/const'
import { Meta } from '../Action'

export enum ProviderType {
    storage = 'browser',
    service = 'service',
    inherited = 'inherited',
}

export enum FilterType {
    Equal = 'eq'
}

export interface Provider {
    type: ProviderType
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

export interface ServiceProvider extends Provider {
    type: ProviderType.service
    url: string
    pathMapping: Record<string, MappingParam>
    queryMapping: Record<string, MappingParam>
    headerMapping: Record<string, MappingParam>
    size: number
}

export enum StorageType {
    local = 'localStorage',
    session = 'sessionStorage',
}

export interface StorageProvider extends Provider {
    type: ProviderType.storage
    key: string,
    storage: StorageType
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
}

export interface QueryResult<TModel extends object = object> {
    list: TModel[]
    additionalInfo?: object
    paging: Paging
    meta?: Meta
}

export type QueryOptions = { page?: number }

export type Query<TProvider extends Provider> = (
    id: string,
    provider: TProvider,
    options: QueryOptions,
    apiProvider: unknown) => unknown

export interface SubmitBase extends Provider {
    auto: boolean
}

export interface ServiceSubmit {
    type: ProviderType.service
    autoSubmitOn?: 'change' | 'blur'
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
    model: ModelPrefix
}

export interface InheritedSubmit extends SubmitBase {
    type: ProviderType.inherited
    model: ModelPrefix
    targetDs: string,
    targetModel: ModelPrefix
    targetField?: string
    submitValueExpression?: string
}

export type ISubmit = StorageSubmit | InheritedSubmit | ServiceSubmit
