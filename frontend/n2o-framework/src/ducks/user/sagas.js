import { replace } from 'connected-react-router'
import { call, put, takeEvery, select } from 'redux-saga/effects'

import {
    SECURITY_LOGIN,
    SECURITY_LOGOUT,
    SECURITY_ERROR,
} from '../../core/auth/authTypes'
import { FETCH_ERROR } from '../../constants/fetch'
import { fetchErrorContinue } from '../../actions/fetch'
import { getAuthUrl } from '../global/store'

import { userLoginSuccess, userLogoutSuccess } from './store'
import { USER_LOGIN, USER_LOGOUT } from './constants'

const DEFAULT_AUTH_URL = '/login'

function* redirectOnUnauthorized(authUrl) {
    const isUrlExternal = /(https?:\/\/\S+)/g.test(authUrl)

    if (isUrlExternal) {
        window.location = authUrl
    } else {
        yield put(replace(authUrl))
    }
}

export function* resolveAuth(
    { authProvider, authUrl = DEFAULT_AUTH_URL },
    { type, payload },
) {
    const configAuthUrl = yield select(getAuthUrl)

    switch (type) {
        case USER_LOGIN:
            try {
                const userPayload = yield call(authProvider, SECURITY_LOGIN, payload)

                yield put(userLoginSuccess(userPayload))
            } catch (e) {
                yield call(authProvider, SECURITY_ERROR)
            }

            break
        case USER_LOGOUT:
            yield call(authProvider, SECURITY_LOGOUT)
            yield put(userLogoutSuccess())
            yield call(redirectOnUnauthorized, configAuthUrl || authUrl)

            break
        case FETCH_ERROR:
            try {
                yield call(authProvider, SECURITY_ERROR, payload.error)
                yield put(fetchErrorContinue())
            } catch (e) {
                yield call(authProvider, SECURITY_LOGOUT)
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

export default (config) => {
    if (!config.authProvider) { return [takeEvery(FETCH_ERROR, callErrorContinue)] }

    return [
        takeEvery(action => action.meta && action.meta.auth, resolveAuth, config),
        takeEvery(FETCH_ERROR, resolveAuth, config),
    ]
}
