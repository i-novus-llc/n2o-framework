import isMap from 'lodash/isMap'
import isPlainObject from 'lodash/isPlainObject'
import isFunction from 'lodash/isFunction'
import pickBy from 'lodash/pickBy'
import isObject from 'lodash/isObject'
import isEmpty from 'lodash/isEmpty'
import isNil from 'lodash/isNil'
import defaultTo from 'lodash/defaultTo'
import flatten from 'flat'
import invariant from 'invariant'
import queryString from 'query-string'

import request from '../utils/request'
import { generateFlatQuery } from '../tools/helpers'

export const API_PREFIX = 'n2o'
export const BASE_PATH_METADATA = '/page'
export const BASE_PATH_DATA = '/data/'
export const BASE_PATH_VALIDATION = '/validation'
export const BASE_PATH_CONFIG = '/config'
export const BASE_PATH_LOCALE_CHANGE = '/locale'

export const FETCH_APP_CONFIG = 'FETCH_APP_CONFIG'
export const FETCH_PAGE_METADATA = 'FETCH_PAGE_METADATA'
export const FETCH_WIDGET_DATA = 'FETCH_WIDGET_DATA'
export const FETCH_INVOKE_DATA = 'FETCH_INVOKE_DATA'
export const FETCH_VALIDATE = 'FETCH_VALIDATE'
export const FETCH_VALUE = 'FETCH_VALUE'
export const CHANGE_LOCALE = 'CHANGE_LOCALE'

/**
 * Удаляет все пустые значения в параметрах запроса
 * @param obj
 * @returns {*}
 */
function clearEmptyParams(obj) {
    return pickBy(obj, (value) => {
        if (isNil(value)) { return false }
        if (isObject(value)) { return !isEmpty(value) }

        return true
    })
}

/**
 * функция генератор Api Provider
 * так как api - Object Literals
 * можно расширять его
 * @param api
 * @returns {function(foo: Object, bar: string)}
 */
export function handleApi(api) {
    invariant(
        isPlainObject(api) || isMap(api),
        'Api Provider: Обработчик api должен быть простым объектов.',
    )

    return (type, options, abortSignal) => {
        const apiFn = api[type]

        invariant(
            isFunction(apiFn),
            'Api Provider: Не найден обработчик для заданого типа.',
        )

        return apiFn.call(undefined, options, abortSignal)
    }
}

/**
 * Стандартный api provider
 */
export const defaultApiProvider = {
    [FETCH_APP_CONFIG]: (options, abortSignal) => request(
        [
            API_PREFIX,
            BASE_PATH_CONFIG,
            '?',
            queryString.stringify(
                flatten(clearEmptyParams(options), { safe: true }),
            ),
        ].join(''),
        { signal: abortSignal },
    ),
    [FETCH_PAGE_METADATA]: (options, abortSignal) => request([API_PREFIX, BASE_PATH_METADATA, options.pageUrl].join(''), {
        headers: options.headers,
        signal: abortSignal,
    }),
    [FETCH_WIDGET_DATA]: (options, abortSignal) => request(
        [
            options.basePath,
            '?',
            queryString.stringify(
                flatten(clearEmptyParams(options.baseQuery), { safe: true }),
            ),
        ].join(''),
        {
            headers: defaultTo(options.headers, {}),
            signal: abortSignal,
        },
    ),
    [FETCH_INVOKE_DATA]: (options, abortSignal) => request(
        [
            options.basePath,
            '?',
            queryString.stringify(
                flatten(clearEmptyParams(options.baseQuery), { safe: true }),
            ),
        ].join(''),
        {
            method: options.baseMethod || 'POST',
            headers: {
                'Content-Type': 'application/json',
                ...defaultTo(options.headers, {}),
            },
            body: JSON.stringify(options.model || {}),
            signal: abortSignal,
        },
    ),
    [FETCH_VALIDATE]: (options, abortSignal) => request(
        [
            API_PREFIX,
            BASE_PATH_VALIDATION,
            '?',
            queryString.stringify(
                flatten(clearEmptyParams(options), { safe: true }),
            ),
        ].join(''),
        { signal: abortSignal },
    ).catch((error) => {
        // eslint-disable-next-line no-console
        console.error(error)
    }),
    [FETCH_VALUE]: ({ url, headers }, abortSignal) => request(url, { headers, signal: abortSignal }),
    [CHANGE_LOCALE]: (locale, abortSignal) => request([API_PREFIX, BASE_PATH_LOCALE_CHANGE].join(''), {
        method: 'POST',
        body: JSON.stringify({ locale }),
        signal: abortSignal,
    }),
}

/**
 * Возвращает стандартный api для N2O
 */
export default handleApi(defaultApiProvider)

/**
 * todo: нужно переделать на другую реализацию, эту нельзя кастомить
 * @param options
 * @param settings
 * @param abortSignal
 * @returns {Promise<any | never>}
 */
export function fetchInputSelectData(
    options,
    settings = { apiPrefix: API_PREFIX, basePath: BASE_PATH_DATA },
    abortSignal,
) {
    const { query, headers } = options

    return request(
        [
            settings.apiPrefix,
            settings.basePath,
            generateFlatQuery(query, '', {}, '.'),
        ].join(''),
        {
            headers,
            signal: abortSignal,
        },
    )
}

export function saveFieldData(url, options, abortSignal) {
    return request(
        [
            url,
            '?',
            queryString.stringify(
                flatten(clearEmptyParams(options.baseQuery), { safe: true }),
            ),
        ].join(''),
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(options.body),
            signal: abortSignal,
        },
    )
}
