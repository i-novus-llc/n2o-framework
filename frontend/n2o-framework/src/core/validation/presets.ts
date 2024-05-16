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
import { isEmptyModel } from '../../utils/isEmptyModel'

import type { ValidateFunction, ValidationResult } from './types'
import { ValidationTypes } from './types'

/**
 * Валидация того, что email
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

    if (options.expression && !evalExpression(options.expression, values)) { return true }

    if (isObject(value)) { return !isEmptyModel(value) }
    if (isString(value)) { return value !== '' }

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
    TOptions extends { expression: string }
>(fieldId: string, values: Record<string, unknown>, options: TOptions) {
    return !!evalExpression(options.expression, values)
}

/**
 * Валидация с отправкой запроса на сервер
 */

interface Options {
    signal?: AbortSignal
    datasourceId?: string
    validationKey?: string
    pageUrl?: string | null
}

export function constraint<TData extends object, TKey extends keyof TData>(
    fieldId: TKey, data: TData, { signal, datasourceId, validationKey, pageUrl }: Options,
): Promise<ValidationResult> | boolean {
    const body = { data, datasourceId, validationId: validationKey }

    const pagePath = pageUrl || ''

    return defaultApiProvider[FETCH_VALIDATE]({ baseMethod: 'POST' }, signal, pagePath, body)
        // @ts-ignore import from js file
        .then((response: ValidationResult) => {
            if (isEmpty(response)) { return true }

            return response
        }).catch(() => {
            if (signal) {
                const { aborted } = signal

                return aborted
            }

            return false
        })
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
 * Валидация максимальной длины
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
