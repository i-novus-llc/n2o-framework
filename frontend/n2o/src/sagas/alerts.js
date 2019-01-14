import { call, put, select, takeLatest, takeEvery, take } from 'redux-saga/effects';
import { delay } from 'redux-saga';

import { ADD, ADD_MULTI } from '../constants/alerts';
import { removeAlert, removeAlerts } from '../actions/alerts';
import { makeAlertsByKeySelector } from '../selectors/alerts';

export function* removeAlertSideEffect(action) {
  const alertsByKey = yield select(makeAlertsByKeySelector(action.payload.key));
  yield alertsByKey && put(removeAlerts(action.payload.key));
}

export function* addAlertSideEffect(config, action) {
  try {
    const timeout = yield call(getTimeout, action, config);
    if (timeout) {
      yield delay(timeout);
      yield* removeAlertSideEffect(action);
    }
  } catch (e) {
    console.error(e);
  }
}

export function getTimeout(action, config) {
  return action.payload.alerts[0].timeout || config.timeout[action.payload.alerts[0].severity];
}

export default config => {
  return [takeEvery([ADD, ADD_MULTI], addAlertSideEffect, config)];
};
