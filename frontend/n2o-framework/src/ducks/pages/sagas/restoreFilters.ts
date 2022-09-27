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

// @ts-ignore import from js file
import { getParams } from '../../../core/dataProviderResolver'
// @ts-ignore import from js file
import { getLocation } from '../../global/store'
import { State } from '../../State'
// @ts-ignore import from js file
import { makePageRoutesByIdSelector } from '../selectors'
import filtersCache from '../utils/FiltersCache'
import { DefaultModels } from '../../models/Models'
// @ts-ignore import from js file
import { resetPage } from '../store'

import { mappingUrlToRedux } from './mapUrlToRedux'
import { IRoutes } from './types'
import { flowDefaultModels } from './defaultModels'

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
    withoutCache?: boolean,
) {
    const newQuery: object | false = yield call(
        generateNewQuery,
        pageId,
        query,
    )

    if (newQuery) {
        yield put(replace({ search: queryString.stringify(newQuery), state: { silent: true } }))

        if (!filtersCache.checkCacheExists(pageId) && !withoutCache) {
            filtersCache.addCache(pageId, newQuery)
        } else if (!withoutCache) {
            filtersCache.setQueryTo(pageId, newQuery)
            filtersCache.clearFromPage(pageId)
        }
    }
}

export function* mapPageQueryToUrl(pageId: string, defaultModels: DefaultModels) {
    const state: State = yield select()
    const routes: IRoutes = makePageRoutesByIdSelector(pageId)(state)

    if (!filtersCache.checkCacheExists(pageId)) {
        yield race([call(flowDefaultModels, defaultModels), take(resetPage.type)])
    } else {
        yield call(mapQueryToUrl, pageId, filtersCache.getCacheByPageId(pageId)?.query)
    }

    yield fork(mappingUrlToRedux, routes)
}
