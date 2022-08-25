import PropTypes from 'prop-types'

/**
 * Параметры для работы обёртки подключающей компонент к источнику данных
 * @typedef {Object} WithDatasourceInitTypes
 * @property {string} id componentId
 * @property {string} datasource datasourceId
 */
export const WithDatasourceInitTypes = {
    id: PropTypes.string,
    datasource: PropTypes.string,
}

/**
 * Методы для взаимодействия с datasource
 * @typedef {Object} DataSourceMethodsTypes
 * @property {function} register
 * @property {function} unregister
 * @property {function} setFilter
 * @property {function} setResolve
 * @property {function} setSorting
 * @property {function} setPage
 * @property {function} fetchData
 */
export const DataSourceMethodsTypes = {
    register: PropTypes.func,
    unregister: PropTypes.func,
    setFilter: PropTypes.func,
    setResolve: PropTypes.func,
    setSelected: PropTypes.func,
    setSorting: PropTypes.func,
    setPage: PropTypes.func,
    setSize: PropTypes.func,
    fetchData: PropTypes.func,
}

/**
 * Модели данных
 * @typedef {Object} ModelTypes
 * @property {object} resolve
 * @property {object} filter
 * @property {Array.<object>} multi
 * @property {Array.<object>} datasource
 */
export const modelsType = {
    resolve: PropTypes.object,
    filter: PropTypes.object,
    multi: PropTypes.array,
    datasource: PropTypes.array,
}

/**
 * Параметры состояния подключенного datasource
 * @typedef {object} DataSourcePropsTypes
 * @property {boolean} loading
 * @property {object} sorting // todo set type from datasource
 * @property {ModelTypes} models
 * @property {number} page
 * @property {number} size
 * @property {number} count
 */
export const DataSourcePropsTypes = {
    loading: PropTypes.bool,
    sorting: PropTypes.object,
    models: PropTypes.shape(modelsType),
    page: PropTypes.number,
    size: PropTypes.number,
    count: PropTypes.number,
}

/**
 * Методы для взаимодействия с datasource
 * @typedef {Object} WithDataSourceTypes
 * @property {boolean} visible
 * @property {boolean} fetchOnInit
 * @property {string} id componentId
 * @property {string} datasource datasourceId
 * @property {function} dispatch
 * @property {function} register
 * @property {function} unregister
 * @property {function} setFilter
 * @property {function} setResolve
 * @property {function} setSorting
 * @property {function} setPage
 * @property {function} fetchData
 */
export const WithDataSourceTypes = {
    ...DataSourceMethodsTypes,
    ...DataSourcePropsTypes,
}
