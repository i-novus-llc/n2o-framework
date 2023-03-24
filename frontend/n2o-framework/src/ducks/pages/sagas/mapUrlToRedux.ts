import { all, call, select, put } from 'redux-saga/effects'
import { getLocation } from 'connected-react-router'
import { matchPath } from 'react-router-dom'
import queryString from 'query-string'
import { Action } from 'redux'
import compact from 'lodash/compact'
import head from 'lodash/head'
import map from 'lodash/map'
import isEmpty from 'lodash/isEmpty'
import each from 'lodash/each'
import isObject from 'lodash/isObject'

import { Location, IRoutes } from './types'

export function* mappingUrlToRedux(routes: IRoutes) {
    const location: Location = yield select(getLocation)

    if (routes) {
        yield all([
            call(pathMapping, location, routes),
            call(queryMapping, location, routes),
        ])
    }
}

export function* pathMapping(location: Location, routes: IRoutes) {
    const parsedPath = head(
        compact(map(routes.list, route => matchPath(location.pathname, route))),
    )

    if (parsedPath && !isEmpty(parsedPath.params)) {
        const actions = map(parsedPath.params, (value, key) => ({
            ...routes.pathMapping[key],
            ...applyPlaceholders(key, routes.pathMapping[key], parsedPath.params),
        }))

        for (const action of actions) {
            yield put(action as never)
        }
    }
}

export function* queryMapping(location: Location, routes: IRoutes) {
    const parsedQuery: Record<string, unknown> = queryString.parse(location.search)

    if (!isEmpty(parsedQuery)) {
        const actions: Action[] = []

        for (const [key] of Object.entries(parsedQuery)) {
            const mapping = routes.queryMapping[key]

            if (mapping) {
                const action: Action = {
                    ...mapping.get,
                    ...applyPlaceholders(key, mapping.get, parsedQuery),
                }

                actions.push(action)
            }
        }

        for (const action of actions) {
            yield put(action)
        }
    }
}

export function applyPlaceholders(
    key: string,
    obj: Action | object,
    placeholders: Record<string, unknown>,
): Record<string, unknown> {
    const newObj: Record<string, unknown> = {}

    each(obj, (v, k) => {
        if (isObject(v)) {
            newObj[k] = applyPlaceholders(key, v as object, placeholders)
        } else if (v === '::self') {
            newObj[k] = placeholders[key]
        } else if (placeholders[(v as string).substr(1)]) {
            newObj[k] = placeholders[(v as string).substr(1)]
        } else {
            newObj[k] = (obj as Record<string, unknown>)[k]
        }
    })

    return newObj
}
