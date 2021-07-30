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

import formPlugin, { defaultState } from './formPlugin'

describe('Тесты formPlugin reducer', () => {
    it('Проверка DISABLE_FIELD', () => {
        expect(
            formPlugin(
                {
                    registeredFields: {
                        testName: {
                            ...defaultState,
                        },
                    },
                },
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
                    ...defaultState,
                    disabled: true,
                    disabled_field: true
                },
            },
        })
    })

    it('Проверка ENABLE_FIELD', () => {
        expect(
            formPlugin(
                {
                    registeredFields: {
                        testName: {
                            ...defaultState,
                            disabled: true,
                            disabled_field: true
                        },
                    },
                },
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
                    ...defaultState,
                },
            },
        })
    })

    it('Проверка SHOW_FIELD', () => {
        expect(
            formPlugin(
                {
                    registeredFields: {
                        testName: {
                            ...defaultState,
                            visible: false,
                            visible_field: false,
                        },
                    },
                },
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
                    ...defaultState,
                },
            },
        })
    })

    it('Проверка HIDE_FIELD', () => {
        expect(
            formPlugin(
                {
                    registeredFields: {
                        testName: {
                            ...defaultState,
                        },
                    },
                },
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
                    ...defaultState,
                    visible: false,
                    visible_field: false,
                },
            },
        })
    })

    it('Проверка ADD_FIELD_MESSAGE', () => {
        expect(
            formPlugin(
                {
                    registeredFields: {
                        testName: {
                            ...defaultState,
                        },
                    },
                },
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
                    ...defaultState,
                    message: {
                        0: 'message',
                    }
                },
            },
        })
    })

    it('Проверка REMOVE_FIELD_MESSAGE', () => {
        expect(
            formPlugin(
                {
                    registeredFields: {
                        testName: {
                            ...defaultState,
                            message: {
                                0: 'message',
                            }
                        },
                    },
                },
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
                    ...defaultState,
                },
            },
        })
    })

    it('Проверка REGISTER_FIELD_EXTRA', () => {
        expect(
            formPlugin(
                {
                    registeredFields: {
                        testName: {
                            ...defaultState,
                        },
                    },
                },
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
                    ...defaultState,
                    visible: false,
                    disabled: true,
                },
            },
        })
    })

    it('Проверка REGISTER_DEPENDENCY', () => {
        expect(
            formPlugin(
                {
                    registeredFields: {
                        testName: {
                            ...defaultState,
                        },
                    },
                },
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
                    ...defaultState,
                    dependency: 'dependency',
                },
            },
        })
    })

    it('Проверка SET_FIELD_FILTER', () => {
        expect(
            formPlugin(
                {
                    registeredFields: {
                        testName: {
                            ...defaultState,
                        },
                    },
                },
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
                    ...defaultState,
                    filter: [
                        {
                            'filter.name': 'Oleg',
                        },
                    ],
                },
            },
        })
    })

    it('Проверка SET_REQUIRED', () => {
        expect(
            formPlugin(
                {
                    registeredFields: {
                        testName: {
                            ...defaultState,
                        },
                    },
                },
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
                    ...defaultState,
                    required: true,
                },
            },
        })
    })

    it('Проверка UNSET_REQUIRED', () => {
        expect(
            formPlugin(
                {
                    registeredFields: {
                        testName: {
                            ...defaultState,
                            required: false,
                        },
                    },
                },
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
                    ...defaultState,
                },
            },
        })
    })
    it('Проверка если название через точку на примере SHOW_FIELD', () => {
        expect(
            formPlugin(
                {
                    registeredFields: {
                        'testName.id': {
                            ...defaultState,
                            visible: false,
                        },
                    },
                },
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
                    ...defaultState,
                },
            },
        })
    })
    it('Проверка SET_LOADING', () => {
        expect(
            formPlugin(
                {
                    registeredFields: {
                        testName: {
                            ...defaultState,
                        },
                    },
                },
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
                    ...defaultState,
                    loading: true,
                },
            },
        })
    })
})
