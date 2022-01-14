import { START_INVOKE } from '../constants/actionImpls'
import { MODEL_PREFIX } from '../core/datasource/const'

import { startInvoke } from './actionImpl'

const datasource = 'datasource_id'
const dataProvider = {
    url: '/n2o/test',
    pathMapping: {},
    queryMapping: {},
}
const page_id = 'page_id'
const model = MODEL_PREFIX.active

describe('Тесты для экшена actionImpl', () => {
    it('Генирирует правильное событие', () => {
        const action = startInvoke(datasource, dataProvider, model, page_id)
        expect(action.type).toEqual(START_INVOKE)
    })
    it('Проверяет правильность возвращаемых данных', () => {
        const action = startInvoke(datasource, dataProvider, model, page_id)
        expect(action.payload.datasource).toEqual(datasource)
        expect(action.payload.dataProvider).toEqual(dataProvider)
        expect(action.payload.model).toEqual(model)
        expect(action.payload.pageId).toEqual(page_id)
    })
    it('Проверяет правильность меты', () => {
        const action = startInvoke(datasource, dataProvider, model, page_id, { refresh: true })
        expect(action.meta.refresh).toEqual(true)
    })
})
