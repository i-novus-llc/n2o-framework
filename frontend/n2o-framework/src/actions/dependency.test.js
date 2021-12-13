import {
    REGISTER_DEPENDENCY,
} from '../constants/dependency'

import { registerDependency } from './dependency'

describe('Тесты для экшенов dependency', () => {
    it('registerDependency генирирует правильное событие', () => {
        const action = registerDependency('testWidget', {
            fetch: [
                {
                    on: ['models.resolve.test'],
                    condition: 'name !== "Мария"',
                },
            ],
        })
        expect(action.type).toEqual(REGISTER_DEPENDENCY)
    })

    it('registerDependency возвращает правильный payload', () => {
        const action = registerDependency('testWidget', {
            fetch: [
                {
                    on: ['models.resolve.test'],
                    condition: 'name !== "Мария"',
                },
            ],
        })
        expect(action.payload).toEqual({
            dependency: {
                fetch: [
                    {
                        on: ['models.resolve.test'],
                        condition: 'name !== "Мария"',
                    },
                ],
            },
            widgetId: 'testWidget',
        })
    })
})
