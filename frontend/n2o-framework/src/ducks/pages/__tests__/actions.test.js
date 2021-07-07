import {
    metadataRequest,
    metadataSuccess,
    metadataFail,
    mapUrl,
    resetPage,
    disablePage,
    enablePage,
    setStatus,
} from '../store'

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
        it('Возвращает правильный payload', () => {
            const action = metadataRequest(pageId, rootPage, pageUrl, mapping)
            expect(action.payload.pageId).toEqual(pageId)
            expect(action.payload.rootPage).toEqual(rootPage)
            expect(action.payload.pageUrl).toEqual(pageUrl)
            expect(action.payload.mapping).toEqual(mapping)
        })
    })

    describe('Проверка экшена metadataSuccess', () => {
        it('Возвращает правильный payload', () => {
            const action = metadataSuccess(pageId, json)
            expect(action.payload.pageId).toEqual(pageId)
            expect(action.payload.json).toEqual(json)
        })
    })

    describe('Проверка экшена metadataFail', () => {
        it('Возвращает правильный payload', () => {
            const action = metadataFail(pageId, err)
            expect(action.payload.pageId).toEqual(pageId)
            expect(action.payload.err).toEqual(err)
        })
    })

    describe('Проверка экшена mapUrl', () => {
        it('Возвращает правильный payload', () => {
            const action = mapUrl(pageId)
            expect(action.payload.pageId).toEqual(pageId)
        })
    })

    describe('Проверка экшена resetPage', () => {
        it('Возвращает правильный payload', () => {
            const action = resetPage(pageId)
            expect(action.payload).toEqual(pageId)
        })
    })

    describe('Проверка экшена disablePage', () => {
        it('Возвращает правильный payload', () => {
            const action = disablePage(pageId)
            expect(action.payload).toEqual(pageId)
        })

        describe('Проверка экшена enablePage', () => {
            it('Возвращает правильный payload', () => {
                const action = enablePage(pageId)
                expect(action.payload).toEqual(pageId)
            })
        })

        describe('Проверка экшена setStatus', () => {
            it('Возвращает правильный payload', () => {
                const action = setStatus(pageId, 404)
                expect(action.payload.pageId).toEqual(pageId)
                expect(action.payload.status).toEqual(404)
            })
        })
    })
})
