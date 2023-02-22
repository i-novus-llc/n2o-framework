import {
    hideField,
    showField,
    enableField,
    disableField,
    hideMultiFields,
    showMultiFields,
    enableMultiFields,
    disableMultiFields,
    registerFieldExtra,
    registerFieldDependency,
    setLoading,
} from '../store'

const form = {
    id: 'test',
}
const message = 'Hello world!'
const name = 'formName'
const names = ['field1', 'field2']
const filter = {
    'filter.name': 'Олег',
}
const dependency = {
    id: 'test',
    dependency: 'dependency',
}

describe('Тесты экшенов formPlugin', () => {
    describe('Проверка экшена hideField', () => {
        it('Проверяет правильность payload', () => {
            const action = hideField(form, name)
            expect(action.payload.name).toEqual(name)
            expect(action.payload.key).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = hideField(form, name)
            expect(action.meta.key).toEqual(form)
        })
    })

    describe('Проверка экшена showField', () => {
        it('Проверяет правильность payload', () => {
            const action = showField(form, name)
            expect(action.payload.name).toEqual(name)
            expect(action.payload.key).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = showField(form, name)
            expect(action.meta.key).toEqual(form)
        })
    })

    describe('Проверка экшена enableField', () => {
        it('Проверяет правильность payload', () => {
            const action = enableField(form, name)
            expect(action.payload.name).toEqual(name)
            expect(action.payload.key).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = enableField(form, name)
            expect(action.meta.key).toEqual(form)
        })
    })

    describe('Проверка экшена disableField', () => {
        it('Проверяет правильность payload', () => {
            const action = disableField(form, name)
            expect(action.payload.name).toEqual(name)
            expect(action.payload.key).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = disableField(form, name)
            expect(action.meta.key).toEqual(form)
        })
    })

    describe('Проверка экшена enableMultiFields', () => {
        it('Проверяет правильность payload', () => {
            const action = enableMultiFields(form, names)
            expect(action.payload.names).toEqual(names)
            expect(action.payload.key).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = enableMultiFields(form, names)
            expect(action.meta.key).toEqual(form)
        })
    })

    describe('Проверка экшена disableMultiFields', () => {
        it('Проверяет правильность payload', () => {
            const action = disableMultiFields(form, names)
            expect(action.payload.names).toEqual(names)
            expect(action.payload.key).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = disableMultiFields(form, names)
            expect(action.meta.key).toEqual(form)
        })
    })

    describe('Проверка экшена showMultiFields', () => {
        it('Проверяет правильность payload', () => {
            const action = showMultiFields(form, names)
            expect(action.payload.names).toEqual(names)
            expect(action.payload.key).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = showMultiFields(form, names)
            expect(action.meta.key).toEqual(form)
        })
    })

    describe('Проверка экшена hideMultiFields', () => {
        it('Проверяет правильность payload', () => {
            const action = hideMultiFields(form, names)
            expect(action.payload.names).toEqual(names)
            expect(action.payload.key).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = hideMultiFields(form, names)
            expect(action.meta.key).toEqual(form)
        })
    })

    describe('Проверка экшена registerFieldExtra', () => {
        it('Проверяет правильность payload', () => {
            const action = registerFieldExtra(form, name, {
                visible: false,
                disabled: true,
            })
            expect(action.payload.name).toEqual(name)
            expect(action.payload.key).toEqual(form)
            expect(action.payload.initialState).toEqual({
                visible: false,
                disabled: true,
            })
        })
        it('Проверяет правильность meta', () => {
            const action = registerFieldExtra(form, name)
            expect(action.meta.key).toEqual(form)
        })
    })

    describe('Проверка экшена registerFieldDependency', () => {
        it('Проверяет правильность payload', () => {
            const action = registerFieldDependency(form, name, dependency)
            expect(action.payload.name).toEqual(name)
            expect(action.payload.key).toEqual(form)
            expect(action.payload.dependency).toEqual(dependency)
        })
        it('Проверяет правильность meta', () => {
            const action = registerFieldDependency(form, name, dependency)
            expect(action.meta.key).toEqual(form)
        })
    })

    describe('Проверка экшена setLoading', () => {
        it('Проверяет правильность payload', () => {
            let action = setLoading(form, name, true)
            expect(action.payload.key).toBe(form)
            expect(action.payload.name).toBe(name)
            expect(action.payload.loading).toBe(true)

            action = setLoading(form, name, false)
            expect(action.payload.loading).toBe(false)
        })
    })
})
