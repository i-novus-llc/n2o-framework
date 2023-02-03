import React from 'react'
import { Provider } from 'react-redux'
import { change } from 'redux-form'
import pick from 'lodash/pick'
import get from 'lodash/get'
import omit from 'lodash/omit'
import set from 'lodash/set'
import configureMockStore from 'redux-mock-store'
import { fork, put } from 'redux-saga/effects'

import formPluginReducer, {
    showField,
    hideField,
    enableField,
    disableField,
    registerFieldDependency,
    registerFieldExtra,
} from '../../ducks/form/store'
import { checkAndModify, modify } from '../../sagas/fieldDependency'

import withDependency from './withDependency'

const mockStore = configureMockStore()

const mockData = {
    values: {
        field1: '',
        field2: '',
    },
    fields: {
        field1: {
            name: 'field1',
            visible_set: true,
            visible_field: true,
            visible: true,
            disabled: false,
            disabled_set: false,
            disabled_field: false,
            dependency: [
                {
                    applyOnInit: true,
                    type: 'visible',
                    on: ['field2'],
                    expression: 'field2 == \'test\'',
                },
            ],
        },
        field2: {
            name: 'field2',
            visible: true,
            disabled: false,
        },
    },
    formName: 'test',
    fieldName: 'field1',
    dispatch: () => {},
    actionType: registerFieldExtra.type,
}

const actions = [
    {
        type: disableField.type,
        payload: {
            name: 'field1',
            form: 'mockForm',
        },
        meta: { form: 'mockForm' },
    },

    {
        type: enableField.type,
        payload: {
            name: 'field1',
            form: 'mockForm',
        },
        meta: { form: 'mockForm' },
    },
    {
        type: hideField.type,
        payload: {
            name: 'field1',
            form: 'mockForm',
        },
        meta: { form: 'mockForm' },
    },

    {
        type: showField.type,
        payload: {
            name: 'field1',
            form: 'mockForm',
        },
        meta: { form: 'mockForm' },
    },
    {
        type: registerFieldDependency.type,
        payload: {
            name: 'field1',
            form: 'mockForm',
            dependency: [
                {
                    applyOnInit: true,
                    type: 'visible',
                    on: ['field2'],
                    expression: 'field2 == \'test\'',
                },
            ],
        },
        meta: { form: 'mockForm' },
    },
]

const REDUX_CHANGE = '@@redux-form/CHANGE'

const setup = mockData => checkAndModify(
    mockData.values,
    mockData.fields,
    mockData.formName,
    mockData.fieldName,
    mockData.actionType,
)

const setupModify = mockData => modify(
    mockData.values,
    mockData.formName,
    mockData.fields.field1.name,
    mockData.fields.field1.dependency[0],
    mockData.fields.field1,
)

describe('Тестирование саги', () => {
    it('Тестирование вызова функции экшена на саге', () => {
        const gen = setup(mockData)
        expect(gen.next().value).toEqual(
            fork(
                modify,
                mockData.values,
                mockData.formName,
                mockData.fields.field1.name,
                mockData.fields.field1.dependency[0],
                mockData.fields.field1,
            ),
        )
        expect(gen.next().done).toBe(true)
    })
    it('Экшен не вызывается при регистрации зависимости, если applyOnInit == false', () => {
        const gen = setup(
            omit(
                set(mockData, 'fields.field1.dependency[0].applyOnInit', false),
                'fieldName',
            ),
        )
        expect(gen.next().done).toBe(true)
    })
    it('Экшен вызывается при изменении значения формы', () => {
        set(mockData, 'fields.field1.dependency[0].applyOnInit', false)
        set(mockData, 'actionType', REDUX_CHANGE)
        set(mockData, 'fieldName', get(mockData, 'fields.field1.dependency[0].on[0]'))
        const gen = setup(mockData)
        expect(gen.next().done).toBe(false)
    })
    it('Проверка модификатора зависимостей', () => {
        let gen = setupModify({
            ...mockData,
            values: {
                field2: 'sadsa',
            },
        })
        expect(gen.next().value).toEqual(
            put(hideField(mockData.formName, mockData.fields.field1.name)),
        )
        expect(gen.next().done).toBe(true)
        set(mockData, 'values.field2', 'test')
        gen = setupModify(mockData)
        expect(gen.next().value).toEqual(
            put(showField(mockData.formName, mockData.fields.field1.name))
        )
        expect(gen.next().done).toBe(true)
    })
    it('Проверка модификатора enabled зависимости', () => {
        let gen
        /* Enabled */
        set(mockData, 'fields.field1.dependency[0].type', 'enabled')
        gen = setupModify({ ...mockData, values: { field2: 'test2' } })
        expect(gen.next().value).toEqual(
            put(disableField(mockData.formName, mockData.fields.field1.name)),
        )
        expect(gen.next().done).toBe(true)
        set(mockData, 'values.field2', 'test')
        gen = setupModify(mockData)
        expect(gen.next().value).toEqual(
            put(enableField(mockData.formName, mockData.fields.field1.name))
        )
        expect(gen.next().done).toBe(true)
    })
    it('Проверка модификатора setValue зависимости', () => {
        let gen
        /* SetValue */
        set(mockData, 'fields.field1.dependency[0].type', 'setValue')
        set(mockData, 'fields.field1.dependency[0].expression', 'field2')
        set(mockData, 'values.field2', 'test')
        gen = setupModify(mockData)
        expect(gen.next().value).toEqual(
            put(
                change(mockData.formName, mockData.fields.field1.name, {
                    keepDirty: true,
                    value: 'test',
                }),
            ),
        )
        expect(gen.next().done).toBe(true)
    })
    it('Проверка модификатора reset зависимости', () => {
        let gen
        /* SetValue */
        set(mockData, 'fields.field1.dependency[0].type', 'reset')
        set(mockData, 'fields.field1.dependency[0].expression', 'field2 == "test"')
        gen = setupModify(mockData)
        expect(gen.next().value).toEqual(
            put(
                change(mockData.formName, mockData.fields.field1.name, {
                    keepDirty: true,
                    value: null,
                }),
            ),
        )
        expect(gen.next().done).toBe(true)
    })
})

describe('Тестирование редюсера', () => {
    const initialState = {
        registeredFields: {
            field1: mockData.fields.field1,
        },
    }
    it('Тестирование регистрации зависимости', () => {
        expect(
            get(
                formPluginReducer(initialState, actions[4]),
                'registeredFields.field1.dependency',
            ),
        ).toBeTruthy()
    })

    it('Тестирование экшенов блокировки', () => {
        expect(
            get(
                formPluginReducer(initialState, actions[0]),
                'registeredFields.field1.disabled',
            ),
        ).toBe(true)
        expect(
            get(
                formPluginReducer(initialState, actions[1]),
                'registeredFields.field1.disabled',
            ),
        ).toBe(false)
    })
    it('Тестирование экшенов видимости', () => {
        expect(
            get(
                formPluginReducer(initialState, actions[2]),
                'registeredFields.field1.visible',
            ),
        ).toBe(false)
        expect(
            get(
                formPluginReducer(initialState, actions[3]),
                'registeredFields.field1.visible',
            ),
        ).toBe(true)
    })
})

describe('Тестирование HOC', () => {
    const initialState = {
        form: {
            mockForm: {
                registeredFields: {
                    field1: pick(mockData.fields.field1, ['name', 'visible', 'disabled']),
                },
            },
        },
    }
    it('Зависимость регистрируется', () => {
        const store = mockStore(initialState)
        const Component = withDependency({})('input')
        const wrapper = mount(
            <Provider store={store}>
                <Component
                    form="mockForm"
                    id="field1"
                    dependency={mockData.fields.field1.dependency}
                />
            </Provider>,
        )
        expect(store.getActions()).toHaveLength(1)
        expect(store.getActions()[0].type).toEqual(registerFieldDependency.type)
    })
})
