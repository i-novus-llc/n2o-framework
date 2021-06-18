import { call, fork, put, select, take, takeEvery } from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import isNil from 'lodash/isNil'
import pick from 'lodash/pick'
import mapValues from 'lodash/mapValues'
import omit from 'lodash/omit'
import keys from 'lodash/keys'
import pickBy from 'lodash/pickBy'
import identity from 'lodash/identity'
import get from 'lodash/get'
import last from 'lodash/last'
import { reset } from 'redux-form'
import { replace } from 'connected-react-router'
import queryString from 'query-string'

import { dataProviderResolver, getParams } from '../core/dataProviderResolver'
import {
    CHANGE_PAGE,
    DATA_REQUEST,
    DISABLE,
    RESOLVE,
} from '../constants/widgets'
import { PREFIXES } from '../ducks/models/constants'
import {
    changeCountWidget,
    changePageWidget,
    dataFailWidget,
    dataSuccessWidget,
    resetWidgetState,
    setWidgetMetadata,
} from '../actions/widgets'
import { removeModel, setModel, clearModel } from '../ducks/models/store'
import {
    makeSelectedIdSelector,
    makeWidgetByIdSelector,
    makeWidgetDataProviderSelector,
    makeWidgetPageIdSelector,
} from '../selectors/widgets'
import { makePageRoutesByIdSelector } from '../selectors/pages'
import { getLocation, rootPageSelector } from '../selectors/global'
import { makeGetModelByPrefixSelector } from '../ducks/models/selectors'
import { FETCH_WIDGET_DATA } from '../core/api'
import { generateErrorMeta } from '../utils/generateErrorMeta'
import { id } from '../utils/id'

import fetchSaga from './fetch'
// eslint-disable-next-line import/no-cycle
import { checkIdBeforeLazyFetch } from './regions'

/**
 * сайд-эффекты на экшен DATA_REQUEST
 */
function* getData() {
    const lastQuery = {}
    const isQueryEqual = (id, newPath, newQuery) => {
        let res = true
        const lq = lastQuery[id]

        if (lq) {
            res = isEqual(lq.path, newPath) && isEqual(lq.query, newQuery)
        }
        lastQuery[id] = { path: newPath, query: { ...newQuery } }

        return res
    }
    let prevSelectedId = null

    while (true) {
        const {
            payload: { widgetId, options },
        } = yield take(DATA_REQUEST)
        const selectedId = yield select(makeSelectedIdSelector(widgetId))

        yield fork(handleFetch, widgetId, options, isQueryEqual, prevSelectedId)

        if (prevSelectedId !== selectedId) {
            prevSelectedId = selectedId
        }
    }
}

/**
 * Подготовка данных
 * @param widgetId
 * @returns {IterableIterator<*>}
 */
export function* prepareFetch(widgetId) {
    const state = yield select()
    const location = yield select(getLocation)
    // selectors options: size, page, filters, sorting
    const widgetState = yield select(makeWidgetByIdSelector(widgetId))
    const currentPageId =
    (yield select(makeWidgetPageIdSelector(widgetId))) ||
    (yield select(rootPageSelector))
    const routes = yield select(makePageRoutesByIdSelector(currentPageId))
    const dataProvider = yield select(makeWidgetDataProviderSelector(widgetId))
    const currentDatasource = yield select(
        makeGetModelByPrefixSelector(PREFIXES.datasource, widgetId),
    )

    return {
        state,
        location,
        widgetState,
        routes,
        dataProvider,
        currentDatasource,
    }
}

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

export function* setWidgetDataSuccess(
    widgetId,
    widgetState,
    resolvedProvider,
    currentDatasource,
) {
    const { basePath, baseQuery, headersParams } = resolvedProvider

    const data = yield call(fetchSaga, FETCH_WIDGET_DATA, {
        basePath,
        baseQuery,
        headers: headersParams,
    })

    if (isEqual(data.list, currentDatasource) && !isEmpty(currentDatasource)) {
        yield put(setModel(PREFIXES.datasource, widgetId, null))
        yield put(setModel(PREFIXES.datasource, widgetId, data.list))
    } else {
        yield put(setModel(PREFIXES.datasource, widgetId, data.list))
    }
    if (isNil(data.list) || isEmpty(data.list)) {
        yield put(setModel(PREFIXES.resolve, widgetId, null))
    }
    yield put(changeCountWidget(widgetId, data.count))
    yield data.page && put(changePageWidget(widgetId, data.page))
    if (data.metadata) {
        yield put(setWidgetMetadata(widgetState.pageId, widgetId, data.metadata))
        yield put(resetWidgetState(widgetId))
    }
    yield put(dataSuccessWidget(widgetId, data))
}

export function getWithoutSelectedId(
    options,
    location,
    selectedId,
    prevSelectedId,
) {
    if (!options || isEmpty(options)) {
        return null
    } if (
        !location.pathname.includes(selectedId) ||
        prevSelectedId === selectedId
    ) {
        return true
    }

    return options.withoutSelectedId
}

export function* handleFetch(widgetId, options, isQueryEqual, prevSelectedId) {
    try {
        const {
            state,
            location,
            widgetState,
            routes,
            dataProvider,
            currentDatasource,
        } = yield call(prepareFetch, widgetId)

        if (!isEmpty(dataProvider) && dataProvider.url) {
            const query = {
                page: get(options, 'page', widgetState.page),
                size: widgetState.size,
                sorting: widgetState.sorting,
            }
            const resolvedProvider = yield call(
                dataProviderResolver,
                state,
                dataProvider,
                query,
                options,
            )

            const withoutSelectedId = getWithoutSelectedId(
                options,
                location,
                widgetState.selectedId,
                prevSelectedId,
            )

            if (
                withoutSelectedId ||
                !isQueryEqual(
                    widgetId,
                    resolvedProvider.basePath,
                    resolvedProvider.baseQuery,
                )
            ) {
                // yield put(setTableSelectedId(widgetId, null));
            } else if (!withoutSelectedId && widgetState.selectedId) {
                resolvedProvider.baseQuery.selectedId = widgetState.selectedId
            }

            if (routes && routes.queryMapping) {
                yield* routesQueryMapping(state, routes, location)
            }

            const { activeWidgetIds, tabsWidgetIds } = yield call(
                checkIdBeforeLazyFetch,
            )

            if (tabsWidgetIds[widgetId] && activeWidgetIds.includes(widgetId)) {
                yield call(
                    setWidgetDataSuccess,
                    widgetId,
                    widgetState,
                    resolvedProvider,
                    currentDatasource,
                )
            } else if (
                !Object.keys(tabsWidgetIds).includes(widgetId) ||
                !tabsWidgetIds[widgetId]
                // eslint-disable-next-line sonarjs/no-duplicated-branches
            ) {
                yield call(
                    setWidgetDataSuccess,
                    widgetId,
                    widgetState,
                    resolvedProvider,
                    currentDatasource,
                )
            }
        } else {
            yield put(dataFailWidget(widgetId))
        }
    } catch (err) {
        // eslint-disable-next-line no-console
        console.error(`JS Error: Widget(${widgetId}) fetch saga. ${err.message}`)
        yield put(
            dataFailWidget(
                widgetId,
                err,
                err.json && err.json.meta
                    ? err.json.meta
                    : {
                        meta: generateErrorMeta({
                            id: id(),
                            text: 'Произошла внутренняя ошибка',
                            stacktrace: err.stack,
                            closeButton: true,
                        }),
                    },
            ),
        )
    }
}

export function* runResolve(action) {
    const { widgetId, model } = action.payload

    try {
        yield put(setModel(PREFIXES.resolve, widgetId, model))
        // eslint-disable-next-line no-empty
    } catch (err) {}
}

export function* clearForm(action) {
    yield put(reset(action.payload.key))
}

export function* clearOnDisable(action) {
    const { widgetId } = action.payload

    yield put(setModel(PREFIXES.datasource, widgetId, null))
    yield put(changeCountWidget(widgetId, 0))
}

const pagesHash = []

function* clearFilters(action) {
    const { widgetId } = action.payload

    if (last(pagesHash) === widgetId) {
        return
    }

    if (pagesHash.includes(widgetId)) {
        const currentPageIndex = pagesHash.indexOf(widgetId)
        const filterResetIds = pagesHash.splice(currentPageIndex + 1)

        for (let index = 0; index < filterResetIds.length; index += 1) {
            const filterResetId = filterResetIds[index]

            yield put(removeModel(PREFIXES.filter, filterResetId))
        }
    } else {
        pagesHash.push(widgetId)
    }
}

/**
 * Сайд-эффекты для виджет редюсера
 * @ignore
 */
export default apiProvider => [
    fork(getData, apiProvider),
    takeEvery(clearModel, clearForm),
    takeEvery(RESOLVE, runResolve),
    takeEvery(DISABLE, clearOnDisable),
    takeEvery(CHANGE_PAGE, clearFilters),
]
