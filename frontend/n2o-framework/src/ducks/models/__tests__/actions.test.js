import {
    setModel,
    updateModel,
    removeModel,
    syncModel,
    copyModel,
    updateMapModel,
    removeAllModel,
} from '../store'

const prefix = 'datasource'
const key = 'Page.Widget'
const keys = ['Page.Widget1', 'Page.Widget2']
const model = {
    id: 1,
    name: 'Test',
}
const field = 'field'
const value = 'new value'
const map = 'map'
const source = {
    prefix: 'resolve',
    key: 'testKey',
}
const target = {
    prefix: 'edit',
    key: 'testKey',
}

describe('Тесты экшенов models', () => {
    describe('Проверка экшена setModel', () => {
        it('Возвращает правильный payload', () => {
            const action = setModel(prefix, key, model)
            expect(action.payload.prefix).toEqual(prefix)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.model).toEqual(model)
        })
    })

    describe('Проверка экшена updateModel', () => {
        it('Возвращает правильный payload', () => {
            const action = updateModel(prefix, key, field, value)
            expect(action.payload.prefix).toEqual(prefix)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.field).toEqual(field)
            expect(action.payload.value).toEqual(value)
        })
    })

    describe('Проверка экшена removeModel', () => {
        it('Возвращает правильный payload', () => {
            const action = removeModel(prefix, key)
            expect(action.payload.prefix).toEqual(prefix)
            expect(action.payload.key).toEqual(key)
        })
    })

    describe('Проверка экшена syncModel', () => {
        it('Возвращает правильный payload', () => {
            const action = syncModel(prefix, keys, model)
            expect(action.payload.prefix).toEqual(prefix)
            expect(action.payload.keys).toEqual(keys)
            expect(action.payload.model).toEqual(model)
        })
    })

    describe('Проверка экшена copyModel', () => {
        it('Возвращает правильный payload', () => {
            const action = copyModel(source, target, { mode: 'replace' })
            expect(action.payload.source).toEqual(source)
            expect(action.payload.target).toEqual(target)
            expect(action.payload.mode).toEqual('replace')
        })
    })

    describe('Проверка экшена updateMapModel', () => {
        it('Возвращает правильный payload', () => {
            const action = updateMapModel(prefix, key, field, value, map)
            expect(action.payload.prefix).toEqual(prefix)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.field).toEqual(field)
            expect(action.payload.value).toEqual(value)
            expect(action.payload.map).toEqual(map)
        })
    })

    describe('Проверка экшена removeAllModel', () => {
        it('Возвращает правильный payload', () => {
            const action = removeAllModel(key)
            expect(action.payload.key).toEqual(key)
        })
    })
})
