import { call, select, put } from 'redux-saga/effects'
import { getLocation } from 'connected-react-router'
import queryString from 'query-string'
import { Action } from 'redux'
import isEmpty from 'lodash/isEmpty'
import each from 'lodash/each'
import isObject from 'lodash/isObject'

import { Location, Routes } from './types'

export function* mappingUrlToRedux(routes: Routes) {
    const location: Location = yield select(getLocation)

    if (routes) {
        call(queryMapping, location, routes)
    }
}

export function* queryMapping(location: Location, routes: Routes) {
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
