import {
    all,
    call,
    put,
    select,
    fork,
    takeEvery,
    throttle,
} from 'redux-saga/effects'
import { matchPath } from 'react-router-dom'
import compact from 'lodash/compact'
import each from 'lodash/each'
import head from 'lodash/head'
import isEmpty from 'lodash/isEmpty'
import isObject from 'lodash/isObject'
import map from 'lodash/map'
import get from 'lodash/get'
import { getAction, getLocation, LOCATION_CHANGE } from 'connected-react-router'
import queryString from 'query-string'

import { destroyOverlay } from '../overlays/store'
import { FETCH_PAGE_METADATA } from '../../core/api'
import { dataProviderResolver } from '../../core/dataProviderResolver'
import { setGlobalLoading, changeRootPage, rootPageSelector } from '../global/store'
import fetchSaga from '../../sagas/fetch'

import { resolvePageFilters, watchPageFilters } from './restoreFilters'
import { makePageRoutesByIdSelector } from './selectors'
import { MAP_URL } from './constants'
import {
    metadataFail,
    metadataSuccess,
    setStatus,
    metadataRequest,
} from './store'

/**
 *
 function autoDetectBasePath(pathPattern, pathname) {
  const match = matchPath(pathname, {
    path: pathPattern,
    exact: false,
    strict: false,
  });
  return match && match.url;
}
 */

export function applyPlaceholders(key, obj, placeholders) {
    const newObj = {}

    each(obj, (v, k) => {
        if (isObject(v)) {
            newObj[k] = applyPlaceholders(key, v, placeholders)
        } else if (v === '::self') {
            newObj[k] = placeholders[key]
        } else if (placeholders[v.substr(1)]) {
            newObj[k] = placeholders[v.substr(1)]
        } else {
            newObj[k] = obj[k]
        }
    })

    return newObj
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
        const actions = compact(
            map(parsedQuery, (value, key) => (
                routes.queryMapping[key] && {
                    ...get(routes, `queryMapping[${key}].get`, {}),
                    ...applyPlaceholders(
                        key,
                        get(routes, `queryMapping[${key}].get`, {}),
                        parsedQuery,
                    ),
                }
            )),
        )

        for (const action of actions) {
            yield put(action)
        }
    }
}

export function* mappingUrlToRedux(routes) {
    const location = yield select(getLocation)

    if (routes) {
        yield all([
            call(pathMapping, location, routes),
            call(queryMapping, location, routes),
        ])
    }
    // TODO: исправить сброс роутинга до базового уровня
    // try {
    //   const firstRoute = (routes && routes.list && routes.list[0]) || {};
    //   const basePath = autoDetectBasePath(firstRoute.path, location.pathname);
    //   if (!firstRoute.isOtherPage && location.pathname !== basePath) {
    //     yield put(
    //       replace({
    //         pathname: basePath,
    //         search: location.search,
    //         state: { silent: true },
    //       })
    //     );
    //   }
    // } catch (e) {
    //   console.error(`Ошибка автоматического определения базового роута.`);
    //   console.error(e);
    // }
}

export function* processUrl() {
    try {
        const location = yield select(getLocation)
        const pageId = yield select(rootPageSelector)
        const routes = yield select(makePageRoutesByIdSelector(pageId))
        const routerAction = yield select(getAction)

        if (routerAction !== 'POP' && !(location.state && location.state.silent)) {
            yield call(mappingUrlToRedux, routes)
        }
    } catch (err) {
        // eslint-disable-next-line no-console
        console.error(err)
    }
}

/**
 * сага, фетчит метадату
 * @param apiProvider
 * @param action
 */
export function* getMetadata(apiProvider, action) {
    const { pageId, rootPage, pageUrl, mapping } = action.payload
    let url = pageUrl

    try {
        yield put(setGlobalLoading(true))

        const { search } = yield select(getLocation)
        let resolveProvider = {}

        if (!isEmpty(mapping)) {
            const state = yield select()
            const extraQueryParams = rootPage && queryString.parse(search)

            resolveProvider = dataProviderResolver(
                state,
                { url, ...mapping },
                extraQueryParams,
            )

            url = resolveProvider.url
        } else if (rootPage) {
            url += search
        }
        const metadata = yield call(
            fetchSaga,
            FETCH_PAGE_METADATA,
            { pageUrl: url, headers: resolveProvider.headersParams },
            apiProvider,
        )

        yield call(mappingUrlToRedux, metadata.routes)

        if (rootPage) {
            yield put(changeRootPage(metadata.id))
            yield put(destroyOverlay())
        }

        yield fork(resolvePageFilters, pageUrl, metadata.id, metadata.routes, metadata.models)
        yield put(setStatus(metadata.id, 200))
        yield put(metadataSuccess(metadata.id, metadata))
    } catch (err) {
        if (err && err.status) {
            yield put(setStatus(pageId, err.status))
        }

        if (rootPage) {
            yield put(changeRootPage(pageId))
        }

        yield put(
            metadataFail(
                pageId,
                {
                    title: err.status ? err.status : 'Ошибка',
                    status: err.status,
                    text: err.message,
                    closeButton: false,
                    severity: 'danger',
                },
                err.json && err.json.meta ? err.json.meta : {},
            ),
        )
    } finally {
        yield put(setGlobalLoading(false))
    }
}

/**
 * Сайд-эффекты для page редюсера
 * @ignore
 */
export default apiProvider => [
    takeEvery(metadataRequest, getMetadata, apiProvider),
    throttle(500, MAP_URL, processUrl),
    takeEvery(LOCATION_CHANGE, watchPageFilters),
]
