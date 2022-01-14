import isEmpty from 'lodash/isEmpty'
import isNumber from 'lodash/isNumber'
import isUndefined from 'lodash/isUndefined'
import isNull from 'lodash/isNull'
import isNaN from 'lodash/isNaN'
import isString from 'lodash/isString'
import isObject from 'lodash/isObject'
import get from 'lodash/get'

import evalExpression from '../../utils/evalExpression'
import { defaultApiProvider, FETCH_VALIDATE } from '../api'

/**
 * Валидация того, что email
 * @param fieldId
 * @param values
 * @returns {boolean}
 */
export function email(fieldId, values) {
    return (
        isString(values[fieldId]) &&
        /^[\w%+.-]+@[\d.a-z-]+\.[a-z]{2,4}$/i.test(values[fieldId])
    )
}

/**
 * Валидация того, что поле непустое
 * @param fieldId
 * @param values
 * @param options
 * @returns {*}
 */
export function required(fieldId, values, options = {}) {
    const value = get(values, fieldId)

    if (options.expression && !evalExpression(options.expression, values)) {
        return true
    }

    if (isObject(value)) {
        return !isEmpty(value)
    } if (isString(value)) {
        return value !== ''
    }

    return (
        !isUndefined(get(values, fieldId)) &&
        !isNull(get(values, fieldId)) &&
        !isNaN(get(values, fieldId))
    )
}

/**
 * Валидация js-condition
 * @param fieldId
 * @param values
 * @param options
 * @returns {boolean}
 */
export function condition(fieldId, values, options = {}) {
    return evalExpression(options.expression, values)
}

/**
 * Валидация с отправкой запроса на сервер
 * @param fieldId
 * @param values
 * @param options
 * @returns {boolean|*}
 */
export function constraint(fieldId, values, options) {
    if (!isEmpty(values[fieldId])) {
        return defaultApiProvider[FETCH_VALIDATE](options)
    }

    return Promise.reject(new Error('is empty value'))
}

/**
 * Валидация того, что integer
 * @param fieldId
 * @param values
 * @returns {boolean}
 * @deprecated
 */
export function integer(fieldId, values) {
    const v = values[fieldId]

    return v && !isNaN(Number(v)) && isNumber(Number(v))
}

/**
 * Валидация минимальной длины
 * @param fieldId
 * @param values
 * @param options
 * @returns {boolean}
 */
export function minLength(fieldId, values, options) {
    return isString(values[fieldId]) && values[fieldId].length > options.min
}

/**
 * Валиадация максимальной длины
 * @param fieldId
 * @param values
 * @param options
 * @returns {boolean}
 */
export function maxLength(fieldId, values, options) {
    return isString(values[fieldId]) && values[fieldId].length < options.max
}
