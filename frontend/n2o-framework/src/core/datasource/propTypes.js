import PropTypes from 'prop-types'

/**
 * Методы для взаимодействия с datasource
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
 */
export const modelsType = {
    resolve: PropTypes.object,
    filter: PropTypes.object,
    multi: PropTypes.array,
    datasource: PropTypes.array,
}

/**
 * Параметры состояния подключенного datasource
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
 */
export const WithDataSourceTypes = {
    ...DataSourceMethodsTypes,
    ...DataSourcePropsTypes,
}
