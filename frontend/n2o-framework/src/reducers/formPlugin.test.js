import {
    DISABLE_FIELD,
    ENABLE_FIELD,
    SHOW_FIELD,
    HIDE_FIELD,
    REGISTER_FIELD_EXTRA,
    ADD_FIELD_MESSAGE,
    REMOVE_FIELD_MESSAGE,
    REGISTER_DEPENDENCY,
    SET_FIELD_FILTER,
    SET_REQUIRED,
    UNSET_REQUIRED,
    SET_LOADING,
} from '../constants/formPlugin'

import formPlugin from './formPlugin'

describe('Тесты formPlugin reducer', () => {
    it('Проверка DISABLE_FIELD', () => {
        expect(
            formPlugin(
                {},
                {
                    type: DISABLE_FIELD,
                    payload: {
                        name: 'testName',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    disabled: true,
                    filter: [],
                    isInit: true,
                    message: null,
                    visible: true,
                    dependency: null,
                    required: false,
                    loading: false,
                },
            },
        })
    })

    it('Проверка ENABLE_FIELD', () => {
        expect(
            formPlugin(
                {},
                {
                    type: ENABLE_FIELD,
                    payload: {
                        name: 'testName',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    disabled: false,
                    filter: [],
                    isInit: true,
                    message: null,
                    visible: true,
                    dependency: null,
                    required: false,
                    loading: false,
                },
            },
        })
    })

    it('Проверка SHOW_FIELD', () => {
        expect(
            formPlugin(
                {},
                {
                    type: SHOW_FIELD,
                    payload: {
                        name: 'testName',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    disabled: false,
                    filter: [],
                    isInit: true,
                    message: null,
                    visible: true,
                    dependency: null,
                    required: false,
                    loading: false,
                },
            },
        })
    })

    it('Проверка HIDE_FIELD', () => {
        expect(
            formPlugin(
                {},
                {
                    type: HIDE_FIELD,
                    payload: {
                        name: 'testName',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    disabled: false,
                    filter: [],
                    isInit: true,
                    message: null,
                    visible: false,
                    dependency: null,
                    required: false,
                    loading: false,
                },
            },
        })
    })

    it('Проверка ADD_FIELD_MESSAGE', () => {
        expect(
            formPlugin(
                {},
                {
                    type: ADD_FIELD_MESSAGE,
                    payload: {
                        message: ['message'],
                        name: 'testName',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    disabled: false,
                    filter: [],
                    isInit: true,
                    message: {
                        0: 'message',
                    },
                    visible: true,
                    dependency: null,
                    required: false,
                    loading: false,
                },
            },
        })
    })

    it('Проверка REMOVE_FIELD_MESSAGE', () => {
        expect(
            formPlugin(
                {},
                {
                    type: REMOVE_FIELD_MESSAGE,
                    payload: {
                        message: ['message'],
                        name: 'testName',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    disabled: false,
                    filter: [],
                    isInit: true,
                    message: null,
                    visible: true,
                    dependency: null,
                    required: false,
                    loading: false,
                },
            },
        })
    })

    it('Проверка REGISTER_FIELD_EXTRA', () => {
        expect(
            formPlugin(
                {},
                {
                    type: REGISTER_FIELD_EXTRA,
                    payload: {
                        name: 'testName',
                        initialState: {
                            visible: false,
                            disabled: true,
                        },
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    disabled: true,
                    filter: [],
                    isInit: true,
                    message: null,
                    visible: false,
                    dependency: null,
                    required: false,
                    loading: false,
                },
            },
        })
    })

    it('Проверка REGISTER_DEPENDENCY', () => {
        expect(
            formPlugin(
                {},
                {
                    type: REGISTER_DEPENDENCY,
                    payload: {
                        name: 'testName',
                        dependency: 'dependency',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    disabled: false,
                    filter: [],
                    isInit: true,
                    message: null,
                    visible: true,
                    required: false,
                    dependency: 'dependency',
                    loading: false,
                },
            },
        })
    })

    it('Проверка SET_FIELD_FILTER', () => {
        expect(
            formPlugin(
                {},
                {
                    type: SET_FIELD_FILTER,
                    payload: {
                        name: 'testName',
                        filter: [
                            {
                                'filter.name': 'Oleg',
                            },
                        ],
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    dependency: null,
                    required: false,
                    disabled: false,
                    isInit: true,
                    message: null,
                    visible: true,
                    loading: false,
                    filter: [
                        {
                            'filter.name': 'Oleg',
                        },
                    ],
                },
            },
        })
    })

    it('Проверка SHOW_FIELD', () => {
        expect(
            formPlugin(
                {},
                {
                    type: SET_REQUIRED,
                    payload: {
                        name: 'testName',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    disabled: false,
                    filter: [],
                    isInit: true,
                    message: null,
                    visible: true,
                    dependency: null,
                    required: true,
                    loading: false,
                },
            },
        })
    })

    it('Проверка UNSET_REQUIRED', () => {
        expect(
            formPlugin(
                {},
                {
                    type: UNSET_REQUIRED,
                    payload: {
                        name: 'testName',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    disabled: false,
                    filter: [],
                    isInit: true,
                    message: null,
                    visible: true,
                    dependency: null,
                    required: false,
                    loading: false,
                },
            },
        })
    })
    it('Проверка если название через точку на примере SHOW_FIELD', () => {
        expect(
            formPlugin(
                {},
                {
                    type: SHOW_FIELD,
                    payload: {
                        name: 'testName.id',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                'testName.id': {
                    disabled: false,
                    filter: [],
                    isInit: true,
                    message: null,
                    visible: true,
                    dependency: null,
                    required: false,
                    loading: false,
                },
            },
        })
    })
    it('Проверка SET_LOADING', () => {
        expect(
            formPlugin(
                {},
                {
                    type: SET_LOADING,
                    payload: {
                        form: 'testForm',
                        name: 'testName',
                        loading: true,
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    disabled: false,
                    filter: [],
                    isInit: true,
                    message: null,
                    visible: true,
                    dependency: null,
                    required: false,
                    loading: true,
                },
            },
        })
    })
})
