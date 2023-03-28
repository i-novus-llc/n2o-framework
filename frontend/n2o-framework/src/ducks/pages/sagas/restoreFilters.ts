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

// @ts-ignore import from js file
import { getParams } from '../../../core/dataProviderResolver'
// @ts-ignore import from js file
import { getLocation, rootPageSelector } from '../../global/store'
import { State } from '../../State'
// @ts-ignore import from js file
import { makePageRoutesByIdSelector } from '../selectors'

import { IRoutes } from './types'

export function* generateNewQuery(pageId: string, query?: object | null) {
    const state: State = yield select()
    const routes: IRoutes = makePageRoutesByIdSelector(pageId)(state)

    if (!isEmpty(routes?.queryMapping) || !isEmpty(query)) {
        const location = getLocation(state)

        const nextRoutesQuery: object = getParams(mapValues(routes.queryMapping, 'set'), state)
        const nonNullableNextRoutesQuery: NonNullable<object> = pickBy(nextRoutesQuery, identity)

        const currentQuery: object = queryString.parse(location.search)
        const currentRoutesQuery: object = pick(currentQuery, keys(routes.queryMapping))
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
    pageId: string,
    query?: object | null,
) {
    const rootPageId: string = yield select(rootPageSelector)
    const newQuery: object | false = yield call(
        generateNewQuery,
        pageId,
        query,
    )

    if (newQuery && pageId === rootPageId) {
        yield put(replace({ search: queryString.stringify(newQuery), state: { silent: true } }))
    }
}
