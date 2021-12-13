import PropTypes from 'prop-types'

import { FETCH_TYPE } from './const'

/**
 * Объект входных параметров виджета, приходящий с БЛ
 * @typedef {Object} WidgetInitialTypes
 * @property {string} id
 * @property {string} datasource
 * @property {string} pageId
 * @property {boolean} visible
 * @property {Array} dependency // TODO описать типы зависимостей
 * @property {object} toolbar // TODO описать тип
 * @property {'always'|'lazy'|'never'} fetch
 */
export const widgetInitialTypes = {
    id: PropTypes.string.isRequired,
    datasource: PropTypes.string.isRequired,
    pageId: PropTypes.string.isRequired,
    visible: PropTypes.bool,
    dependency: PropTypes.array,
    toolbar: PropTypes.any,
    fetch: PropTypes.oneOf([
        FETCH_TYPE.always,
        FETCH_TYPE.lazy,
        FETCH_TYPE.never,
    ]).isRequired,
    className: PropTypes.string,
    style: PropTypes.any,
    autoFocus: PropTypes.bool,
}

/**
 * Модели данных
 * @typedef {WidgetInitialTypes} WidgetModelTypes
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
 * Данные виджета, хранимые в redux
 * @typedef {WidgetInitialTypes} WidgetReduxTypes
 * @property {boolean} isInit
 * @property {boolean} isActive
 */
export const reduxTypes = {
    ...widgetInitialTypes,
    isInit: PropTypes.bool,
    isActive: PropTypes.bool,
    dispatch: PropTypes.func,
}

/**
 * Данные, необходимые для виджета, но лежащие в datasource
 * @typedef {object} WidgetDatasourceTypes
 * @property {boolean} loading
 * @property {object} sorting // todo set type from datasource
 * @property {WidgetModelTypes} models
 * @property {number} page
 */
export const dataSourceTypes = {
    loading: PropTypes.bool,
    sorting: PropTypes.object,
    models: PropTypes.shape(modelsType),
    page: PropTypes.number,
    size: PropTypes.number,
    count: PropTypes.number,
}

/**
 * Методы для управления состоянием виджета
 * @typedef {object} WidgetMethods
 * @property {Function} fetchData
 * @property {Function} setFilter
 */
export const widgetMethodsProps = {
    fetchData: PropTypes.func,
    setFilter: PropTypes.func,
    setResolve: PropTypes.func,
    setSelected: PropTypes.func,
    setSorting: PropTypes.func,
    setPage: PropTypes.func,
    setSize: PropTypes.func,
}

/**
 * @typedef {WidgetReduxTypes, WidgetDatasourceTypes, WidgetMethods} WidgetTypes
 */
export const widgetPropTypes = {
    ...dataSourceTypes,
    ...reduxTypes,
    ...widgetMethodsProps,
}
