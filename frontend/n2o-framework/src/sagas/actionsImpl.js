import {
  call,
  put,
  select,
  takeEvery,
  throttle,
  fork,
} from 'redux-saga/effects';
import { getFormValues } from 'redux-form';

import isFunction from 'lodash/isFunction';
import get from 'lodash/get';

import merge from 'deepmerge';

import { START_INVOKE } from '../constants/actionImpls';
import { CALL_ACTION_IMPL } from '../constants/toolbar';

import {
  makeFormModelPrefixSelector,
  makeWidgetValidationSelector,
} from '../selectors/widgets';
import { getModelSelector } from '../selectors/models';

import { validateField } from '../core/validation/createValidator';
import actionResolver from '../core/factory/actionResolver';
import fetchSaga from './fetch.js';
import { dataProviderResolver } from '../core/dataProviderResolver';
import { FETCH_INVOKE_DATA } from '../core/api.js';
import { setModel } from '../actions/models';
import { PREFIXES } from '../constants/models';
import { disablePage, enablePage } from '../actions/pages';
import { successInvoke, failInvoke } from '../actions/actionImpl';
import { disableWidgetOnFetch, enableWidget } from '../actions/widgets';

import { setButtonDisabled, setButtonEnabled } from '../actions/toolbar';

/**
 * @deprecated
 */

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

/**
 * Отправка запроса
 * @param dataProvider
 * @param model
 * @param apiProvider
 * @returns {IterableIterator<*>}
 */
export function* fetchInvoke(dataProvider, model, apiProvider) {
  const state = yield select();
  const submitForm = get(dataProvider, 'submitForm', true);
  const {
    basePath: path,
    formParams,
    headersParams,
  } = yield dataProviderResolver(state, dataProvider);

  const response = yield call(
    fetchSaga,
    FETCH_INVOKE_DATA,
    {
      basePath: path,
      baseQuery: {},
      baseMethod: dataProvider.method,
      headers: headersParams,
      model: submitForm ? Object.assign({}, model, formParams) : formParams,
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
  const {
    modelLink,
    widgetId,
    pageId,
    dataProvider,
    data,
    needResolve = true,
  } = action.payload;

  const state = yield select();
  const optimistic = get(dataProvider, 'optimistic', false);
  const buttonIds = !optimistic ? Object.keys(state.toolbar[pageId]) : [];

  try {
    if (!dataProvider) {
      throw new Error('dataProvider is undefined');
    }
    if (pageId && !optimistic) {
      yield put(disablePage(pageId));
    }
    if (widgetId && !optimistic) {
      yield put(disableWidgetOnFetch(widgetId));

      for (let index in buttonIds) {
        yield put(setButtonDisabled(pageId, buttonIds[index]));
      }
    }
    let model = data || {};
    if (modelLink) {
      model = yield select(getModelSelector(modelLink));
    }
    const response = optimistic
      ? yield fork(fetchInvoke, dataProvider, model, apiProvider)
      : yield call(fetchInvoke, dataProvider, model, apiProvider);

    const meta = merge(action.meta.success || {}, response.meta || {});
    const modelPrefix = yield select(makeFormModelPrefixSelector(widgetId));

    if (
      needResolve &&
      (optimistic || (!meta.redirect && !meta.modalsToClose))
    ) {
      yield put(
        setModel(modelPrefix, widgetId, optimistic ? model : response.data)
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

      for (let index in buttonIds) {
        yield put(setButtonEnabled(pageId, buttonIds[index]));
      }
    }
  }
}

export function* handleDummy() {
  alert('AHOY!');
}

export default (apiProvider, factories) => {
  return [
    throttle(500, CALL_ACTION_IMPL, handleAction, factories),
    throttle(500, START_INVOKE, handleInvoke, apiProvider),
    takeEvery('n2o/button/Dummy', handleDummy),
  ];
};
