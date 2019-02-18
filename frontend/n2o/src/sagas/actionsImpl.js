import { call, put, select, takeEvery, throttle, takeLatest } from 'redux-saga/effects';
import { getFormValues } from 'redux-form';
import pathToRegexp from 'path-to-regexp';
import { isFunction } from 'lodash';
import merge from 'deepmerge';

import { START_INVOKE, SUCCESS_INVOKE, FAIL_INVOKE } from '../constants/actionImpls';
import { CALL_ACTION_IMPL } from '../constants/toolbar';

import createActionHelper from '../actions/createActionHelper';
import { makeWidgetValidationSelector } from '../selectors/widgets';
import { getModelSelector } from '../selectors/models';

import { validateField } from '../core/validation/createValidator';
import factoryResolver from '../utils/factoryResolver';
import fetchSaga from './fetch.js';
import { FETCH_INVOKE_DATA } from '../core/api.js';
import { getParams } from '../utils/compileUrl';
import { setModel } from '../actions/models';
import { PREFIXES } from '../constants/models';
import { disablePage, enablePage } from '../actions/pages';
import { disableWidgetOnFetch, enableWidget } from '../actions/widgets';

export function* validate(options) {
  const isTouched = true;
  const state = yield select();
  const validationConfig = yield select(makeWidgetValidationSelector(options.containerKey));
  const values = (yield select(getFormValues(options.containerKey))) || {};
  const notValid =
    options.validate &&
    (yield call(
      validateField(validationConfig, options.containerKey, state, isTouched),
      values,
      options.dispatch
    ));
  return notValid;
}

/**
 * вызов экшена
 */
export function* handleAction(action) {
  const { options, actionSrc } = action.payload;
  try {
    let actionFunc;
    if (isFunction(actionSrc)) {
      actionFunc = actionSrc;
    } else {
      actionFunc = factoryResolver(actionSrc, null, 'function');
    }
    const state = yield select();
    const notValid = yield validate(options);
    if (notValid) {
      throw Error('Ошибка валидации');
    } else {
      yield actionFunc &&
        call(actionFunc, {
          ...options,
          state
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
 * @returns {IterableIterator<*>}
 */
export function* fetchInvoke(dataProvider, model) {
  const state = yield select();
  const path = yield resolveMapping(dataProvider, state);
  const response = yield call(fetchSaga, FETCH_INVOKE_DATA, {
    basePath: path,
    baseQuery: {},
    baseMethod: dataProvider.method,
    model
  });
  return response;
}

export function* handleFailInvoke(metaInvokeFail, widgetId, metaResponse) {
  const meta = merge(metaInvokeFail, metaResponse);
  yield put(createActionHelper(FAIL_INVOKE)({ widgetId }, meta));
}

/**
 * вызов экшена
 */
export function* handleInvoke(action) {
  const { modelLink, widgetId, pageId, dataProvider, data } = action.payload;
  try {
    if (!dataProvider) {
      throw new Error('dataProvider is undefined');
    }
    if (pageId) {
      yield put(disablePage(pageId));
    }
    if (widgetId) {
      yield put(disableWidgetOnFetch(widgetId));
    }
    let model = data || {};
    if (modelLink) {
      model = yield select(getModelSelector(modelLink));
    }
    const response = yield call(fetchInvoke, dataProvider, model);

    const meta = merge(action.meta.success || {}, response.meta || {});

    if (!meta.redirect && !meta.closeLastModal) {
      yield put(setModel(PREFIXES.resolve, widgetId, response.data));
    }
    yield put(
      createActionHelper(SUCCESS_INVOKE)(
        { widgetId },
        {
          ...meta,
          withoutSelectedId: true
        }
      )
    );
  } catch (err) {
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

export const actionsImplSagas = [
  takeLatest(CALL_ACTION_IMPL, handleAction),
  throttle(500, START_INVOKE, handleInvoke)
];
