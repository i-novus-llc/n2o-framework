import { runSaga } from 'redux-saga'
import fetchMock from 'fetch-mock'
import { put } from 'redux-saga/effects'

import { FETCH_APP_CONFIG } from '../core/api'
import * as api from '../core/api'
import { fetchEnd, fetchStart } from '../actions/fetch'

import fetchSaga from './fetch'

fetchMock.get('*', () => ({ some: 'value' }))

describe('Проверка саги fetch', () => {
    it('Проверка отправки запросов', async () => {
        const dispatched = []

        const fakeStore = {
            getState: () => ({ value: 'some value' }),
            dispatch: action => dispatched.push(action),
        }

        api.default = jest.fn(() => Promise.resolve({
            some: 'value',
        }))

        await runSaga(fakeStore, fetchSaga, FETCH_APP_CONFIG, { locale: 'ru_RU' })

        const startFetch = put(fetchStart(FETCH_APP_CONFIG, { locale: 'ru_RU' }))
            .payload.action
        const endFetch = put(
            fetchEnd(FETCH_APP_CONFIG, { locale: 'ru_RU' }, { some: 'value' }),
        ).payload.action

        expect(dispatched[0]).toEqual(startFetch)
        expect(dispatched[1]).toEqual(endFetch)
    })
})
