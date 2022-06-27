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
    validate = 'validate'
}

export type DataSourceDependency = {
    type: DependencyTypes,
    on: string
}
