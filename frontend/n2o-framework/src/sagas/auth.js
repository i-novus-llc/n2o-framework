import { push, replace } from 'connected-react-router'
import { call, put, takeEvery } from 'redux-saga/effects'

import { USER_LOGIN, USER_LOGOUT } from '../constants/auth'
import {
    SECURITY_LOGIN,
    SECURITY_LOGOUT,
    SECURITY_ERROR,
} from '../core/auth/authTypes'
import { userLoginSuccess, userLogoutSuccess } from '../actions/auth'
import { FETCH_ERROR } from '../constants/fetch'
import { fetchErrorContinue } from '../actions/fetch'

export function* resolveAuth(
    { authProvider, redirectPath, externalLoginUrl },
    { type, payload },
) {
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
            if (externalLoginUrl) {
                window.location = externalLoginUrl
            } else {
                yield put(replace(redirectPath || '/login'))
            }
            break
        case FETCH_ERROR:
            try {
                yield call(authProvider, SECURITY_ERROR, payload.error)
                yield put(fetchErrorContinue())
            } catch (e) {
                yield call(authProvider, SECURITY_LOGOUT)
                if (externalLoginUrl) {
                    window.location = externalLoginUrl
                } else {
                    yield put(push(redirectPath || '/login'))
                }
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
