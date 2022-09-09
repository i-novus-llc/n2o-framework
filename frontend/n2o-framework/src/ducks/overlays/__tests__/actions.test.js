import {
    insertOverlay,
    hideOverlay,
    showOverlay,
    closeOverlay,
    showPrompt,
    hidePrompt,
} from '../store'

const name = 'MODAL_NAME'

describe('Тесты экшенов overlays', () => {
    describe('Проверка экшена insertOverlay', () => {
        it('Возвращает правильный payload', () => {
            const action = insertOverlay(name, true, 'modal', {
                title: 'TITLE',
                size: 'lg',
                visible: true,
                pageId: 'page_id',
                widgetId: 'TableWidget',
            })
            expect(action.payload).toMatchObject({
                mode: 'modal',
                name: 'MODAL_NAME',
                visible: true,
                pageId: 'page_id',
                size: 'lg',
                title: 'TITLE',
                widgetId: 'TableWidget',
            })
        })
    })

    describe('Проверка экшена showOverlay', () => {
        it('Возвращает правильный payload', () => {
            const action = showOverlay(name)
            expect(action.payload).toEqual(name)
        })
    })

    describe('Проверка экшена hideOverlay', () => {
        it('Возвращает правильный payload', () => {
            const action = hideOverlay(name)
            expect(action.payload).toEqual(name)
        })
    })

    describe('Проверка экшена closeOverlay', () => {
        it('Возвращает правильный payload', () => {
            const action = closeOverlay('test', true)
            expect(action.payload).toEqual({
                name: 'test',
                prompt: true,
            })
        })
    })

    describe('Проверка экшена showPrompt', () => {
        it('Возвращает правильный payload', () => {
            const action = showPrompt('test')
            expect(action.payload).toEqual('test')
        })
    })

    describe('Проверка экшена hidePrompt', () => {
        it('Возвращает правильный payload', () => {
            const action = hidePrompt('test')
            expect(action.payload).toEqual('test')
        })
    })
})
