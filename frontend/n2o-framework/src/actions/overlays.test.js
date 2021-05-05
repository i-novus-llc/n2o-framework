import {
    INSERT_MODAL,
    DESTROY,
    HIDE,
    SHOW,
    CLOSE,
    SHOW_PROMPT,
    HIDE_PROMPT,
} from '../constants/overlays'

import {
    insertOverlay,
    showOverlay,
    hideOverlay,
    destroyOverlay,
    closeOverlay,
    hidePrompt,
    showPrompt,
} from './overlays'

const name = 'MODAL_NAME'

describe('Тесты экшенов overlays', () => {
    describe('Проверка экшена insertOverlay', () => {
        it('Генирирует правильное событие', () => {
            const action = insertOverlay(name, true, 'TITLE', {
                size: 'lg',
                visible: true,
                pageId: 'page_id',
                widgetId: 'TableWidget',
            })

            expect(action.type).toEqual(INSERT_MODAL)
        })
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
        it('Генирирует правильное событие', () => {
            const action = showOverlay(name)

            expect(action.type).toEqual(SHOW)
        })
        it('Возвращает правильный payload', () => {
            const action = showOverlay(name)

            expect(action.payload.name).toEqual(name)
        })
    })

    describe('Проверка экшена hideOverlay', () => {
        it('Генирирует правильное событие', () => {
            const action = hideOverlay(name)

            expect(action.type).toEqual(HIDE)
        })
        it('Возвращает правильный payload', () => {
            const action = hideOverlay(name)

            expect(action.payload.name).toEqual(name)
        })
    })

    describe('Проверка экшена destroyOverlay', () => {
        it('Генирирует правильное событие', () => {
            const action = destroyOverlay()

            expect(action.type).toEqual(DESTROY)
        })
    })

    describe('Проверка экшена closeOverlay', () => {
        it('Генирирует правильное событие', () => {
            const action = closeOverlay('test', true)

            expect(action.type).toEqual(CLOSE)
        })
        it('Возвращает правильный payload', () => {
            const action = closeOverlay('test', true)

            expect(action.payload).toEqual({
                name: 'test',
                prompt: true,
            })
        })
    })

    describe('Проверка экшена showPrompt', () => {
        it('Генирирует правильное событие', () => {
            const action = showPrompt('test', true)

            expect(action.type).toEqual(SHOW_PROMPT)
        })
        it('Возвращает правильный payload', () => {
            const action = showPrompt('test')

            expect(action.payload).toEqual({
                name: 'test',
            })
        })
    })

    describe('Проверка экшена hidePrompt', () => {
        it('Генирирует правильное событие', () => {
            const action = hidePrompt('test', true)

            expect(action.type).toEqual(HIDE_PROMPT)
        })
        it('Возвращает правильный payload', () => {
            const action = hidePrompt('test')

            expect(action.payload).toEqual({
                name: 'test',
            })
        })
    })
})
