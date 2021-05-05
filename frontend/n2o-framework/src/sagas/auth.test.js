import { runSaga } from 'redux-saga'
import { call } from 'redux-saga/effects'
import { replace, push } from 'connected-react-router'

import { USER_LOGIN, USER_LOGOUT } from '../constants/auth'
import { FETCH_ERROR } from '../constants/fetch'
import { userLoginSuccess, userLogoutSuccess } from '../actions/auth'
import { SECURITY_ERROR } from '../core/auth/authTypes'

import { resolveAuth } from './auth'

describe('Проверка саги auth', () => {
    it('resolveAuth должен залогиниться', async () => {
        const dispatched = []
        const data = {
            login: 'admin',
            password: '1234',
        }
        const fakeStore = {
            getState: () => ({ some: 'value' }),
            dispatch: action => dispatched.push(action),
        }

        await runSaga(
            fakeStore,
            resolveAuth,
            {
                authProvider: () => data,
            },
            { type: USER_LOGIN, payload: null },
        )
        expect(dispatched[0]).toEqual(userLoginSuccess(data))
    })

    it('resolveAuth должен разлогинить пользователя', async () => {
        const dispatched = []
        const fakeStore = {
            dispatch: action => dispatched.push(action),
        }

        await runSaga(
            fakeStore,
            resolveAuth,
            {
                authProvider: () => ({ logout: true }),
                redirectPath: '/n2o/saga/test',
            },
            { type: USER_LOGOUT, payload: undefined },
        )
        expect(dispatched[0]).toEqual(userLogoutSuccess())
        expect(dispatched[1]).toEqual(replace('/n2o/saga/test'))
    })

    it('resolveAuth должен поймать ошибку запроса', async () => {
        const authProvider = () => ({ error: true })
        const gen = resolveAuth(
            {
                authProvider,
            },
            { type: FETCH_ERROR, payload: { error: 'request error' } },
        )

        expect(gen.next().value.CALL).toEqual(
            call(authProvider, SECURITY_ERROR, 'request error').CALL,
        )
    })

    it('resolveAuth должен выпасть в исключение', () => {
        const authProvider = () => {
            throw new Error()
        }
        const gen = resolveAuth(
            {
                authProvider,
            },
            { type: FETCH_ERROR },
        )

        gen.next()
        expect(gen.next().value.payload.action).toEqual(push('/login'))
    })
})
