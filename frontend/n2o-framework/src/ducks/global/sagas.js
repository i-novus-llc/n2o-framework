import { call, put, takeEvery, select } from 'redux-saga/effects'
import get from 'lodash/get'

import { userLogin } from '../user/store'
import {
    FETCH_APP_CONFIG,
    CHANGE_LOCALE as CHANGE_LOCALE_API,
} from '../../core/api'
import fetchSaga from '../../sagas/fetch'

import {
    requestConfigSuccess,
    requestConfigFail,
    setReady,
    localeSelector,
    changeLocale as changeLocaleGlobal,
    requestConfig,
} from './store'

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
        // eslint-disable-next-line no-console
        console.error(err)
    }
}

/**
 * Сайд-эффекты для global редюсера
 * @ignore
 */
export default apiProvider => [
    takeEvery(requestConfig, getConfig, apiProvider),
    takeEvery(changeLocaleGlobal, changeLocale, apiProvider),
]
