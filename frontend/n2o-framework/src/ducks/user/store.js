import { createAction, createSlice } from '@reduxjs/toolkit'

import {
    USER_CHECK,
    USER_CHECK_ERROR,
    USER_CHECK_SUCCESS,
    USER_LOGIN,
    USER_LOGIN_ERROR,
    USER_LOGOUT,
    USER_LOGOUT_ERROR,
} from './constants'

/**
 * @type User.store
 */
const initialState = {
    id: null,
    name: null,
    roles: [],
    isLoggedIn: false,
    inProgress: false,
}

const userSlice = createSlice({
    name: 'n2o/auth',
    initialState,
    reducers: {
        USER_LOGIN_SUCCESS: {
            /**
             * @param {any} payload
             * @return {{payload: any, meta: {auth: boolean}}}
             */
            prepare(payload) {
                return ({
                    payload,
                    meta: { auth: true },
                })
            },

            /**
             * Авторизация успешна
             * @param {User.store} state
             * @param {Object} action
             * @param {string} action.type
             * @param {any} action.payload
             */
            reducer(_state, action) {
                return { ...action.payload, isLoggedIn: true }
            },
        },

        USER_LOGOUT_SUCCESS: {
            /**
             * @param {any} payload
             * @return {{payload: any, meta: {auth: boolean}}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(payload) {
                return ({
                    payload,
                    meta: { auth: true },
                })
            },

            reducer() {
                return initialState
            },
        },
    },
})

export default userSlice.reducer

// Actions
export const {
    USER_LOGIN_SUCCESS: userLoginSuccess,
    USER_LOGOUT_SUCCESS: userLogoutSuccess,
} = userSlice.actions

export const userCheck = createAction(USER_CHECK, payload => ({
    payload,
    meta: { auth: true },
}))

export const userCheckError = createAction(USER_CHECK_ERROR, payload => ({
    payload,
    meta: { auth: true },
}))

export const userCheckSuccess = createAction(USER_CHECK_SUCCESS, payload => ({
    payload,
    meta: { auth: true },
}))

export const userLogin = createAction(USER_LOGIN, payload => ({
    payload,
    meta: { auth: true },
}))

export const userLoginError = createAction(USER_LOGIN_ERROR, payload => ({
    payload,
    meta: { auth: true },
}))

export const userLogout = createAction(USER_LOGOUT, payload => ({
    payload,
    meta: { auth: true },
}))

export const userLogoutError = createAction(USER_LOGOUT_ERROR, payload => ({
    payload,
    meta: { auth: true },
}))
