import { START_INVOKE } from '../constants/actionImpls'

import { startInvoke } from './actionImpl'

/**
 * @deprecated
 */

const widgetId = 'Widget_Id'
const dataProvider = {
    url: '/n2o/test',
    pathMapping: {},
    queryMapping: {},
}
const data = {}
const modelLink = 'modelLink'

describe('Тесты для экшена actionImpl', () => {
    it('Генирирует правильное событие', () => {
        const action = startInvoke(widgetId, dataProvider, data, modelLink)

        expect(action.type).toEqual(START_INVOKE)
    })
    it('Проверяет правильность возвращаемых данных', () => {
        const action = startInvoke(widgetId, dataProvider, data, modelLink)

        expect(action.payload.widgetId).toEqual(widgetId)
        expect(action.payload.dataProvider).toEqual(dataProvider)
        expect(action.payload.data).toEqual(data)
        expect(action.payload.modelLink).toEqual(modelLink)
    })
    it('Проверяет правильность меты', () => {
        const action = startInvoke(widgetId, dataProvider, data, modelLink)

        expect(action.meta.refresh).toEqual(true)
    })
})
