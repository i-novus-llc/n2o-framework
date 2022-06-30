export enum ProviderType {
    storage = 'browser',
    service = 'service',
    inherited = 'inherited',
}

export interface IProvider {
    type: ProviderType
}
interface IMappingParam {
    link: string
    observe: boolean
    required: boolean
    value: string
}

export interface ServiceProvider extends IProvider {
    type: ProviderType.service
    url: string
    pathMappeng: Record<string, IMappingParam>
    queryMapping: Record<string, IMappingParam>
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

export interface QueryResult<TModel extends object = object> {
    list: TModel[]
    page: number
    count: number
    meta?: object
}

export type QueryOptions = { page?: number }

export type Query<TProvider extends IProvider> = (id: string, provider: TProvider, options: QueryOptions) => unknown
