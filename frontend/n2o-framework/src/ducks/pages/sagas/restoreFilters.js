import { call, put, select, fork, race, take } from 'redux-saga/effects'
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
import filtersCache from '../utils/FiltersCache'
import { resetPage } from '../store'

import { mappingUrlToRedux } from './mapUrlToRedux'
import { flowDefaultModels } from './defaultModels'

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
    withoutCache,
) {
    const rootPageId = yield select(rootPageSelector)
    const newQuery = yield call(
        generateNewQuery,
        pageId,
        query,
    )

    if (newQuery && pageId === rootPageId) {
        yield put(replace({ search: queryString.stringify(newQuery), state: { silent: true } }))

        if (!filtersCache.checkCacheExists(pageId) && !withoutCache) {
            filtersCache.addCache(pageId, newQuery)
        } else if (!withoutCache) {
            filtersCache.setQueryTo(pageId, newQuery)
            filtersCache.clearFromPage(pageId)
        }
    }
}

export function* mapPageQueryToUrl(pageId, defaultModels) {
    const state = yield select()
    const routes = makePageRoutesByIdSelector(pageId)(state)

    if (!filtersCache.checkCacheExists(pageId)) {
        yield race([call(flowDefaultModels, defaultModels), take(resetPage.type)])
    } else {
        yield call(mapQueryToUrl, pageId, filtersCache.getCacheByPageId(pageId)?.query)
    }

    yield fork(mappingUrlToRedux, routes)
}
