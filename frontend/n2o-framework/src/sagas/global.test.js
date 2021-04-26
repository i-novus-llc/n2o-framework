import { runSaga } from 'redux-saga'

import { REQUEST_CONFIG } from '../constants/global'
import { requestConfigSuccess, requestConfigFail } from '../actions/global'

import { getConfig } from './global'

describe('Проверка саги global', () => {
    it('Должен получить конфиг', async () => {
        const action = {
            meta: {},
            payload: {
                params: undefined,
            },
            type: REQUEST_CONFIG,
        }
        const config = {
            menu: {},
            page: {},
        }
        const dispatched = []

        const fakeStore = {
            getState: () => ({
                global: {
                    locale: 'ru_RU',
                },
            }),
            dispatch: action => dispatched.push(action),
        }

        const api = jest.fn(() => Promise.resolve(config))

        await runSaga(fakeStore, getConfig, api, action)
        const requestConfigSuccessAction = requestConfigSuccess(config)
        expect(dispatched[2]).toEqual(requestConfigSuccessAction)
    })

    it('Должна выпасть ошибка', async () => {
        const errorObject = {
            stacked: true,
            messages: {
                text: 'Не удалось получить конфигурацию приложения',
                severity: 'danger',
            },
        }
        const gen = getConfig()
        gen.next()
        expect(gen.next().value.payload.action.type).toEqual(
            requestConfigFail(errorObject).type,
        )
    })
})
