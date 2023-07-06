import { replace } from 'connected-react-router'
import { call, put, takeEvery, select } from 'redux-saga/effects'

import { SECURITY_LOGIN, SECURITY_LOGOUT, SECURITY_ERROR } from '../../core/auth/authTypes'
import { FETCH_ERROR } from '../../constants/fetch'
// @ts-ignore ignore import error from js file
import { fetchErrorContinue } from '../../actions/fetch'
import { getAuthUrl } from '../global/selectors'
import { Action } from '../Action'

import { userLoginSuccess, userLogoutSuccess } from './store'
import { USER_LOGIN, USER_LOGOUT } from './constants'

const DEFAULT_AUTH_URL = '/login'

/* FIXME Типизация authProvider */
interface IConfig { authProvider(): Promise<boolean>, authUrl?: string }

function* redirectOnUnauthorized(authUrl: string & Location) {
    const isUrlExternal = /(https?:\/\/\S+)|(www\.\S+)/g.test(authUrl)

    if (isUrlExternal) {
        window.location = authUrl
    } else {
        yield put(replace(authUrl))
    }
}

export function* resolveAuth({ authProvider, authUrl = DEFAULT_AUTH_URL }: IConfig, { type, payload }: Action) {
    const configAuthUrl: string = yield select(getAuthUrl)

    switch (type) {
        case USER_LOGIN:
            try {
                // @ts-ignore проблема с типизацией saga
                const userPayload: object = yield call(authProvider, SECURITY_LOGIN, payload)

                yield put(userLoginSuccess(userPayload))
            } catch (e) {
                // @ts-ignore проблема с типизацией saga
                yield call(authProvider, SECURITY_ERROR)
            }

            break
        case USER_LOGOUT:
            // @ts-ignore проблема с типизацией saga
            yield call(authProvider, SECURITY_LOGOUT)
            yield put(userLogoutSuccess())
            // @ts-ignore проблема с типизацией saga
            yield call(redirectOnUnauthorized, configAuthUrl || authUrl)

            break
        case FETCH_ERROR:
            try {
                // @ts-ignore проблема с типизацией saga
                yield call(authProvider, SECURITY_ERROR, payload.error)
                yield put(fetchErrorContinue())
            } catch (e) {
                // @ts-ignore проблема с типизацией saga
                yield call(authProvider, SECURITY_LOGOUT)
                // @ts-ignore проблема с типизацией saga
                yield call(redirectOnUnauthorized, configAuthUrl || authUrl)
            }

            break
        default:
            break
    }
}

export function* callErrorContinue() {
    yield put(fetchErrorContinue())
}

export default (config: IConfig) => {
    if (!config.authProvider) { return [takeEvery(FETCH_ERROR, callErrorContinue)] }

    return [
        // @ts-ignore проблема с типизацией saga
        takeEvery((action: { meta?: { auth: IConfig } }) => action?.meta && action.meta.auth, resolveAuth, config),
        takeEvery(FETCH_ERROR, resolveAuth, config),
    ]
}
