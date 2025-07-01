import pathToRegexp from 'path-to-regexp'
import urlParse from 'url-parse'
import queryString from 'query-string'
import isEmpty from 'lodash/isEmpty'
import includes from 'lodash/includes'
import each from 'lodash/each'
import isNil from 'lodash/isNil'
import startsWith from 'lodash/startsWith'
import { flatten } from 'flat'

import linkResolver from '../utils/linkResolver'
import { clearEmptyParams } from '../utils/clearEmptyParams'

/**
 * Получение разрешенных параметров dataProvider
 * @param state
 * @param dataProvider
 * @param query
 * @param options
 * @returns
 */

/* FIXME после типизации dataProviderResolver
    нужно поправить все файлы где он используется
    @ts-ignore import from js file */
/**
 * @param {object} state
 * @param {object} dataProvider
 * @param {object} [query]
 * @param {object} [options]
 * @returns {{
 *  baseQuery: Record<string, string|number>,
 *  basePath: string,
 *  queryParams: Record<string, string|number>,
 *  pathParams: Record<string, string|number>,
 *  formParams: Record<string, string|number>,
 *  headersParams: Record<string, string|number>,
 *  url: string
 * }}
 */
export function dataProviderResolver(state, dataProvider, query, options) {
    const {
        url,
        pathMapping,
        queryMapping,
        headersMapping,
        formMapping,
        size,
        evalContext = {},
    } = dataProvider
    const { origin, pathname, query: queryFromUrl = {}, hash } = urlParse(url)
    const pathParams = getParams(pathMapping, state, evalContext)
    const queryParams = getParams(queryMapping, state, evalContext)
    const headersParams = getParams(headersMapping, state, evalContext)
    const formParams = getParams(formMapping, state, evalContext)
    const baseQuery = {
        ...query,
        ...options,
        ...queryParams,
        ...queryString.parse(queryFromUrl),
    }

    const isAbsolutePath = startsWith(url, ':')
    const isRelativePath = startsWith(url, '.')

    let path = (isAbsolutePath || isRelativePath) ? url : pathname

    // если хеш является частью роутинга, то приклеиваем его обратно
    if (hash?.includes('/')) {
        path = `${pathname}${pathname.endsWith('/') ? '' : '/'}${hash}`
    }

    let basePath

    try {
        basePath = pathToRegexp.compile(path)(pathParams)
    } catch (error) {
        if (isEmpty(pathParams)) { throw error }

        throw new Error(`Не удалось сформировать URL для запроса.
        Не переданы обязательные параметры:
        ${Object.entries(pathParams).filter(([, value]) => isNil(value)).map(([key, value]) => `${key}=${value}`).join(', ')}`)
    }

    let compiledUrl = basePath

    if (!isEmpty(queryParams) || !isEmpty(query) || !isEmpty(queryFromUrl) || size) {
        const params = {
            size,
            ...queryParams,
            ...query,
            ...queryString.parse(queryFromUrl),
        }

        compiledUrl = `${compiledUrl}?${queryString
            .stringify(
                flatten(clearEmptyParams(params), { safe: true }),
            )}`
    }

    if (includes(url, origin)) {
        compiledUrl = origin + compiledUrl
        basePath = origin + basePath
    }

    basePath = decodeURIComponent(basePath)
    compiledUrl = decodeURIComponent(compiledUrl)

    return {
        basePath,
        pathParams,
        baseQuery,
        headersParams,
        formParams,
        url: compiledUrl,
    }
}

/**
 * @returns {Record<string, string>}
 */
export function getParams(mapping, state, evalContext = {}) {
    const params = {}

    each(mapping, (options, key) => {
        const value = linkResolver(state, options, evalContext)
        const { required } = options

        if (!isNil(value)) { params[key] = value }

        if (required && isNil(value)) {
            throw new Error(`DataProvider error: "${key}" is required`)
        }
    })

    return params
}
