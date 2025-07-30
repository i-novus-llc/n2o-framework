import { runSaga } from 'redux-saga'
import { call } from 'redux-saga/effects'
import { replace } from 'connected-react-router'

import { USER_LOGIN, USER_LOGOUT } from '../constants'
import { userLoginSuccess, userLogoutSuccess } from '../store'
import { resolveAuth } from '../sagas'

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
            getState: () => ({ global: {} }),
        }

        await runSaga(
            fakeStore,
            resolveAuth,
            {
                authProvider: () => ({ logout: true }),
                authUrl: '/n2o/saga/test',
            },
            { type: USER_LOGOUT, payload: undefined },
        )
        expect(dispatched[0]).toEqual(userLogoutSuccess())
        expect(dispatched[1]).toEqual(replace('/n2o/saga/test'))
    })
})
