import {
    SET,
    REMOVE,
    SYNC,
    COPY,
    UPDATE,
    UPDATE_MAP,
    REMOVE_ALL,
} from '../constants/models'

import {
    setModel,
    updateModel,
    removeModel,
    syncModel,
    copyModel,
    updateMapModel,
    removeAllModel,
} from './models'

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
        it('Генирирует правильное событие', () => {
            const action = setModel(prefix, key, model)
            expect(action.type).toEqual(SET)
        })
        it('Возвращает правильный payload', () => {
            const action = setModel(prefix, key, model)
            expect(action.payload.prefix).toEqual(prefix)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.model).toEqual(model)
        })
    })

    describe('Проверка экшена updateModel', () => {
        it('Генирирует правильное событие', () => {
            const action = updateModel(prefix, key, field, value)
            expect(action.type).toEqual(UPDATE)
        })
        it('Возвращает правильный payload', () => {
            const action = updateModel(prefix, key, field, value)
            expect(action.payload.prefix).toEqual(prefix)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.field).toEqual(field)
            expect(action.payload.value).toEqual(value)
        })
    })

    describe('Проверка экшена removeModel', () => {
        it('Генирирует правильное событие', () => {
            const action = removeModel(prefix, key)
            expect(action.type).toEqual(REMOVE)
        })
        it('Возвращает правильный payload', () => {
            const action = removeModel(prefix, key)
            expect(action.payload.prefix).toEqual(prefix)
            expect(action.payload.key).toEqual(key)
        })
    })

    describe('Проверка экшена syncModel', () => {
        it('Генирирует правильное событие', () => {
            const action = syncModel(prefix, keys, model)
            expect(action.type).toEqual(SYNC)
        })
        it('Возвращает правильный payload', () => {
            const action = syncModel(prefix, keys, model)
            expect(action.payload.prefix).toEqual(prefix)
            expect(action.payload.keys).toEqual(keys)
            expect(action.payload.model).toEqual(model)
        })
    })

    describe('Проверка экшена copyModel', () => {
        it('Генирирует правильное событие', () => {
            const action = copyModel(source, target, 'replace')
            expect(action.type).toEqual(COPY)
        })
        it('Возвращает правильный payload', () => {
            const action = copyModel(source, target, { mode: 'replace' })
            expect(action.payload.source).toEqual(source)
            expect(action.payload.target).toEqual(target)
            expect(action.payload.mode).toEqual('replace')
        })
    })

    describe('Проверка экшена updateMapModel', () => {
        it('Генирирует правильное событие', () => {
            const action = updateMapModel(prefix, key, field, value, map)
            expect(action.type).toEqual(UPDATE_MAP)
        })
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
        it('Генирирует правильное событие', () => {
            const action = removeAllModel(key)
            expect(action.type).toEqual(REMOVE_ALL)
        })
        it('Возвращает правильный payload', () => {
            const action = removeAllModel(key)
            expect(action.payload.key).toEqual(key)
        })
    })
})
