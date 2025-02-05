import PropTypes from 'prop-types'

import { WithDataSourceTypes } from '../datasource/propTypes'

import { FETCH_TYPE } from './const'

/**
 * Объект входных параметров виджета, приходящий с БЛ
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
 */
export const reduxTypes = {
    ...widgetInitialTypes,
    isInit: PropTypes.bool,
    isActive: PropTypes.bool,
    dispatch: PropTypes.func,
}

export const widgetPropTypes = {
    ...reduxTypes,
    ...WithDataSourceTypes,
}
