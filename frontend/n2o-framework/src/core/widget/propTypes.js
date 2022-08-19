import PropTypes from 'prop-types'

import { WithDataSourceTypes } from '../datasource/propTypes'

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
 * @property {boolean} fetchOnInit
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
    fetchOnInit: PropTypes.bool,
    className: PropTypes.string,
    style: PropTypes.any,
    autoFocus: PropTypes.bool,
    disabled: PropTypes.bool,
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
 * @typedef {WidgetReduxTypes, WidgetDatasourceTypes, WidgetMethods} WidgetTypes
 */
export const widgetPropTypes = {
    ...reduxTypes,
    ...WithDataSourceTypes,
}
