import { call, put, select } from 'redux-saga/effects'
import queryString from 'query-string'
import isEmpty from 'lodash/isEmpty'
import mapValues from 'lodash/mapValues'
import keys from 'lodash/keys'
import isEqual from 'lodash/isEqual'
import pickBy from 'lodash/pickBy'
import pick from 'lodash/pick'
import identity from 'lodash/identity'
import omit from 'lodash/omit'
import { replace } from 'connected-react-router'

import { getParams } from '../../../core/dataProviderResolver'
import { getLocation, rootPageSelector } from '../../global/store'
import { makePageRoutesByIdSelector } from '../selectors'

export function* generateNewQuery(pageId, query) {
    const state = yield select()
    const routes = makePageRoutesByIdSelector(pageId)(state)

    if (!isEmpty(routes?.queryMapping) || !isEmpty(query)) {
        const location = getLocation(state)

        const nextRoutesQuery = getParams(mapValues(routes.queryMapping, 'set'), state)
        const nonNullableNextRoutesQuery = pickBy(nextRoutesQuery, identity)

        const currentQuery = queryString.parse(location.search)
        const currentRoutesQuery = pick(currentQuery, keys(routes.queryMapping))
        const newQuery = {
            ...omit(currentQuery, keys(routes.queryMapping)),
            ...nonNullableNextRoutesQuery,
            ...(query || {}),
        }

        if (
            !isEqual(nonNullableNextRoutesQuery, currentRoutesQuery) ||
            !isEqual(currentRoutesQuery, newQuery)
        ) {
            return newQuery
        }
    }

    return false
}

export function* mapQueryToUrl(
    pageId,
    query,
) {
    const rootPageId = yield select(rootPageSelector)
    const newQuery = yield call(
        generateNewQuery,
        pageId,
        query,
    )

    if (newQuery && pageId === rootPageId) {
        yield put(replace({ search: queryString.stringify(newQuery), state: { silent: true } }))
    }
}
