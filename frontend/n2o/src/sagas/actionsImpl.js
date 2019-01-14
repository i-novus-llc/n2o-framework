import { call, put, select, takeEvery, throttle } from 'redux-saga/effects';
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
    const validationConfig = yield select(makeWidgetValidationSelector(options.containerKey));
    const values = (yield select(getFormValues(options.containerKey))) || {};
    const notValid =
      options.validate &&
      (yield call(validateField(validationConfig, options.containerKey), values, options.dispatch));
    console.log(notValid);
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
  if (!dataProvider.pathMapping) {
    return 'null';
  }
  const pathParams = yield call(getParams, dataProvider.pathMapping, state);
  return pathToRegexp.compile(dataProvider.url)(pathParams);
}

/**
 * вызов экшена
 */
export function* handleInvoke(action) {
  const { modelLink, widgetId, dataProvider, data } = action.payload;
  try {
    if (!dataProvider) {
      throw new Error('dataProvider is undefined');
    }
    let model = data || {};
    if (modelLink) {
      model = yield select(getModelSelector(modelLink));
    }
    const state = yield select();
    const path = yield resolveMapping(dataProvider, state);
    const response = yield call(fetchSaga, FETCH_INVOKE_DATA, {
      basePath: path,
      baseQuery: {},
      baseMethod: dataProvider.method,
      model
    });

    const meta = merge(action.meta.success || {}, response.meta || {});

    if (!meta.redirect) {
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
    const meta = merge(action.meta.fail, (err.body && err.body.meta) || {});
    yield put(createActionHelper(FAIL_INVOKE)({ widgetId, err }, meta));
  }
}

export const actionsImplSagas = [
  throttle(500, CALL_ACTION_IMPL, handleAction),
  throttle(500, START_INVOKE, handleInvoke)
];
