import {
    METADATA_REQUEST,
    METADATA_SUCCESS,
    METADATA_FAIL,
    MAP_URL,
    RESET,
    DISABLE,
    ENABLE,
    SET_STATUS,
} from '../constants/pages'

import {
    metadataRequest,
    metadataSuccess,
    metadataFail,
    mapUrl,
    resetPage,
    disablePage,
    enablePage,
    setStatus,
} from './pages'

const pageId = 'Page.Widget'
const rootPage = 'Root.Page'
const pageUrl = '/page-url'
const mapping = {
    a: 1,
    b: 2,
}
const json = JSON.stringify({
    mapping,
})
const err = {
    text: 'Not found',
    status: 404,
}

describe('Тесты экшенов pages', () => {
    describe('Проверка экшена metadataRequest', () => {
        it('Генирирует правильное событие', () => {
            const action = metadataRequest(pageId, rootPage, pageUrl, mapping)

            expect(action.type).toEqual(METADATA_REQUEST)
        })
        it('Возвращает правильный payload', () => {
            const action = metadataRequest(pageId, rootPage, pageUrl, mapping)

            expect(action.payload.pageId).toEqual(pageId)
            expect(action.payload.rootPage).toEqual(rootPage)
            expect(action.payload.pageUrl).toEqual(pageUrl)
            expect(action.payload.mapping).toEqual(mapping)
        })
    })

    describe('Проверка экшена metadataSuccess', () => {
        it('Генирирует правильное событие', () => {
            const action = metadataSuccess(pageId, json)

            expect(action.type).toEqual(METADATA_SUCCESS)
        })
        it('Возвращает правильный payload', () => {
            const action = metadataSuccess(pageId, json)

            expect(action.payload.pageId).toEqual(pageId)
            expect(action.payload.json).toEqual(json)
        })
    })

    describe('Проверка экшена metadataFail', () => {
        it('Генирирует правильное событие', () => {
            const action = metadataFail(pageId, err)

            expect(action.type).toEqual(METADATA_FAIL)
        })
        it('Возвращает правильный payload', () => {
            const action = metadataFail(pageId, err)

            expect(action.payload.pageId).toEqual(pageId)
            expect(action.payload.err).toEqual(err)
        })
    })

    describe('Проверка экшена mapUrl', () => {
        it('Генирирует правильное событие', () => {
            const action = mapUrl(pageId)

            expect(action.type).toEqual(MAP_URL)
        })
        it('Возвращает правильный payload', () => {
            const action = mapUrl(pageId)

            expect(action.payload.pageId).toEqual(pageId)
        })
    })

    describe('Проверка экшена resetPage', () => {
        it('Генирирует правильное событие', () => {
            const action = resetPage(pageId)

            expect(action.type).toEqual(RESET)
        })
        it('Возвращает правильный payload', () => {
            const action = resetPage(pageId)

            expect(action.payload.pageId).toEqual(pageId)
        })
    })

    describe('Проверка экшена disablePage', () => {
        it('Генерирует правильное событие', () => {
            const action = disablePage(pageId)

            expect(action.payload.pageId).toEqual(pageId)
        })
        it('Возвращает правильный payload', () => {
            const action = disablePage(pageId)

            expect(action.type).toEqual(DISABLE)
        })

        describe('Проверка экшена disablePage', () => {
            it('Генерирует правильное событие', () => {
                const action = enablePage(pageId)

                expect(action.type).toEqual(ENABLE)
            })
            it('Возвращает правильный payload', () => {
                const action = enablePage(pageId)

                expect(action.payload.pageId).toEqual(pageId)
            })
        })

        describe('Проверка экшена setStatus', () => {
            it('Генирирует правильное событие', () => {
                const action = setStatus(pageId, 404)

                expect(action.type).toEqual(SET_STATUS)
            })
            it('Возвращает правильный payload', () => {
                const action = setStatus(pageId, 404)

                expect(action.payload.pageId).toEqual(pageId)
                expect(action.payload.status).toEqual(404)
            })
        })
    })
})
