import { put, select, takeEvery, take, fork, cancel } from 'redux-saga/effects';
import { push, LOCATION_CHANGE } from 'connected-react-router';
import isArray from 'lodash/isArray';
import map from 'lodash/map';
import get from 'lodash/get';
import toPairs from 'lodash/toPairs';
import flow from 'lodash/flow';
import keys from 'lodash/keys';
import { reset, touch } from 'redux-form';
import { batchActions } from 'redux-batched-actions';

import { GLOBAL_KEY } from '../constants/alerts';
import { addAlerts, removeAlerts } from '../actions/alerts';
import { addFieldMessage } from '../actions/formPlugin';
import { metadataRequest } from '../actions/pages';
import { dataRequestWidget } from '../actions/widgets';
import { updateWidgetDependency } from '../actions/dependency';
import { insertOverlay } from '../actions/overlays';
import compileUrl from '../utils/compileUrl';
import { id } from '../utils/id';

export function* alertEffect(action) {
  try {
    const { alertKey = GLOBAL_KEY, messages, stacked } = action.meta.alert;
    if (!stacked) yield put(removeAlerts(alertKey));
    const alerts = isArray(messages)
      ? messages.map(message => ({ ...message, id: message.id || id() }))
      : [{ ...messages, id: messages.id || id() }];
    yield put(addAlerts(alertKey, alerts));
  } catch (e) {
    console.error(e);
  }
}

export function* redirectEffect(action) {
  try {
    const { path, pathMapping, queryMapping, target } = action.meta.redirect;
    const state = yield select();
    const newUrl = compileUrl(path, { pathMapping, queryMapping }, state);
    if (target === 'application') {
      yield put(push(newUrl));
    } else if (target === 'self') {
      window.location = newUrl;
    } else {
      window.open(newUrl);
    }
  } catch (e) {
    console.error(e);
  }
}

function* fetchFlow(options) {
  while (true) {
    yield take([LOCATION_CHANGE]);
    return yield put(dataRequestWidget(options.widgetId, options.options));
  }
}

export function* refreshEffect(action) {
  let lastTask;
  try {
    const { type, options } = action.meta.refresh;

    switch (type) {
      case 'widget':
        if (
          action.meta.redirect &&
          action.meta.redirect.target === 'application'
        ) {
          if (lastTask) {
            yield cancel(lastTask);
          }
          lastTask = yield fork(fetchFlow, options);
        } else {
          yield put(
            dataRequestWidget(options.widgetId, {
              ...options.options,
              withoutSelectedId: action.meta.withoutSelectedId,
            })
          );
        }
        break;
      case 'metadata':
        yield put(metadataRequest(...options));
        break;
    }
  } catch (e) {
    console.log(e);
  }
}

export function* messagesFormEffect({ meta }) {
  try {
    const formID = get(meta, 'messages.form', false);
    const fields = get(meta, 'messages.fields', false);
    const putBanchActions = flow([batchActions, put]);
    if (formID && fields) {
      const serrializeData = map(toPairs(fields), ([name, ...message]) =>
        addFieldMessage(formID, name, ...message)
      );
      yield putBanchActions(serrializeData);
      yield put(touch(formID, ...keys(fields)));
    }
  } catch (e) {
    console.error(e);
  }
}

export function* clearFormEffect(action) {
  yield put(reset(action.meta.clearForm));
}

export function* updateWidgetDependencyEffect({ meta }) {
  const { widgetId } = meta;
  yield put(updateWidgetDependency(widgetId));
}

export function* userDialogEffect({ meta }) {
  const { title, description, toolbar } = meta.dialog;

  yield put(
    insertOverlay('dialog', true, 'dialog', {
      title,
      description,
      toolbar,
    })
  );
}

export const metaSagas = [
  takeEvery(action => action.meta && action.meta.alert, alertEffect),
  takeEvery(action => action.meta && action.meta.redirect, redirectEffect),
  takeEvery(action => action.meta && action.meta.refresh, refreshEffect),
  takeEvery(action => action.meta && action.meta.clearForm, clearFormEffect),
  takeEvery(action => action.meta && action.meta.messages, messagesFormEffect),
  takeEvery(
    action => action.meta && action.meta.updateWidgetDependency,
    updateWidgetDependencyEffect
  ),
  takeEvery(action => action.meta && action.meta.dialog, userDialogEffect),
];
