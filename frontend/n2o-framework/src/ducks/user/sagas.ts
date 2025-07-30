import { replace } from 'connected-react-router'
import { call, put, takeEvery, select } from 'redux-saga/effects'

import { getAuthUrl } from '../global/selectors'
import { Action } from '../Action'
import { AuthProvider } from '../../core/auth/Provider'

import { userLoginSuccess, userLogoutSuccess } from './store'
import { USER_LOGIN, USER_LOGOUT } from './constants'

const DEFAULT_AUTH_URL = '/login'

interface Config {
    authUrl?: string
    provider: AuthProvider
}

function* redirectOnUnauthorized(authUrl: string & Location) {
    const isUrlExternal = /(https?:\/\/\S+)|(www\.\S+)/g.test(authUrl)

    if (isUrlExternal) {
        window.location = authUrl
    } else {
        yield put(replace(authUrl))
    }
}

export function* resolveAuth({ provider, authUrl = DEFAULT_AUTH_URL }: Config, { type, payload }: Action) {
    const configAuthUrl: string = yield select(getAuthUrl)

    switch (type) {
        case USER_LOGIN:
            try {
                const userPayload: object = yield provider.login({ user: payload, authUrl })

                yield put(userLoginSuccess(userPayload))
            } catch (e) {
                // eslint-disable-next-line no-console
                console.warn(e)
            }

            break
        case USER_LOGOUT:
            // @ts-ignore проблема с типизацией saga
            yield provider.logout()
            yield put(userLogoutSuccess())
            // @ts-ignore проблема с типизацией saga
            yield call(redirectOnUnauthorized, configAuthUrl || authUrl)

            break
        default:
            break
    }
}

export default (config: Config) => ([
    // @ts-ignore проблема с типизацией saga
    takeEvery((action: { meta?: { auth: Config } }) => action?.meta && action.meta.auth, resolveAuth, config),
])
