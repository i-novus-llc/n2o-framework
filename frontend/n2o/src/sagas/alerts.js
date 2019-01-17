import { call, put, select, takeLatest, takeEvery, take, all } from 'redux-saga/effects';
import { delay } from 'redux-saga';

import { ADD, ADD_MULTI } from '../constants/alerts';
import { removeAlert, removeAlerts } from '../actions/alerts';
import { makeAlertsByKeySelector } from '../selectors/alerts';

export function* removeAlertSideEffect(action, timeout) {
  yield delay(timeout);
  const alertsByKey = yield select(makeAlertsByKeySelector(action.payload.key));
  yield alertsByKey && put(removeAlerts(action.payload.key));
}

export function* addAlertSideEffect(config, action) {
  try {
    const { alerts } = action.payload;
    let effects = [];
    for (let i = 0; i < alerts.length; i++) {
      const timeout = yield call(getTimeout, alerts[i], config);
      if (timeout) {
        effects = [...effects, removeAlertSideEffect(action, timeout)];
      }
    }
    yield all(effects);
  } catch (e) {
    console.error(e);
  }
}

export function getTimeout(alert, config) {
  return alert.timeout || (config.timeout && config.timeout[alert.severity]);
}

export default config => {
  return [takeEvery([ADD, ADD_MULTI], addAlertSideEffect, config)];
};
