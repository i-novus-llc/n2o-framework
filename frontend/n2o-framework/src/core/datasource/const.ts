/**
 * @enum SortDirection Направление сортировки
 */
export enum SortDirection {
    none = 'NONE',
    asc = 'ASC',
    desc = 'DESC'
}

/**
 * @enum ModelPrefix Префиксы для используемых моделей
 */
export enum ModelPrefix {
    active = 'resolve',
    selected = 'multi',
    source = 'datasource',
    filter = 'filter',
    edit = 'edit'
}

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
    on: string
    applyOnInit: boolean
}

export interface DataSourceDependencyCopy extends DataSourceDependencyBase {
    type: DependencyTypes.copy
    model: ModelPrefix
    field?: string
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
