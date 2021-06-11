import {
    hideField,
    showField,
    enableField,
    disableField,
    hideMultiFields,
    showMultiFields,
    enableMultiFields,
    disableMultiFields,
    addFieldMessage,
    removeFieldMessage,
    registerFieldExtra,
    registerFieldDependency,
    setFilterValue,
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
            expect(action.payload.form).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = hideField(form, name)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена showField', () => {
        it('Проверяет правильность payload', () => {
            const action = showField(form, name)
            expect(action.payload.name).toEqual(name)
            expect(action.payload.form).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = showField(form, name)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена enableField', () => {
        it('Проверяет правильность payload', () => {
            const action = enableField(form, name)
            expect(action.payload.name).toEqual(name)
            expect(action.payload.form).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = enableField(form, name)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена disableField', () => {
        it('Проверяет правильность payload', () => {
            const action = disableField(form, name)
            expect(action.payload.name).toEqual(name)
            expect(action.payload.form).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = disableField(form, name)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена enableMultiFields', () => {
        it('Проверяет правильность payload', () => {
            const action = enableMultiFields(form, names)
            expect(action.payload.names).toEqual(names)
            expect(action.payload.form).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = enableMultiFields(form, names)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена disableMultiFields', () => {
        it('Проверяет правильность payload', () => {
            const action = disableMultiFields(form, names)
            expect(action.payload.names).toEqual(names)
            expect(action.payload.form).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = disableMultiFields(form, names)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена showMultiFields', () => {
        it('Проверяет правильность payload', () => {
            const action = showMultiFields(form, names)
            expect(action.payload.names).toEqual(names)
            expect(action.payload.form).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = showMultiFields(form, names)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена hideMultiFields', () => {
        it('Проверяет правильность payload', () => {
            const action = hideMultiFields(form, names)
            expect(action.payload.names).toEqual(names)
            expect(action.payload.form).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = hideMultiFields(form, names)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена addFieldMessage', () => {
        it('Проверяет правильность payload', () => {
            const action = addFieldMessage(form, name, message)
            expect(action.payload.name).toEqual(name)
            expect(action.payload.form).toEqual(form)
            expect(action.payload.message).toEqual(message)
        })
        it('Проверяет правильность meta', () => {
            const action = addFieldMessage(form, name, message)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена removeFieldMessage', () => {
        it('Проверяет правильность payload', () => {
            const action = removeFieldMessage(form, name)
            expect(action.payload.name).toEqual(name)
            expect(action.payload.form).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = removeFieldMessage(form, name)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена registerFieldExtra', () => {
        it('Проверяет правильность payload', () => {
            const action = registerFieldExtra(form, name, {
                visible: false,
                disabled: true,
            })
            expect(action.payload.name).toEqual(name)
            expect(action.payload.form).toEqual(form)
            expect(action.payload.initialState).toEqual({
                visible: false,
                disabled: true,
            })
        })
        it('Проверяет правильность meta', () => {
            const action = registerFieldExtra(form, name)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена registerFieldDependency', () => {
        it('Проверяет правильность payload', () => {
            const action = registerFieldDependency(form, name, dependency)
            expect(action.payload.name).toEqual(name)
            expect(action.payload.form).toEqual(form)
            expect(action.payload.dependency).toEqual(dependency)
        })
        it('Проверяет правильность meta', () => {
            const action = registerFieldDependency(form, name, dependency)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена setFilterValue', () => {
        it('Проверяет правильность payload', () => {
            const action = setFilterValue(form, name, filter)
            expect(action.payload.name).toEqual(name)
            expect(action.payload.form).toEqual(form)
            expect(action.payload.filter).toEqual(filter)
        })
        it('Проверяет правильность meta', () => {
            const action = setFilterValue(form, name, filter)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена setLoading', () => {
        it('Проверяет правильность payload', () => {
            let action = setLoading(form, name, true)
            expect(action.payload.form).toBe(form)
            expect(action.payload.name).toBe(name)
            expect(action.payload.loading).toBe(true)

            action = setLoading(form, name, false)
            expect(action.payload.loading).toBe(false)
        })
    })
})
