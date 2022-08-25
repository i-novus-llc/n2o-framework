import { put, select } from 'redux-saga/effects'
import mapValues from 'lodash/mapValues'
import queryString from 'query-string'
import pick from 'lodash/pick'
import keys from 'lodash/keys'
import isEqual from 'lodash/isEqual'
import pickBy from 'lodash/pickBy'
import identity from 'lodash/identity'
import omit from 'lodash/omit'
import { replace } from 'connected-react-router'

// @ts-ignore import from js file
import { getParams } from '../../../../core/dataProviderResolver'
// @ts-ignore import from js file
import { getLocation, rootPageSelector } from '../../../global/store'
// @ts-ignore import from js file
import { makePageRoutesByIdSelector } from '../../../pages/selectors'
import { State } from '../../../State'

interface IRoutes {
    queryMapping?: object
}

export function* routesQueryMapping(
    pageId: string | null,
    routes: IRoutes | undefined,
    query: object = {},
) {
    const state: State = yield select()
    const currentPageId: string = pageId || rootPageSelector(state)
    const currentRoutes: IRoutes = routes || makePageRoutesByIdSelector(currentPageId)(state)

    if (currentRoutes?.queryMapping) {
        const location = getLocation(state)

        const queryObject: object = getParams(mapValues(currentRoutes.queryMapping, 'set'), state)
        const currentQueryObject: object = { ...queryString.parse(location.search), ...query }
        const pageQueryObject: object = pick(currentQueryObject, keys(queryObject))

        if (!isEqual(pickBy(queryObject, identity), pageQueryObject)) {
            const newQuery = queryString.stringify({
                ...queryObject,
                ...omit(currentQueryObject, keys(queryObject)),
                ...query,
            })

            yield put(
                replace({ search: newQuery, state: { silent: true } }),
            )
        }
    }
}
