import {
    isEmpty,
    isNumber,
    isUndefined,
    isNull,
    isNaN,
    isString,
    isObject,
    get,
} from 'lodash'

import evalExpression from '../../utils/evalExpression'
import { defaultApiProvider, FETCH_VALIDATE } from '../api'
import { isEmptyModel } from '../../utils/isEmptyModel'

import type { ValidateFunction } from './types'
import { ValidationTypes } from './types'

/**
 * Валидация того, что email
 * @returns {boolean}
 */
export function email<
    TData extends object,
    TKey extends keyof TData
>(fieldId: TKey, values: TData) {
    const value = values[fieldId]

    return (
        typeof value === 'string' &&
        /^[\w%+.-]+@[\d.a-z-]+\.[a-z]{2,4}$/i.test(value)
    )
}

/**
 * Валидация того, что поле непустое
 */
export function required<
    TData extends object,
    TKey extends keyof TData,
    TOptions extends { expression: string }
>(fieldId: TKey, values: TData, options: TOptions): boolean {
    const value = get(values, fieldId) // get, т.к. в качестве field может прийти путь

    if (options.expression && !evalExpression(options.expression, values)) {
        return true
    }

    if (isObject(value)) {
        return !isEmptyModel(value)
    }
    if (isString(value)) {
        return value !== ''
    }

    return (
        !isUndefined(value) &&
        !isNull(value) &&
        !isNaN(value)
    )
}

/**
 * Валидация js-condition
 */
export function condition<
    TData extends object,
    TKey extends keyof TData,
    TOptions extends { expression: string }
>(fieldId: TKey, values: TData, options: TOptions): boolean {
    return !!evalExpression(options.expression, values)
}

/**
 * Валидация с отправкой запроса на сервер
 */
export function constraint<
    TData extends object,
    TKey extends keyof TData,
    TOptions extends { signal?: AbortSignal }
>(fieldId: TKey, values: TData, { signal, ...options }: TOptions): Promise<boolean> {
    if (!isEmpty(values[fieldId])) {
        // @ts-ignore import from js file
        return defaultApiProvider[FETCH_VALIDATE](options, signal)
    }

    return Promise.reject(new Error('is empty value'))
}

/**
 * Валидация того, что integer
 */
export function integer<
    TData extends object,
    TKey extends keyof TData
>(fieldId: TKey, values: TData) {
    const value = values[fieldId]

    return value && !isNaN(Number(value)) && isNumber(Number(value))
}

/**
 * Валидация минимальной длины
 */
export function minLength<
    TData extends object,
    TKey extends keyof TData,
    TOptions extends { min: number }
>(fieldId: TKey, values: TData, options: TOptions) {
    const value = values[fieldId]

    return typeof value === 'string' && value.length > options.min
}

/**
 * Валиадация максимальной длины
 */
export function maxLength<
    TData extends object,
    TKey extends keyof TData,
    TOptions extends { max: number }
>(fieldId: TKey, values: TData, options: TOptions) {
    const value = values[fieldId]

    return typeof value === 'string' && value.length < options.max
}

export const presets: Record<ValidationTypes, ValidateFunction> = {
    [ValidationTypes.condition]: condition,
    [ValidationTypes.email]: email,
    [ValidationTypes.required]: required,
    [ValidationTypes.constraint]: constraint,
    [ValidationTypes.integer]: integer,
    [ValidationTypes.minLength]: minLength,
    [ValidationTypes.maxLength]: maxLength,
}
