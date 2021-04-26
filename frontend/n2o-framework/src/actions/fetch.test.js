import {
    FETCH_START,
    FETCH_END,
    FETCH_ERROR,
    FETCH_CANCEL,
} from '../constants/fetch'

import { fetchStart, fetchEnd, fetchError, fetchCancel } from './fetch'

const options = {
    headers: {
        'Content-Type': 'application/json',
    },
}

const response = {
    data: [
        {
            name: 'Alex',
        },
        {
            name: 'Ben',
        },
    ],
}

const error = {
    error: {
        text: 'Not found',
        status: 404,
    },
}

describe('Тесты для экшенов fetch', () => {
    describe('Проверка экшена fetchStart', () => {
        it('Генирирует правильное событие', () => {
            const action = fetchStart('GET', options)
            expect(action.type).toEqual(FETCH_START)
        })
        it('Возвращает правильный payload', () => {
            const action = fetchStart('GET', options)
            expect(action.payload.fetchType).toEqual('GET')
            expect(action.payload.options).toEqual(options)
        })
    })

    describe('Проверка экшена fetchEnd', () => {
        it('Генирирует правильное событие', () => {
            const action = fetchEnd('POST', options)
            expect(action.type).toEqual(FETCH_END)
        })
        it('Возвращает правильный payload', () => {
            const action = fetchEnd('POST', options, response)
            expect(action.payload.fetchType).toEqual('POST')
            expect(action.payload.options).toEqual(options)
            expect(action.payload.response).toEqual(response)
        })
    })

    describe('Проверка экшена fetchError', () => {
        it('Генирирует правильное событие', () => {
            const action = fetchError('GET', options)
            expect(action.type).toEqual(FETCH_ERROR)
        })
        it('Возвращает правильный payload', () => {
            const action = fetchError('GET', options, error)
            expect(action.payload.fetchType).toEqual('GET')
            expect(action.payload.options).toEqual(options)
            expect(action.payload.error).toEqual(error)
        })
    })

    describe('Проверка экшена fetchCancel', () => {
        it('Генирирует правильное событие', () => {
            const action = fetchCancel('GET', options)
            expect(action.type).toEqual(FETCH_CANCEL)
        })
        it('Возвращает правильный payload', () => {
            const action = fetchCancel('PUT', options)
            expect(action.payload.fetchType).toEqual('PUT')
            expect(action.payload.options).toEqual(options)
        })
    })
})
