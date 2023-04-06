import { all, call, select, put } from 'redux-saga/effects'
import { getLocation } from 'connected-react-router'
import { matchPath } from 'react-router-dom'
import queryString from 'query-string'
import compact from 'lodash/compact'
import head from 'lodash/head'
import map from 'lodash/map'
import isEmpty from 'lodash/isEmpty'
import each from 'lodash/each'
import isObject from 'lodash/isObject'

export function* mappingUrlToRedux(routes) {
    const location = yield select(getLocation)

    if (routes) {
        yield all([
            call(pathMapping, location, routes),
            call(queryMapping, location, routes),
        ])
    }
}

export function* pathMapping(location, routes) {
    const parsedPath = head(
        compact(map(routes.list, route => matchPath(location.pathname, route))),
    )

    if (parsedPath && !isEmpty(parsedPath.params)) {
        const actions = map(parsedPath.params, (value, key) => ({
            ...routes.pathMapping[key],
            ...applyPlaceholders(key, routes.pathMapping[key], parsedPath.params),
        }))

        for (const action of actions) {
            yield put(action)
        }
    }
}

export function* queryMapping(location, routes) {
    const parsedQuery = queryString.parse(location.search)

    if (!isEmpty(parsedQuery)) {
        const actions = []

        for (const [key] of Object.entries(parsedQuery)) {
            const mapping = routes.queryMapping[key]

            if (mapping) {
                const action = {
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
    key,
    obj,
    placeholders,
) {
    const newObj = {}

    each(obj, (v, k) => {
        if (isObject(v)) {
            newObj[k] = applyPlaceholders(key, v, placeholders)
        } else if (v === '::self') {
            newObj[k] = placeholders[key]
        } else if (placeholders[(v).substr(1)]) {
            newObj[k] = placeholders[(v).substr(1)]
        } else {
            newObj[k] = (obj)[k]
        }
    })

    return newObj
}
