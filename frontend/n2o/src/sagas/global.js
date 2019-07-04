import { call, put, takeEvery, select } from 'redux-saga/effects';
import { CHANGE_LOCALE, REQUEST_CONFIG } from '../constants/global';
import { requestConfigSuccess, requestConfigFail } from '../actions/global';
import { userLogin } from '../actions/auth';
import { localeSelector } from '../selectors/global';
import fetchSaga from './fetch';
import { FETCH_APP_CONFIG } from '../core/api';

/**
 * Сага для вызова настроек приложения
 * @param apiProvider
 * @param action
 */
export function* getConfig(apiProvider, action) {
  try {
    const params = {
      locale: yield select(localeSelector),
      ...action.payload.params,
    };
    const config = yield call(fetchSaga, FETCH_APP_CONFIG, params, apiProvider);

    if (config.user) {
      yield put(userLogin(config.user));
    }
    yield put(requestConfigSuccess(config));
  } catch (err) {
    yield put(
      requestConfigFail({
        stacked: true,
        messages: {
          text: 'Не удалось получить конфигурацию приложения',
          stacktrace: err.stack,
          severity: 'danger',
        },
      })
    );
  }
}

/**
 * Сайд-эффекты для global редюсера
 * @ignore
 */
export default apiProvider => {
  return [takeEvery([REQUEST_CONFIG, CHANGE_LOCALE], getConfig, apiProvider)];
};
