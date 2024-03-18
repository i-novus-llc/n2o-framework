import {
    changeButtonVisibility,
    changeButtonMessage,
    changeButtonDisabled,
} from '../store'

import {
    resolveButton,
} from '../sagas'

const setupResolveButton = () => resolveButton({
    conditions: {
        visible: [
            {
                expression: 'test === \'test\'',
                modelLink: 'model',
            },
        ],
        enabled: [
            {
                expression: 'test !== \'test\'',
                modelLink: 'model',
                message: 'test message',
            },
        ],
    },
})

describe('Проверка саги toolbar', () => {
    it('Тестирование вызова  экшена на саге', () => {
        const gen = setupResolveButton()
        gen.next()
        let { value } = gen.next({ model: { test: 'test' } })
        expect(value.payload.action.type).toEqual(changeButtonVisibility.type)
        expect(value.payload.action.payload.visible).toBe(true)
        gen.next()
        value = gen.next().value
        expect(value.payload.action.type).toEqual(changeButtonDisabled.type)
        expect(value.payload.action.payload.disabled).toBe(true)
        value = gen.next().value
        expect(value.payload.action.type).toEqual(changeButtonMessage.type)
        expect(value.payload.action.payload.message).toBe('test message')
        gen.next()
        expect(gen.next().done).toBe(true)
    })
})
