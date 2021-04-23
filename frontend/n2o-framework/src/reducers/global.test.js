import {
    CHANGE_LOCALE,
    REQUEST_CONFIG,
    REQUEST_CONFIG_SUCCESS,
    REQUEST_CONFIG_FAIL,
    CHANGE_ROOT_PAGE,
} from '../constants/global'

import global from './global'

describe('Тесты global reducer', () => {
    it('Должен вернуть EN_en locale', () => {
        expect(
            global(null, {
                type: CHANGE_LOCALE,
                payload: {
                    locale: 'EN_en',
                },
            }),
        ).toEqual({
            locale: 'EN_en',
        })
    })

    it('Должен сменить статус запроса на "загрузку"', () => {
        expect(
            global(null, {
                type: REQUEST_CONFIG,
            }),
        ).toEqual({
            loading: true,
        })
    })

    it('Должен сменить статус загрузки на "успешно"', () => {
        expect(
            global(null, {
                type: REQUEST_CONFIG_SUCCESS,
                payload: {
                    config: {
                        messages: ['message1', 'message2'],
                        menu: ['menu1', 'menu2'],
                    },
                },
            }),
        ).toEqual({
            loading: false,
            messages: ['message1', 'message2'],
            menu: ['menu1', 'menu2'],
        })
    })

    it('Должен сменить статус на "ошибка" и вернуть ошибку', () => {
        expect(
            global(null, {
                type: REQUEST_CONFIG_FAIL,
                payload: {
                    error: {
                        text: 'ERROR',
                        status: 500,
                    },
                },
            }),
        ).toEqual({
            loading: false,
            error: {
                text: 'ERROR',
                status: 500,
            },
        })
    })

    it('Должен сменить rootPageId', () => {
        expect(
            global(null, {
                type: CHANGE_ROOT_PAGE,
                payload: {
                    rootPageId: 'new root page',
                },
            }),
        ).toEqual({
            rootPageId: 'new root page',
        })
    })
})
