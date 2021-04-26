import pathToRegexp from 'path-to-regexp'
import urlParse from 'url-parse'
import queryString from 'query-string'
import isEmpty from 'lodash/isEmpty'
import includes from 'lodash/includes'
import each from 'lodash/each'
import isNil from 'lodash/isNil'

import linkResolver from '../utils/linkResolver'

/**
 * Получение разрешенных параметров dataProvider
 * @param state
 * @param dataProvider
 * @param query
 * @param options
 * @returns
 */
export function dataProviderResolver(state, dataProvider, query, options) {
    const {
        url,
        pathMapping,
        queryMapping,
        headersMapping,
        formMapping,
    } = dataProvider
    const { origin, pathname, query: queryFromUrl = {} } = urlParse(url)

    const pathParams = getParams(pathMapping, state)
    const queryParams = getParams(queryMapping, state)
    const headersParams = getParams(headersMapping, state)
    const formParams = getParams(formMapping, state)
    const baseQuery = { ...query, ...options, ...queryParams }
    let basePath = pathToRegexp.compile(pathname)(pathParams)
    let compiledUrl = basePath

    if (!isEmpty(queryParams) || !isEmpty(query) || !isEmpty(queryFromUrl)) {
        compiledUrl = `${compiledUrl}?${queryString.stringify({
            ...queryParams,
            ...query,
            ...queryString.parse(queryFromUrl),
        })}`
    }

    if (includes(url, origin)) {
        compiledUrl = origin + compiledUrl
        basePath = origin + basePath
    }

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
        params[key] = !isNil(value) ? value : undefined
    })
    return params
}
