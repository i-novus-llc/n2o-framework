import {
    USER_CHECK,
    USER_CHECK_ERROR,
    USER_CHECK_SUCCESS,
    USER_LOGIN,
    USER_LOGIN_ERROR,
    USER_LOGIN_SUCCESS,
    USER_LOGOUT,
    USER_LOGOUT_SUCCESS,
    USER_LOGOUT_ERROR,
} from '../constants/auth'

export function userCheck(payload) {
    return {
        type: USER_CHECK,
        payload,
        meta: { auth: true },
    }
}

export function userCheckError(payload) {
    return {
        type: USER_CHECK_ERROR,
        payload,
        meta: { auth: true },
    }
}

export function userCheckSuccess(payload) {
    return {
        type: USER_CHECK_SUCCESS,
        payload,
        meta: { auth: true },
    }
}

export function userLogin(payload) {
    return {
        type: USER_LOGIN,
        payload,
        meta: { auth: true },
    }
}

export function userLoginError(payload) {
    return {
        type: USER_LOGIN_ERROR,
        payload,
        meta: { auth: true },
    }
}

export function userLoginSuccess(payload) {
    return {
        type: USER_LOGIN_SUCCESS,
        payload,
        meta: { auth: true },
    }
}

export function userLogout(payload) {
    return {
        type: USER_LOGOUT,
        payload,
        meta: { auth: true },
    }
}

export function userLogoutError(payload) {
    return {
        type: USER_LOGOUT_ERROR,
        payload,
        meta: { auth: true },
    }
}

export function userLogoutSuccess(payload) {
    return {
        type: USER_LOGOUT_SUCCESS,
        payload,
        meta: { auth: true },
    }
}
