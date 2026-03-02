import { ModelLink, ModelPrefix } from '../models/types'

/**
 * @enum SortDirection Направление сортировки
 */
export enum SortDirection {
    none = 'NONE',
    asc = 'ASC',
    desc = 'DESC',
}

export { ModelPrefix }

/**
 * @enum DependencyTypes Типы зависимостей
 */
export enum DependencyTypes {
    fetch = 'fetch',
    validate = 'validate',
    copy = 'copy',
}

export interface DataSourceDependencyBase {
    type: DependencyTypes
    on: ModelLink
    applyOnInit: boolean
}

export interface DataSourceDependencyCopy extends DataSourceDependencyBase {
    type: DependencyTypes.copy
    model: ModelPrefix
    field?: string
    submit: boolean
}

export interface DataSourceDependencyFetch extends DataSourceDependencyBase {
    type: DependencyTypes.fetch
}

export interface DataSourceDependencyValidate extends DataSourceDependencyBase {
    type: DependencyTypes.validate
}

export type DataSourceDependency = (
    DataSourceDependencyFetch
    | DataSourceDependencyCopy
    | DataSourceDependencyValidate
)

export interface DataSourceModels {
    datasource: object[],
    edit: object,
    filter: object,
    multi: object[],
    resolve: object,
}
