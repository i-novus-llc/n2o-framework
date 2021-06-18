import {
    HIDE_FIELD,
    SHOW_FIELD,
    DISABLE_FIELD,
    ENABLE_FIELD,
    HIDE_FIELDS,
    SHOW_FIELDS,
    ENABLE_FIELDS,
    DISABLE_FIELDS,
    REGISTER_FIELD_EXTRA,
    ADD_FIELD_MESSAGE,
    REMOVE_FIELD_MESSAGE,
    REGISTER_DEPENDENCY,
    SET_FIELD_FILTER,
    SET_LOADING,
} from '../constants/formPlugin'

import {
    hideField,
    showField,
    enableField,
    disableField,
    hideFields,
    showFields,
    enableFields,
    disableFields,
    addFieldMessage,
    removeFieldMessage,
    registerFieldExtra,
    registerFieldDependency,
    setFilterValue,
    setLoading,
} from './formPlugin'

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
        it('Генирирует правильное событие', () => {
            const action = hideField(form, name)
            expect(action.type).toEqual(HIDE_FIELD)
        })
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
        it('Генирирует правильное событие', () => {
            const action = showField(form, name)
            expect(action.type).toEqual(SHOW_FIELD)
        })
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
        it('Генирирует правильное событие', () => {
            const action = enableField(form, name)
            expect(action.type).toEqual(ENABLE_FIELD)
        })
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
        it('Генирирует правильное событие', () => {
            const action = disableField(form, name)
            expect(action.type).toEqual(DISABLE_FIELD)
        })
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

    describe('Проверка экшена enableFields', () => {
        it('Генирирует правильное событие', () => {
            const action = enableFields(form, names)
            expect(action.type).toEqual(ENABLE_FIELDS)
        })
        it('Проверяет правильность payload', () => {
            const action = enableFields(form, names)
            expect(action.payload.names).toEqual(names)
            expect(action.payload.form).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = enableFields(form, names)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена disableFields', () => {
        it('Генирирует правильное событие', () => {
            const action = disableFields(form, names)
            expect(action.type).toEqual(DISABLE_FIELDS)
        })
        it('Проверяет правильность payload', () => {
            const action = disableFields(form, names)
            expect(action.payload.names).toEqual(names)
            expect(action.payload.form).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = disableFields(form, names)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена showFields', () => {
        it('Генирирует правильное событие', () => {
            const action = showFields(form, names)
            expect(action.type).toEqual(SHOW_FIELDS)
        })
        it('Проверяет правильность payload', () => {
            const action = showFields(form, names)
            expect(action.payload.names).toEqual(names)
            expect(action.payload.form).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = showFields(form, names)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена hideFields', () => {
        it('Генирирует правильное событие', () => {
            const action = hideFields(form, names)
            expect(action.type).toEqual(HIDE_FIELDS)
        })
        it('Проверяет правильность payload', () => {
            const action = hideFields(form, names)
            expect(action.payload.names).toEqual(names)
            expect(action.payload.form).toEqual(form)
        })
        it('Проверяет правильность meta', () => {
            const action = hideFields(form, names)
            expect(action.meta.form).toEqual(form)
        })
    })

    describe('Проверка экшена addFieldMessage', () => {
        it('Генирирует правильное событие', () => {
            const action = addFieldMessage(form, name, message)
            expect(action.type).toEqual(ADD_FIELD_MESSAGE)
        })
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
        it('Генирирует правильное событие', () => {
            const action = removeFieldMessage(form, name)
            expect(action.type).toEqual(REMOVE_FIELD_MESSAGE)
        })
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
        it('Генирирует правильное событие', () => {
            const action = registerFieldExtra(form, name)
            expect(action.type).toEqual(REGISTER_FIELD_EXTRA)
        })
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
        it('Генирирует правильное событие', () => {
            const action = registerFieldDependency(form, name, dependency)
            expect(action.type).toEqual(REGISTER_DEPENDENCY)
        })
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
        it('Генирирует правильное событие', () => {
            const action = setFilterValue(form, name, filter)
            expect(action.type).toEqual(SET_FIELD_FILTER)
        })
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
        it('Генирирует правильное событие', () => {
            const action = setLoading(form, name, true)
            expect(action.type).toEqual(SET_LOADING)
        })
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
