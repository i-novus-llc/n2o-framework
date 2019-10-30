import {
  call,
  put,
  select,
  takeEvery,
  throttle,
  fork,
} from 'redux-saga/effects';
import { getFormValues, initialize } from 'redux-form';
import pathToRegexp from 'path-to-regexp';
import { isFunction, get } from 'lodash';
import merge from 'deepmerge';

import {
  START_INVOKE,
  SUCCESS_INVOKE,
  FAIL_INVOKE,
} from '../constants/actionImpls';
import { CALL_ACTION_IMPL } from '../constants/toolbar';

import { makeWidgetValidationSelector } from '../selectors/widgets';
import { getModelSelector } from '../selectors/models';

import { validateField } from '../core/validation/createValidator';
import actionResolver from '../core/factory/actionResolver';
import fetchSaga from './fetch.js';
import { FETCH_INVOKE_DATA } from '../core/api.js';
import { getParams } from '../utils/compileUrl';
import { setModel } from '../actions/models';
import { PREFIXES } from '../constants/models';
import { disablePage, enablePage } from '../actions/pages';
import { successInvoke, failInvoke } from '../actions/actionImpl';
import { disableWidgetOnFetch, enableWidget } from '../actions/widgets';
import { MAP_URL, METADATA_REQUEST } from '../constants/pages';

export function* validate(options) {
  const isTouched = true;
  const state = yield select();
  const validationConfig = yield select(
    makeWidgetValidationSelector(options.validatedWidgetId)
  );
  const values = (yield select(getFormValues(options.validatedWidgetId))) || {};
  const notValid =
    options.validate &&
    (yield call(
      validateField(
        validationConfig,
        options.validatedWidgetId,
        state,
        isTouched
      ),
      values,
      options.dispatch
    ));
  return notValid;
}

/**
 * вызов экшена
 */
export function* handleAction(factories, action) {
  const { options, actionSrc } = action.payload;
  try {
    let actionFunc;
    if (isFunction(actionSrc)) {
      actionFunc = actionSrc;
    } else {
      actionFunc = actionResolver(actionSrc, factories);
    }
    const state = yield select();
    const notValid = yield validate(options);
    if (notValid) {
      console.log(`Форма ${options.validatedWidgetId} не прошла валидацию.`);
    } else {
      yield actionFunc &&
      call(actionFunc, {
        ...options,
        state,
      });
    }
  } catch (err) {
    console.error(err);
  }
}

export function* resolveMapping(dataProvider, state) {
  const pathParams = yield call(getParams, dataProvider.pathMapping, state);
  return pathToRegexp.compile(dataProvider.url)(pathParams);
}

/**
 * Отправка запроса
 * @param dataProvider
 * @param model
 * @param apiProvider
 * @returns {IterableIterator<*>}
 */
export function* fetchInvoke(dataProvider, model, apiProvider) {
  const state = yield select();
  const path = yield resolveMapping(dataProvider, state);
  const response = yield call(
    fetchSaga,
    FETCH_INVOKE_DATA,
    {
      basePath: path,
      baseQuery: {},
      baseMethod: dataProvider.method,
      model,
    },
    apiProvider
  );
  return response;
}

export function* handleFailInvoke(metaInvokeFail, widgetId, metaResponse) {
  const meta = merge(metaInvokeFail, metaResponse);
  yield put(failInvoke(widgetId, meta));
}

/**
 * вызов экшена
 */
export function* handleInvoke(apiProvider, action) {
  const { modelLink, widgetId, pageId, dataProvider, data } = action.payload;
  try {
    if (!dataProvider) {
      throw new Error('dataProvider is undefined');
    }

    const optimistic = get(dataProvider, 'optimistic', false);

    if (pageId && !optimistic) {
      yield put(disablePage(pageId));
    }
    if (widgetId && !optimistic) {
      yield put(disableWidgetOnFetch(widgetId));
    }
    let model = data || {};
    if (modelLink) {
      model = yield select(getModelSelector(modelLink));
    }
    const response = optimistic
      ? yield fork(fetchInvoke, dataProvider, model, apiProvider)
      : yield call(fetchInvoke, dataProvider, model, apiProvider);

    const meta = merge(action.meta.success || {}, response.meta || {});

    if (optimistic || (!meta.redirect && !meta.closeLastModal)) {
      yield put(
        setModel(PREFIXES.resolve, widgetId, optimistic ? model : response.data)
      );
    }
    yield put(successInvoke(widgetId, { ...meta, withoutSelectedId: true }));
  } catch (err) {
    console.error(err);
    yield* handleFailInvoke(
      action.meta.fail || {},
      widgetId,
      err.json && err.json.meta ? err.json.meta : {}
    );
  } finally {
    if (pageId) {
      yield put(enablePage(pageId));
    }
    if (widgetId) {
      yield put(enableWidget(widgetId));
    }
  }
}

export default (apiProvider, factories) => {
  return [
    throttle(500, CALL_ACTION_IMPL, handleAction, factories),
    throttle(500, START_INVOKE, handleInvoke, apiProvider),
  ];
};
