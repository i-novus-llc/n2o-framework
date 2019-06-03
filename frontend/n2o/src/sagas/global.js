import { call, put, takeEvery, select } from 'redux-saga/effects';
import { CHANGE_LOCALE, REQUEST_CONFIG } from '../constants/global';
import { requestConfigSuccess, requestConfigFail } from '../actions/global';
import { userLogin } from '../actions/auth';
import { localeSelector } from '../selectors/global';
import fetchSaga from './fetch.js';
import { FETCH_APP_CONFIG } from '../core/api.js';

/**
 * Сага для вызова настроек приложения
 * @param action
 */
export function* getConfig(action) {
  try {
    const params = {
      locale: yield select(localeSelector),
      ...action.payload.params,
    };
    const { user, ...config } = yield call(fetchSaga, FETCH_APP_CONFIG, params);
    if (user) {
      yield put(userLogin(user));
    }
    yield put(requestConfigSuccess(config));
  } catch (err) {
    // todo: реальная ошибка
    yield put(
      requestConfigFail({
        label: 'Ошибка',
        text: 'Не удалось получить конфигурацию приложения',
        closeButton: false,
        severity: 'danger',
      })
    );
  }
}

/**
 * Сайд-эффекты для global редюсера
 * @ignore
 */
export const globalSagas = [
  takeEvery([REQUEST_CONFIG, CHANGE_LOCALE], getConfig),
];
