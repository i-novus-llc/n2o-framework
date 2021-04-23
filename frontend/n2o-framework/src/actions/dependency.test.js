import {
    REGISTER_DEPENDENCY,
    UPDATE_WIDGET_DEPENDENCY,
} from '../constants/dependency'

import { registerDependency, updateWidgetDependency } from './dependency'

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

    it('updateWidgetDependency генирирует правильное событие', () => {
        const action = updateWidgetDependency('testWidget')
        expect(action.type).toEqual(UPDATE_WIDGET_DEPENDENCY)
    })

    it('updateWidgetDependency возвращает правильный payload', () => {
        const action = updateWidgetDependency('testWidget')
        expect(action.payload).toEqual({
            widgetId: 'testWidget',
        })
    })
})
