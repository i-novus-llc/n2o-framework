import {
    CHANGE_LOCALE,
    REQUEST_CONFIG,
    REQUEST_CONFIG_SUCCESS,
    REQUEST_CONFIG_FAIL,
    CHANGE_ROOT_PAGE,
} from '../constants/global'

import {
    changeLocale,
    requestConfig,
    requestConfigSuccess,
    requestConfigFail,
    changeRootPage,
} from './global'

const params = {
    a: 1,
    b: 2,
}

const config = {
    toolbar: {},
    actions: {},
    page: 'test',
}

const error = {
    text: 'Not found',
    status: 404,
}

describe('Тесты экшенов global', () => {
    describe('Проверка экшена changeLocale', () => {
        it('Генирирует правильное событие', () => {
            const action = changeLocale('RU_ru')

            expect(action.type).toEqual(CHANGE_LOCALE)
        })
        it('Возвращает правильный payload', () => {
            const action = changeLocale('RU_ru')

            expect(action.payload.locale).toEqual('RU_ru')
        })
    })

    describe('Проверка экшена requestConfig', () => {
        it('Генирирует правильное событие', () => {
            const action = requestConfig(params)

            expect(action.type).toEqual(REQUEST_CONFIG)
        })
        it('Возвращает правильный payload', () => {
            const action = requestConfig(params)

            expect(action.payload.params).toEqual(params)
        })
    })

    describe('Проверка экшена requestConfigSuccess', () => {
        it('Генирирует правильное событие', () => {
            const action = requestConfigSuccess(config)

            expect(action.type).toEqual(REQUEST_CONFIG_SUCCESS)
        })
        it('Возвращает правильный payload', () => {
            const action = requestConfigSuccess(config)

            expect(action.payload.config).toEqual(config)
        })
    })
    describe('Проверка экшена requestConfigFail', () => {
        it('Генирирует правильное событие', () => {
            const action = requestConfigFail(error)

            expect(action.type).toEqual(REQUEST_CONFIG_FAIL)
        })
        it('Возвращает правильный payload', () => {
            const action = requestConfigFail(error)

            expect(action.payload.error).toEqual(error)
        })
    })
    describe('Проверка экшена changeRootPage', () => {
        it('Генирирует правильное событие', () => {
            const action = changeRootPage('Page_ID')

            expect(action.type).toEqual(CHANGE_ROOT_PAGE)
        })
        it('Возвращает правильный payload', () => {
            const action = changeRootPage('Page_ID')

            expect(action.payload.rootPageId).toEqual('Page_ID')
        })
    })
})
