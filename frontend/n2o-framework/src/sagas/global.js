import { call, put, takeEvery, select } from 'redux-saga/effects'
import get from 'lodash/get'

import { CHANGE_LOCALE, REQUEST_CONFIG } from '../constants/global'
import {
    requestConfigSuccess,
    requestConfigFail,
    setReady,
} from '../actions/global'
import { userLogin } from '../actions/auth'
import { localeSelector } from '../selectors/global'
import {
    FETCH_APP_CONFIG,
    CHANGE_LOCALE as CHANGE_LOCALE_API,
} from '../core/api'

import fetchSaga from './fetch'

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
        }
        const config = yield call(fetchSaga, FETCH_APP_CONFIG, params, apiProvider)

        if (config.user) {
            yield put(userLogin(config.user))
        }
        yield put(requestConfigSuccess(config))
        yield put(setReady())
    } catch (err) {
        yield put(
            requestConfigFail({
                stacked: true,
                messages: {
                    text: 'Не удалось получить конфигурацию приложения',
                    stacktrace: err.stack,
                    severity: 'danger',
                },
            }),
        )
    }
}

/**
 * Сага для изменения locale
 * @param apiProvider
 * @param action
 */
export function* changeLocale(apiProvider, action) {
    try {
        const locale = get(action, 'payload.locale')

        yield call(fetchSaga, CHANGE_LOCALE_API, locale, apiProvider)
        window.location.reload()
    } catch (err) {
        console.error(err)
    }
}

/**
 * Сайд-эффекты для global редюсера
 * @ignore
 */
export default apiProvider => [
    takeEvery(REQUEST_CONFIG, getConfig, apiProvider),
    takeEvery(CHANGE_LOCALE, changeLocale, apiProvider),
]
