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
import { getLocation } from '../../global/selectors'
import { State } from '../../State'
import { makePageByIdSelector, makePageRoutesByIdSelector } from '../selectors'
import { Page } from '../Pages'

export function* generateNewQuery(pageId: string, query?: object | null) {
    const state: State = yield select()
    const routes = makePageRoutesByIdSelector(pageId)(state)

    if (!isEmpty(routes?.queryMapping) || !isEmpty(query)) {
        const location: { search: string } = getLocation(state)

        const nextRoutesQuery: object = getParams(mapValues(routes?.queryMapping, 'set'), state)
        const nonNullableNextRoutesQuery: NonNullable<object> = pickBy(nextRoutesQuery, identity)

        const currentQuery: object = queryString.parse(location.search)
        const currentRoutesQuery: object = pick(currentQuery, keys(routes?.queryMapping))
        const newQuery = {
            ...omit(currentQuery, keys(routes?.queryMapping)),
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
    const page: Page | null = yield select(makePageByIdSelector(pageId))

    if (!page) { return }

    const { rootPage } = page
    const newQuery: object | false = yield call(
        generateNewQuery,
        pageId,
        query,
    )

    if (newQuery && rootPage) {
        yield put(replace({ search: queryString.stringify(newQuery), state: { silent: true } }))
    }
}
