import formPlugin, {
    hideField,
    disableField,
    enableField,
    showField,
    registerFieldExtra,
    registerFieldDependency,
    setRequired,
    unsetRequired,
    setLoading
} from '../store'
import FormPlugin from '../FormPlugin'

describe('Тесты formPlugin reducer', () => {
    it('Проверка DISABLE_FIELD', () => {
        expect(
            formPlugin(
                {
                    registeredFields: {
                        testName: {
                            ...FormPlugin.defaultState,
                        },
                    },
                },
                {
                    type: disableField.type,
                    payload: {
                        name: 'testName',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    ...FormPlugin.defaultState,
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
                            ...FormPlugin.defaultState,
                            disabled: true,
                            disabled_field: true
                        },
                    },
                },
                {
                    type: enableField.type,
                    payload: {
                        name: 'testName',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    ...FormPlugin.defaultState,
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
                            ...FormPlugin.defaultState,
                            visible: false,
                            visible_field: false,
                        },
                    },
                },
                {
                    type: showField.type,
                    payload: {
                        name: 'testName',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    ...FormPlugin.defaultState,
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
                            ...FormPlugin.defaultState,
                        },
                    },
                },
                {
                    type: hideField.type,
                    payload: {
                        name: 'testName',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    ...FormPlugin.defaultState,
                    visible: false,
                    visible_field: false,
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
                            ...FormPlugin.defaultState,
                        },
                    },
                },
                {
                    type: registerFieldExtra.type,
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
                    ...FormPlugin.defaultState,
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
                            ...FormPlugin.defaultState,
                        },
                    },
                },
                {
                    type: registerFieldDependency.type,
                    payload: {
                        name: 'testName',
                        dependency: 'dependency',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    ...FormPlugin.defaultState,
                    dependency: 'dependency',
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
                            ...FormPlugin.defaultState,
                        },
                    },
                },
                {
                    type: setRequired.type,
                    payload: {
                        name: 'testName',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    ...FormPlugin.defaultState,
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
                            ...FormPlugin.defaultState,
                            required: false,
                        },
                    },
                },
                {
                    type: unsetRequired.type,
                    payload: {
                        name: 'testName',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                testName: {
                    ...FormPlugin.defaultState,
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
                            ...FormPlugin.defaultState,
                            visible: false,
                        },
                    },
                },
                {
                    type: showField.type,
                    payload: {
                        name: 'testName.id',
                    },
                },
            ),
        ).toEqual({
            registeredFields: {
                'testName.id': {
                    ...FormPlugin.defaultState,
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
                            ...FormPlugin.defaultState,
                        },
                    },
                },
                {
                    type: setLoading.type,
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
                    ...FormPlugin.defaultState,
                    loading: true,
                },
            },
        })
    })
})
