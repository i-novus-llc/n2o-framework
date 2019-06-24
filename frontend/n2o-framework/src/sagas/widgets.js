import { call, fork, put, select, take, takeEvery } from 'redux-saga/effects';
import {
  isEmpty,
  isEqual,
  isNil,
  pick,
  get,
  mapValues,
  omit,
  keys,
  pickBy,
  identity,
} from 'lodash';
import { reset } from 'redux-form';
import { replace } from 'connected-react-router';
import pathToRegexp from 'path-to-regexp';
import queryString from 'query-string';
import { DATA_REQUEST, DISABLE, RESOLVE } from '../constants/widgets';
import { CLEAR, PREFIXES } from '../constants/models';
import {
  changeCountWidget,
  changePageWidget,
  dataFailWidget,
  dataSuccessWidget,
  resetWidgetState,
  setTableSelectedId,
  setWidgetMetadata,
} from '../actions/widgets';
import { setModel } from '../actions/models';
import {
  makeWidgetByIdSelector,
  makeWidgetDataProviderSelector,
  makeWidgetPageIdSelector,
} from '../selectors/widgets';
import { makePageRoutesByIdSelector } from '../selectors/pages';
import { getLocation, rootPageSelector } from '../selectors/global';
import { makeGetModelByPrefixSelector } from '../selectors/models';
import fetchSaga from './fetch.js';
import { FETCH_WIDGET_DATA } from '../core/api.js';
import { getParams } from '../utils/compileUrl';
import { generateErrorMeta } from '../utils/generateErrorMeta';
import { id } from '../utils/id';

/**
 * сайд-эффекты на экшен DATA_REQUEST
 */
function* getData() {
  let lastQuery = {};
  const isQueryEqual = (id, newPath, newQuery) => {
    let res = true;
    const lq = lastQuery[id];
    if (lq) {
      res = isEqual(lq.path, newPath) && isEqual(lq.query, newQuery);
    }
    lastQuery[id] = { path: newPath, query: { ...newQuery } };
    return res;
  };
  while (true) {
    const {
      payload: { widgetId, options },
    } = yield take(DATA_REQUEST);

    yield fork(handleFetch, widgetId, options, isQueryEqual);
  }
}

/**
 * Подготовка данных
 * @param widgetId
 * @returns {IterableIterator<*>}
 */
export function* prepareFetch(widgetId) {
  const state = yield select();
  const location = yield select(getLocation);
  // selectors options: size, page, filters, sorting
  const widgetState = yield select(makeWidgetByIdSelector(widgetId));
  const currentPageId =
    (yield select(makeWidgetPageIdSelector(widgetId))) ||
    (yield select(rootPageSelector));
  const routes = yield select(makePageRoutesByIdSelector(currentPageId));
  const dataProvider = yield select(makeWidgetDataProviderSelector(widgetId));
  const currentDatasource = yield select(
    makeGetModelByPrefixSelector(PREFIXES.datasource, widgetId)
  );
  return {
    state,
    location,
    widgetState,
    routes,
    dataProvider,
    currentDatasource,
  };
}

export function* routesQueryMapping(state, routes, location) {
  const queryObject = yield call(
    getParams,
    mapValues(routes.queryMapping, 'set'),
    state
  );
  const currentQueryObject = queryString.parse(location.search);
  const pageQueryObject = pick(
    queryString.parse(location.search),
    keys(queryObject)
  );
  if (!isEqual(pickBy(queryObject, identity), pageQueryObject)) {
    const newQuery = queryString.stringify(queryObject);
    const tailQuery = queryString.stringify(
      omit(currentQueryObject, keys(queryObject))
    );
    yield put(
      replace({
        search: newQuery + (tailQuery ? `&${tailQuery}` : ''),
        state: { silent: true },
      })
    );
  }
}

/**
 * Получение basePath и baseQuery
 * @param state
 * @param dataProvider
 * @param widgetState
 * @param options
 * @returns {IterableIterator<*>}
 */
export function* resolveUrl(state, dataProvider, widgetState, options) {
  const pathParams = yield call(getParams, dataProvider.pathMapping, state);
  const basePath = pathToRegexp.compile(dataProvider.url)(pathParams);
  const queryParams = yield call(getParams, dataProvider.queryMapping, state);
  const baseQuery = {
    size: widgetState.size,
    page: get(options, 'page', widgetState.page),
    sorting: widgetState.sorting,
    ...options,
    ...queryParams,
  };
  return {
    basePath,
    baseQuery,
  };
}

export function* setWidgetDataSuccess(
  widgetId,
  widgetState,
  basePath,
  baseQuery,
  currentDatasource
) {
  const data = yield call(fetchSaga, FETCH_WIDGET_DATA, {
    basePath,
    baseQuery,
  });
  if (isEqual(data.list, currentDatasource)) {
    yield put(setModel(PREFIXES.datasource, widgetId, null));
    yield put(setModel(PREFIXES.datasource, widgetId, data.list));
  } else {
    yield put(setModel(PREFIXES.datasource, widgetId, data.list));
  }
  if (isNil(data.list) || isEmpty(data.list)) {
    yield put(setModel(PREFIXES.resolve, widgetId, null));
  }
  yield put(changeCountWidget(widgetId, data.count));
  yield data.page && put(changePageWidget(widgetId, data.page));
  if (data.metadata) {
    yield put(setWidgetMetadata(widgetState.pageId, widgetId, data.metadata));
    yield put(resetWidgetState(widgetId));
  }
  yield put(dataSuccessWidget(widgetId, data));
}

export function getWithoutSelectedId(options, location, selectedId) {
  if (!options) return null;
  else if (!location.pathname.includes(selectedId)) {
    return true;
  }

  return options.withoutSelectedId;
}

export function* handleFetch(widgetId, options, isQueryEqual) {
  try {
    const {
      state,
      location,
      widgetState,
      routes,
      dataProvider,
      currentDatasource,
    } = yield call(prepareFetch, widgetId);
    if (!isEmpty(dataProvider) && dataProvider.url) {
      const { basePath, baseQuery } = yield call(
        resolveUrl,
        state,
        dataProvider,
        widgetState,
        options
      );
      const withoutSelectedId = getWithoutSelectedId(
        options,
        location,
        widgetState.selectedId
      );
      if (withoutSelectedId || !isQueryEqual(widgetId, basePath, baseQuery)) {
        yield put(setTableSelectedId(widgetId, null));
      } else if (!withoutSelectedId && widgetState.selectedId) {
        baseQuery.selectedId = widgetState.selectedId;
      }
      if (routes && routes.queryMapping) {
        yield* routesQueryMapping(state, routes, location);
      }
      yield call(
        setWidgetDataSuccess,
        widgetId,
        widgetState,
        basePath,
        baseQuery,
        currentDatasource
      );
    } else {
      yield put(dataFailWidget(widgetId));
    }
  } catch (err) {
    console.error(`JS Error: Widget(${widgetId}) fetch saga. ${err.message}`);
    yield put(
      dataFailWidget(
        widgetId,
        err,
        err.json && err.json.meta
          ? err.json.meta
          : {
              meta: generateErrorMeta({
                id: id(),
                text: `Произошла внутренняя ошибка`,
                stacktrace: err.stack,
                closeButton: true,
              }),
            }
      )
    );
  }
}

export function* runResolve(action) {
  const { widgetId, model } = action.payload;
  try {
    yield put(setModel(PREFIXES.resolve, widgetId, model));
  } catch (err) {}
}

export function* clearForm(action) {
  yield put(reset(action.payload.key));
}

export function* clearOnDisable(action) {
  const { widgetId } = action.payload;
  yield put(setModel(PREFIXES.datasource, widgetId, null));
  yield put(changeCountWidget(widgetId, 0));
}

/**
 * Сайд-эффекты для виджет редюсера
 * @ignore
 */
export const widgetsSagas = [
  fork(getData),
  takeEvery(CLEAR, clearForm),
  takeEvery(RESOLVE, runResolve),
  takeEvery(DISABLE, clearOnDisable),
];
