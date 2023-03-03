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

const form = 'test_form'
const formStore = (state) => ({
    [form]: { ...state }
})

describe('Тесты formPlugin reducer', () => {
    it('Проверка DISABLE_FIELD', () => {
        expect(
            formPlugin(
                formStore({
                    registeredFields: {
                        testName: {
                            ...FormPlugin.defaultState,
                        },
                    },
                })
                ,
                {
                    type: disableField.type,
                    payload: {
                        key: form,
                        name: 'testName',
                    },
                },
            ),
        ).toEqual(
            formStore({
                registeredFields: {
                    testName: {
                        ...FormPlugin.defaultState,
                        disabled: true,
                        disabled_field: true
                    },
                },
            })
        )
    })

    it('Проверка ENABLE_FIELD', () => {
        expect(
            formPlugin(
                formStore({
                    registeredFields: {
                        testName: {
                            ...FormPlugin.defaultState,
                            disabled: true,
                            disabled_field: true
                        },
                    },
                }),
                {
                    type: enableField.type,
                    payload: {
                        key: form,
                        name: 'testName',
                    },
                },
            ),
        ).toEqual(
            formStore({
                registeredFields: {
                    testName: {
                        ...FormPlugin.defaultState,
                    },
                },
            })
        )
    })

    it('Проверка SHOW_FIELD', () => {
        expect(
            formPlugin(
                formStore({
                    registeredFields: {
                        testName: {
                            ...FormPlugin.defaultState,
                            visible: false,
                            visible_field: false,
                        },
                    },
                }),
                {
                    type: showField.type,
                    payload: {
                        key: form,
                        name: 'testName',
                    },
                },
            ),
        ).toEqual(
            formStore({
                registeredFields: {
                    testName: {
                        ...FormPlugin.defaultState,
                    },
                },
            })
        )
    })

    it('Проверка HIDE_FIELD', () => {
        expect(
            formPlugin(
                formStore({
                    registeredFields: {
                        testName: {
                            ...FormPlugin.defaultState,
                        },
                    },
                }),
                {
                    type: hideField.type,
                    payload: {
                        key: form,
                        name: 'testName',
                    },
                },
            ),
        ).toEqual(formStore({
            registeredFields: {
                testName: {
                    ...FormPlugin.defaultState,
                    visible: false,
                    visible_field: false,
                },
            },
        }))
    })

    it('Проверка REGISTER_FIELD_EXTRA', () => {
        expect(
            formPlugin(
                formStore({
                    registeredFields: {
                        testName: {
                            ...FormPlugin.defaultState,
                        },
                    },
                }),
                {
                    type: registerFieldExtra.type,
                    payload: {
                        key: form,
                        name: 'testName',
                        initialState: {
                            visible: false,
                            disabled: true,
                        },
                    },
                },
            ),
        ).toEqual(
            formStore({
                registeredFields: {
                    testName: {
                        ...FormPlugin.defaultState,
                        visible: false,
                        disabled: true,
                    },
                },
            })
        )
    })

    it('Проверка REGISTER_DEPENDENCY', () => {
        expect(
            formPlugin(
                formStore({
                    registeredFields: {
                        testName: {
                            ...FormPlugin.defaultState,
                        },
                    },
                }),
                {
                    type: registerFieldDependency.type,
                    payload: {
                        key: form,
                        name: 'testName',
                        dependency: 'dependency',
                    },
                },
            ),
        ).toEqual(
            formStore({
                registeredFields: {
                    testName: {
                        ...FormPlugin.defaultState,
                        dependency: 'dependency',
                    },
                },
            })
        )
    })

    it('Проверка SET_REQUIRED', () => {
        expect(
            formPlugin(
                formStore({
                    registeredFields: {
                        testName: {
                            ...FormPlugin.defaultState,
                        },
                    },
                }),
                {
                    type: setRequired.type,
                    payload: {
                        key: form,
                        field: 'testName',
                    },
                },
            ),
        ).toEqual(
            formStore({
                registeredFields: {
                    testName: {
                        ...FormPlugin.defaultState,
                        required: true,
                    },
                },
            })
        )
    })

    it('Проверка UNSET_REQUIRED', () => {
        expect(
            formPlugin(
                formStore({
                    registeredFields: {
                        testName: {
                            ...FormPlugin.defaultState,
                            required: false,
                        },
                    },
                }),
                {
                    type: unsetRequired.type,
                    payload: {
                        key: form,
                        field: 'testName',
                    },
                },
            ),
        ).toEqual(
            formStore({
                registeredFields: {
                    testName: {
                        ...FormPlugin.defaultState,
                    },
                },
            })
        )
    })
    it('Проверка если название через точку на примере SHOW_FIELD', () => {
        expect(
            formPlugin(
                formStore({
                    registeredFields: {
                        'testName.id': {
                            ...FormPlugin.defaultState,
                            visible: false,
                        },
                    },
                }),
                {
                    type: showField.type,
                    payload: {
                        key: form,
                        name: 'testName.id',
                    },
                },
            ),
        ).toEqual(
            formStore({
                registeredFields: {
                    'testName.id': {
                        ...FormPlugin.defaultState,
                    },
                },
            })
        )
    })
    it('Проверка SET_LOADING', () => {
        expect(
            formPlugin(
                formStore({
                    registeredFields: {
                        testName: {
                            ...FormPlugin.defaultState,
                        },
                    },
                }),
                {
                    type: setLoading.type,
                    payload: {
                        key: form,
                        name: 'testName',
                        loading: true,
                    },
                },
            ),
        ).toEqual(
            formStore({
                registeredFields: {
                    testName: {
                        ...FormPlugin.defaultState,
                        loading: true,
                    },
                },
            })
        )
    })
})
