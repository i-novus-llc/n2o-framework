/**
 * @enum SORT_DIRECTION Направление сортировки
 * @property {string} NONE
 * @property {string} ASC
 * @property {string} DESC
 */
export const SORT_DIRECTION = {
    NONE: 'NONE',
    ASC: 'ASC',
    DESC: 'DESC',
}

/**
 * @enum MODEL_PREFIX Префиксы для используемых моделей
 * @property {string} active
 * @property {string} selected
 * @property {string} source
 * @property {string} filter
 */
export const MODEL_PREFIX = {
    active: 'resolve',
    selected: 'multi',
    source: 'datasource',
    filter: 'filter',
    edit: 'edit',
}

/**
 * @enum VALIDATION_SEVERITY
 * @property {string} danger
 * @property {string} warning
 * @property {string} success
 */
export const VALIDATION_SEVERITY = {
    danger: 'danger',
    warning: 'warning',
    success: 'success',
}

/**
 * @enum VALIDATION_SEVERITY_PRIORITY Приоритет сообщений валидации по типу
 * @property {number} danger
 * @property {number} warning
 * @property {number} success
 */
export const VALIDATION_SEVERITY_PRIORITY = {
    [VALIDATION_SEVERITY.danger]: 0,
    [VALIDATION_SEVERITY.warning]: 1,
    [VALIDATION_SEVERITY.success]: 2,
}

/**
 * @enum DEPENDENCY_TYPE Типы зависимостей
 * @property {string} fetch
 * @property {string} validate
 */
export const DEPENDENCY_TYPE = {
    fetch: 'fetch',
    validate: 'validate',
}

/**
 * @typedef DataSourceDependency
 * @property {DEPENDENCY_TYPE} type
 * @property {string} on
 */
