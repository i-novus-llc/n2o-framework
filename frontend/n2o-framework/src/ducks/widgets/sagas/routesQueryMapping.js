import { call, put } from 'redux-saga/effects'
import mapValues from 'lodash/mapValues'
import queryString from 'query-string'
import pick from 'lodash/pick'
import keys from 'lodash/keys'
import isEqual from 'lodash/isEqual'
import pickBy from 'lodash/pickBy'
import identity from 'lodash/identity'
import omit from 'lodash/omit'
import { replace } from 'connected-react-router'

import { getParams } from '../../../core/dataProviderResolver'

export function* routesQueryMapping(state, routes, location) {
    const queryObject = yield call(
        getParams,
        mapValues(routes.queryMapping, 'set'),
        state,
    )
    const currentQueryObject = queryString.parse(location.search)
    const pageQueryObject = pick(
        queryString.parse(location.search),
        keys(queryObject),
    )

    if (!isEqual(pickBy(queryObject, identity), pageQueryObject)) {
        const newQuery = queryString.stringify(queryObject)
        const tailQuery = queryString.stringify(
            omit(currentQueryObject, keys(queryObject)),
        )

        yield put(
            replace({
                search: newQuery + (tailQuery ? `&${tailQuery}` : ''),
                state: { silent: true },
            }),
        )
    }
}
