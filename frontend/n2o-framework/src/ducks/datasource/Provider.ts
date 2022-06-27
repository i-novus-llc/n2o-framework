export enum ProviderType {
    storage = 'browser',
    service = 'service',
    inherited = 'inherited',
    application = 'application'
}

export interface IProviderBase {
    type: ProviderType
}
interface IMappingParam {
    link: string
    observe: boolean
    required: boolean
    value: string
}

export interface ServiceProvider extends IProviderBase {
    type: ProviderType.service
    url: string
    pathMappeng: Record<string, IMappingParam>
    queryMapping: Record<string, IMappingParam>
    size: number
}

export type IProvider = IProviderBase & (ServiceProvider)

export interface QueryResult<TModel extends object = object> {
    list: TModel[]
    page: number
    count: number
    meta?: object
}
