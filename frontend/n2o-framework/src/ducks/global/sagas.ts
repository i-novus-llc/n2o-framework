import { call, put, takeEvery, select } from 'redux-saga/effects'
import get from 'lodash/get'

import { userLogin } from '../user/store'
// @ts-ignore ignore import error from js file
import { FETCH_APP_CONFIG, CHANGE_LOCALE as CHANGE_LOCALE_API } from '../../core/api'
// @ts-ignore ignore import error from js file
import fetchSaga from '../../sagas/fetch'
import { addAlert } from '../alerts/store'

import {
    requestConfigSuccess,
    requestConfigFail,
    setReady,
    changeLocale as changeLocaleGlobal,
    requestConfig,
} from './store'
import { localeSelector } from './selectors'
import { State as Global } from './Global'

/**
 * Сага для вызова настроек приложения
 * @param apiProvider
 * @param action
 */
export function* getConfig(apiProvider: unknown, action: { payload: { params: object } }) {
    try {
        const params: { locale: string } = {
            locale: yield select(localeSelector),
            ...action.payload.params,
        }
        const config: Global = yield call(fetchSaga, FETCH_APP_CONFIG, params, apiProvider)

        if (config.user) {
            yield put(userLogin(config.user))
        }
        yield put(requestConfigSuccess(config))
        yield put(setReady())
    } catch ({ json, stack }) {
        const stacktrace = get(json, 'meta.alert.messages[0].stacktrace', stack)
        const alert = get(json, 'meta.alert.messages[0]', null)
        const status = get(json, 'status')

        yield put(
            requestConfigFail({
                title: 'Ошибка',
                text: 'Не удалось получить конфигурацию приложения',
                stacktrace,
                severity: 'danger',
                placement: 'top',
                status,
            }),
        )

        if (alert) {
            yield put(addAlert('top', alert))
        }
    }
}

/**
 * Сага для изменения locale
 * @param apiProvider
 * @param action
 */
export function* changeLocale(apiProvider: unknown, action: { payload: { locale: string } }) {
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
/* FIXME */
export default (apiProvider: unknown) => [
    // @ts-ignore проблемы с типпизацией
    takeEvery(requestConfig.type, getConfig, apiProvider),
    // @ts-ignore проблемы с типпизацией
    takeEvery(changeLocaleGlobal.type, changeLocale, apiProvider),
]
