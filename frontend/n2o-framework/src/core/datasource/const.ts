/**
 * @enum SortDirection Направление сортировки
 */
export enum SortDirection {
    NONE = 'NONE',
    ASC = 'ASC',
    DESC = 'DESC',
}

/**
 * @enum ModelPrefix Префиксы для используемых моделей
 */
export enum ModelPrefix {
    active = 'resolve',
    selected = 'multi',
    source = 'datasource',
    filter = 'filter',
    edit = 'edit',
}

/**
 * @enum DEPENDENCY_TYPE Типы зависимостей
 */
export enum DEPENDENCY_TYPE {
    fetch = 'fetch',
    validate = 'validate',
}

export type DataSourceDependency = {
    type: DEPENDENCY_TYPE,
    on: string
}