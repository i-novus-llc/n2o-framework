import React from 'react'
import { Provider } from 'react-redux'
import { change, actionTypes } from 'redux-form'
import pick from 'lodash/pick'
import get from 'lodash/get'
import omit from 'lodash/omit'
import set from 'lodash/set'
import configureMockStore from 'redux-mock-store'
import { call, put } from 'redux-saga/effects'

import {
    DISABLE_FIELD,
    ENABLE_FIELD,
    REGISTER_DEPENDENCY,
    HIDE_FIELD,
    SHOW_FIELD,
    SET_FIELD_FILTER,
} from '../../constants/formPlugin'
import { showField, hideField, enableField, disableField } from '../../actions/formPlugin'
import formPluginReducer from '../../reducers/formPlugin'
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
            visible: true,
            disabled: false,
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
    fieldName: 'field2',
    dispatch: () => {},
    actionType: actionTypes.INITIALIZE,
}

const actions = [
    {
        type: DISABLE_FIELD,
        payload: {
            name: 'field1',
            form: 'mockForm',
        },
        meta: { form: 'mockForm' },
    },

    {
        type: ENABLE_FIELD,
        payload: {
            name: 'field1',
            form: 'mockForm',
        },
        meta: { form: 'mockForm' },
    },
    {
        type: HIDE_FIELD,
        payload: {
            name: 'field1',
            form: 'mockForm',
        },
        meta: { form: 'mockForm' },
    },

    {
        type: SHOW_FIELD,
        payload: {
            name: 'field1',
            form: 'mockForm',
        },
        meta: { form: 'mockForm' },
    },
    {
        type: REGISTER_DEPENDENCY,
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
    {
        type: SET_FIELD_FILTER,
        payload: {
            name: 'field1',
            form: 'mockForm',
            filter: {
                filterId: 'test',
                value: 'test',
                resetMode: true,
            },
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
            call(
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
        expect(gen.next().value).toBe(undefined)
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
        expect(gen.next().value).toEqual(undefined)
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
                    keepDirty: false,
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
                    keepDirty: false,
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
            field1: pick(mockData.fields.field1, ['name', 'visible', 'disabled']),
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
    it('Тестирование экшенов блокировки', () => {
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
        expect(store.getActions()[0].type).toEqual(REGISTER_DEPENDENCY)
    })
})
