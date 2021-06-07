import {
    all,
    call,
    put,
    race,
    select,
    take,
    takeEvery,
    throttle,
    fork,
    actionChannel,
    cancelled,
} from 'redux-saga/effects'
import { matchPath } from 'react-router-dom'
import { batchActions } from 'redux-batched-actions'
import compact from 'lodash/compact'
import each from 'lodash/each'
import head from 'lodash/head'
import isEmpty from 'lodash/isEmpty'
import isObject from 'lodash/isObject'
import map from 'lodash/map'
import clone from 'lodash/clone'
import isEqual from 'lodash/isEqual'
import get from 'lodash/get'
import set from 'lodash/set'
import reduce from 'lodash/reduce'
import pickBy from 'lodash/pickBy'
import { getAction, getLocation } from 'connected-react-router'
import queryString from 'query-string'

import { MAP_URL, METADATA_REQUEST, RESET } from '../constants/pages'
import { metadataFail, metadataSuccess, setStatus } from '../actions/pages'
import {
    combineModels,
    setModel,
    copyModel,
    syncModel,
    removeModel,
    updateModel,
    updateMapModel,
} from '../ducks/models/store'
import { changeRootPage } from '../actions/global'
import { destroyOverlay } from '../ducks/overlays/store'
import { rootPageSelector } from '../selectors/global'
import { makePageRoutesByIdSelector } from '../selectors/pages'
import { FETCH_PAGE_METADATA } from '../core/api'
import { dataProviderResolver } from '../core/dataProviderResolver'
import linkResolver from '../utils/linkResolver'

import fetchSaga from './fetch'

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
        yield put(
            batchActions(
                map(parsedPath.params, (value, key) => ({
                    ...routes.pathMapping[key],
                    ...applyPlaceholders(key, routes.pathMapping[key], parsedPath.params),
                })),
            ),
        )
    }
}

export function* queryMapping(location, routes) {
    const parsedQuery = queryString.parse(location.search)

    if (!isEmpty(parsedQuery)) {
        yield put(
            batchActions(
                compact(
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
                ),
            ),
        )
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
        yield fork(watcherDefaultModels, metadata.models)
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
                    label: err.status ? err.status : 'Ошибка',
                    text: err.message,
                    closeButton: false,
                    severity: 'danger',
                },
                err.json && err.json.meta ? err.json.meta : {},
            ),
        )
    }
}

/**
 * Дополнительная функция для observeModels.
 * резолвит и сравнивает модели из стейта и резолв модели.
 * @param models - модели для резолва
 * @param stateModels - модели из стейта
 * @returns {*}
 */
export function compareAndResolve(models, stateModels) {
    return reduce(
        models,
        (acc, value, path) => {
            const resolveValue = linkResolver(stateModels, value)
            const stateValue = get(stateModels, path)

            if (!isEqual(stateValue, resolveValue)) {
                return set(clone(acc), path, resolveValue)
            }

            return acc
        },
        {},
    )
}

/**
 * Для закрытия канала используем race
 * c экшеном сброса (обнуление) метаданных страницы
 * @param config - конфиг для моделей по умолчанию, который прокидывается в сагу
 */
export function* watcherDefaultModels(config) {
    yield race([call(flowDefaultModels, config), take(RESET)])
}

/**
 * Сага для первоначальной установки моделей по умолчанию
 * и подписка на изменения через канал
 * @param config - конфиг для моделей по умолчанию
 * @returns {boolean}
 */
// eslint-disable-next-line consistent-return
export function* flowDefaultModels(config) {
    if (isEmpty(config)) { return false }
    const state = yield select()
    const initialModels = yield call(compareAndResolve, config, state)

    if (!isEmpty(initialModels)) {
        yield put(combineModels(initialModels))
    }
    const observableModels = pickBy(
        config,
        item => !!item.observe && !!item.link,
    )

    if (!isEmpty(observableModels)) {
        const modelsChan = yield actionChannel([
            setModel.type,
            copyModel.type,
            syncModel.type,
            removeModel.type,
            updateModel.type,
            updateMapModel.type,
        ])

        try {
            while (true) {
                const oldState = yield select()

                yield take(modelsChan)
                const newState = yield select()
                const changedModels = pickBy(
                    observableModels,
                    cfg => !isEqual(get(oldState, cfg.link), get(newState, cfg.link)),
                )
                const newModels = yield call(
                    compareAndResolve,
                    changedModels,
                    newState,
                )

                if (!isEmpty(newModels)) {
                    yield put(combineModels(newModels))
                }
            }
        } finally {
            if (yield cancelled()) {
                modelsChan.close()
            }
        }
    }
}

/**
 * Сайд-эффекты для page редюсера
 * @ignore
 */
export default apiProvider => [
    takeEvery(METADATA_REQUEST, getMetadata, apiProvider),
    throttle(500, MAP_URL, processUrl),
]
