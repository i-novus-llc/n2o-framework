import { prepareEntity, resolveConditions } from './conditions'

describe('Проверка саги toolbar', () => {
    it('генератор prepareEntity должен вернуть массив кнопок по ключам modelLink', () => {
        const buttons = {
            someType: {
                'models.resolve[\'__patients-update\']': [
                    {
                        name: 'buttonsButton',
                    },
                ],
            },
        }
        const payload = {
            name: 'payloadButton',
            conditions: {
                visible: [
                    {
                        modelLink: 'models.resolve[\'__patients\']',
                    },
                ],
                enabled: [
                    {
                        modelLink: 'model.resolve[\'__contacts\']',
                    },
                ],
            },
        }

        expect(
            prepareEntity(
                Object.defineProperties({}, Object.getOwnPropertyDescriptors(buttons)),
                payload,
                'someType',
            ),
        ).toEqual({
            someType: {
                ...buttons.someType,
                'model.resolve[\'__contacts\']': [
                    {
                        name: 'payloadButton',
                        conditions: {
                            visible: [
                                {
                                    modelLink: 'models.resolve[\'__patients\']',
                                },
                            ],
                            enabled: [
                                {
                                    modelLink: 'model.resolve[\'__contacts\']',
                                },
                            ],
                        },
                    },
                ],
            },
        })
    })
    it('Тестирование resolveConditions', () => {
        expect(
            resolveConditions(
                [
                    {
                        expression: 'test === \'test\'',
                        modelLink: 'model',
                    },
                ],
                { model: { test: 'test' } },
            ).resolve,
        ).toBe(true)
        expect(
            resolveConditions(
                [
                    {
                        expression: 'test === \'test\'',
                        modelLink: 'no_model',
                    },
                ],
                { model: { test: 'test' } },
            ).resolve,
        ).toBe(false)
    })
    it('Тестирование resolveConditions на null condition', () => {
        expect(
            resolveConditions(
                [
                    {
                        expression: 'test === \'test\'',
                        modelLink: 'model',
                    },
                ],
                null,
            ).resolve,
        ).toBe(false)
    })
})
