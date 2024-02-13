import pathToRegexp from 'path-to-regexp'
import urlParse from 'url-parse'
import queryString from 'query-string'
import isEmpty from 'lodash/isEmpty'
import includes from 'lodash/includes'
import each from 'lodash/each'
import isNil from 'lodash/isNil'
import startsWith from 'lodash/startsWith'
import flatten from 'flat'

import linkResolver from '../utils/linkResolver'

import { clearEmptyParams } from './api'

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
export function dataProviderResolver(state, dataProvider, query, options) {
    const {
        url,
        pathMapping,
        queryMapping,
        headersMapping,
        formMapping,
        size,
    } = dataProvider
    const { origin, pathname, query: queryFromUrl = {}, hash } = urlParse(url)
    const pathParams = getParams(pathMapping, state)
    const queryParams = getParams(queryMapping, state)
    const headersParams = getParams(headersMapping, state)
    const formParams = getParams(formMapping, state)

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
    if (hash && hash.includes('/')) {
        path = `${pathname}${pathname.endsWith('/') ? '' : '/'}${hash}`
    }

    let basePath = pathToRegexp.compile(path)(pathParams)
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
                flatten(
                    clearEmptyParams(params), { safe: true },
                ),
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
        baseQuery,
        headersParams,
        formParams,
        url: compiledUrl,
    }
}

export function getParams(mapping, state) {
    const params = {}

    each(mapping, (options, key) => {
        const value = linkResolver(state, options)
        const { required } = options

        params[key] = !isNil(value) ? value : undefined

        if (required && isNil(value)) {
            throw new Error(`DataProvider error: "${key}" is required`)
        }
    })

    return params
}
