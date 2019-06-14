import { delay } from 'redux-saga';
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
} from 'redux-saga/effects';
import { matchPath } from 'react-router';
import { batchActions } from 'redux-batched-actions';
import {
  compact,
  each,
  head,
  isEmpty,
  isObject,
  map,
  clone,
  isEqual,
  has,
  findLast,
  get,
  set,
  reduce,
  pickBy,
} from 'lodash';
import {
  getAction,
  getLocation,
  LOCATION_CHANGE,
  replace,
  push,
} from 'connected-react-router';
import queryString from 'query-string';
import {
  SET,
  COPY,
  SYNC,
  REMOVE,
  UPDATE,
  UPDATE_MAP,
} from '../constants/models';
import { MAP_URL, METADATA_REQUEST, RESET } from '../constants/pages';
import { metadataFail, metadataSuccess } from '../actions/pages';
import { combineModels, updateModel } from '../actions/models';
import { changeRootPage } from '../actions/global';
import { rootPageSelector } from '../selectors/global';
import { makePageRoutesByIdSelector } from '../selectors/pages';
import { modelsSelector } from '../selectors/models';
import fetchSaga from './fetch.js';
import { FETCH_PAGE_METADATA } from '../core/api.js';
import compileUrl from '../utils/compileUrl';
import linkResolver from '../utils/linkResolver';
import configureErrorPages from '../components/errors';
import history from '../history';

const errorPages = configureErrorPages();

function autoDetectBasePath(pathPattern, pathname) {
  const match = matchPath(pathname, {
    path: pathPattern,
    exact: false,
    strict: false,
  });
  return match && match.url;
}

function applyPlaceholders(key, obj, placeholders) {
  const newObj = {};
  each(obj, (v, k) => {
    if (isObject(v)) {
      newObj[k] = applyPlaceholders(key, v, placeholders);
    } else if (v === '::self') {
      newObj[k] = placeholders[key];
    } else if (placeholders[v.substr(1)]) {
      newObj[k] = placeholders[v.substr(1)];
    } else {
      newObj[k] = obj[k];
    }
  });
  return newObj;
}

function* pathMapping(location, routes) {
  const parsedPath = head(
    compact(map(routes.list, route => matchPath(location.pathname, route)))
  );
  if (parsedPath && !isEmpty(parsedPath.params)) {
    yield put(
      batchActions(
        map(parsedPath.params, (value, key) => ({
          ...routes.pathMapping[key],
          ...applyPlaceholders(key, routes.pathMapping[key], parsedPath.params),
        }))
      )
    );
  }
}

function* queryMapping(location, routes) {
  const parsedQuery = queryString.parse(location.search);
  if (!isEmpty(parsedQuery)) {
    yield put(
      batchActions(
        compact(
          map(parsedQuery, (value, key) => {
            return (
              routes.queryMapping[key] && {
                ...routes.queryMapping[key].get,
                ...applyPlaceholders(
                  key,
                  routes.queryMapping[key].get,
                  parsedQuery
                ),
              }
            );
          })
        )
      )
    );
  }
}

function* mappingUrlToRedux(routes) {
  const location = yield select(getLocation);
  if (routes) {
    yield all([
      call(pathMapping, location, routes),
      call(queryMapping, location, routes),
    ]);
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

function* processUrl() {
  try {
    const location = yield select(getLocation);
    const pageId = yield select(rootPageSelector);
    const routes = yield select(makePageRoutesByIdSelector(pageId));
    const routerAction = yield select(getAction);
    if (routerAction !== 'POP' && !(location.state && location.state.silent)) {
      yield call(mappingUrlToRedux, routes);
    }
  } catch (err) {
    console.error(err);
  }
}

/**
 * сага, фетчит метадату
 * @param action
 */
function* getMetadata(action) {
  let { pageId, rootPage, pageUrl, mapping } = action.payload;
  try {
    const { search } = yield select(getLocation);
    if (!isEmpty(mapping)) {
      const state = yield select();
      const extraQueryParams = rootPage && queryString.parse(search);
      pageUrl = compileUrl(pageUrl, mapping, state, { extraQueryParams });
    } else if (rootPage) {
      pageUrl = pageUrl + search;
    }
    const metadata = yield call(fetchSaga, FETCH_PAGE_METADATA, { pageUrl });

    yield call(mappingUrlToRedux, metadata.routes);
    if (rootPage) {
      yield put(changeRootPage(metadata.id));
    }
    yield fork(watcherDefaultModels, metadata.models);
    yield put(metadataSuccess(metadata.id, metadata));
  } catch (err) {
    if (err && err.status) {
      const page = findLast(errorPages, ['status', err.status]);
      if (page) {
        return;
      }
    }

    if (rootPage) {
      yield put(changeRootPage(pageId));
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
        err.json && err.json.meta ? err.json.meta : {}
      )
    );
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
      const resolveValue = linkResolver(stateModels, value);
      const stateValue = get(stateModels, path);

      if (!isEqual(stateValue, resolveValue)) {
        return set(clone(acc), path, resolveValue);
      }
      return acc;
    },
    {}
  );
}

/**
 * Для закрытия канала используем race
 * c экшеном сброса (обнуление) метаданных страницы
 * @param config - конфиг для моделей по умолчанию, который прокидывается в сагу
 */
export function* watcherDefaultModels(config) {
  yield race([call(flowDefaultModels, config), take(RESET)]);
}

/**
 * Сага для первоначальной установки моделей по умолчанию
 * и подписка на изменения через канал
 * @param config - конфиг для моделей по умолчанию
 * @returns {boolean}
 */
export function* flowDefaultModels(config) {
  if (isEmpty(config)) return false;
  const state = yield select();
  const initialModels = yield call(compareAndResolve, config, state);
  if (!isEmpty(initialModels)) {
    yield put(combineModels(initialModels));
  }
  const observableModels = pickBy(
    config,
    item => !!item.observe && !!item.link
  );
  if (!isEmpty(observableModels)) {
    const modelsChan = yield actionChannel([
      SET,
      COPY,
      SYNC,
      REMOVE,
      UPDATE,
      UPDATE_MAP,
    ]);
    try {
      while (true) {
        const oldState = yield select();
        yield take(modelsChan);
        const newState = yield select();
        let changedModels = pickBy(
          observableModels,
          cfg => !isEqual(get(oldState, cfg.link), get(newState, cfg.link))
        );
        const newModels = yield call(
          compareAndResolve,
          changedModels,
          newState
        );
        if (!isEmpty(newModels)) {
          yield put(combineModels(newModels));
        }
      }
    } finally {
      if (yield cancelled()) {
        modelsChan.close();
      }
    }
  }
}

/**
 * Сайд-эффекты для page редюсера
 * @ignore
 */
export const pagesSagas = [
  takeEvery(METADATA_REQUEST, getMetadata),
  throttle(500, MAP_URL, processUrl),
];
