import formPlugin, {
    hideField,
    disableField,
    enableField,
    showField,
    registerFieldExtra,
    addFieldMessage,
    removeFieldMessage,
    registerFieldDependency,
    setFilterValue,
    setRequired,
    unsetRequired,
    setLoading
} from '../store'

describe('Тесты formPlugin reducer', () => {
    it('Проверка DISABLE_FIELD', () => {
        expect(
            formPlugin(
                {
                    registeredFields: {
                        testName: {
                            isInit: true,
                            visible: true,
                            disabled: false,
                            message: null,
                            filter: [],
                            dependency: null,
                            required: false,
                            loading: false,
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
                {
                    registeredFields: {
                        testName: {
                            isInit: true,
                            visible: true,
                            disabled: false,
                            message: null,
                            filter: [],
                            dependency: null,
                            required: false,
                            loading: false,
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
                {
                    registeredFields: {
                        testName: {
                            isInit: true,
                            visible: true,
                            disabled: false,
                            message: null,
                            filter: [],
                            dependency: null,
                            required: false,
                            loading: false,
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
                {
                    registeredFields: {
                        testName: {
                            isInit: true,
                            visible: true,
                            disabled: false,
                            message: null,
                            filter: [],
                            dependency: null,
                            required: false,
                            loading: false,
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
                {
                    registeredFields: {
                        testName: {
                            isInit: true,
                            visible: true,
                            disabled: false,
                            message: null,
                            filter: [],
                            dependency: null,
                            required: false,
                            loading: false,
                        },
                    },
                },
                {
                    type: addFieldMessage.type,
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
                {
                    registeredFields: {
                        testName: {
                            isInit: true,
                            visible: true,
                            disabled: false,
                            message: null,
                            filter: [],
                            dependency: null,
                            required: false,
                            loading: false,
                        },
                    },
                },
                {
                    type: removeFieldMessage.type,
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
                {
                    registeredFields: {
                        testName: {
                            isInit: true,
                            visible: true,
                            disabled: false,
                            message: null,
                            filter: [],
                            dependency: null,
                            required: false,
                            loading: false,
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
                {
                    registeredFields: {
                        testName: {
                            isInit: true,
                            visible: true,
                            disabled: false,
                            message: null,
                            filter: [],
                            dependency: null,
                            required: false,
                            loading: false,
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
                {
                    registeredFields: {
                        testName: {
                            isInit: true,
                            visible: true,
                            disabled: false,
                            message: null,
                            filter: [],
                            dependency: null,
                            required: false,
                            loading: false,
                        },
                    },
                },
                {
                    type: setFilterValue.type,
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
                {
                    registeredFields: {
                        testName: {
                            isInit: true,
                            visible: true,
                            disabled: false,
                            message: null,
                            filter: [],
                            dependency: null,
                            required: false,
                            loading: false,
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
                {
                    registeredFields: {
                        testName: {
                            isInit: true,
                            visible: true,
                            disabled: false,
                            message: null,
                            filter: [],
                            dependency: null,
                            required: false,
                            loading: false,
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
                {
                    registeredFields: {
                        'testName.id': {
                            isInit: true,
                            visible: true,
                            disabled: false,
                            message: null,
                            filter: [],
                            dependency: null,
                            required: false,
                            loading: false,
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
                {
                    registeredFields: {
                        testName: {
                            isInit: true,
                            visible: true,
                            disabled: false,
                            message: null,
                            filter: [],
                            dependency: null,
                            required: false,
                            loading: false,
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
