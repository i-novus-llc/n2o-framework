import models, {
    setModel,
    removeModel,
    updateModel,
    removeAllModel
} from '../store'

describe('Тесты models reducer', () => {
    it('Проверка SET', () => {
        expect(
            models(
                {'prefix': {}},
                {
                    type: setModel.type,
                    payload: {
                        prefix: 'prefix',
                        key: 'key',
                        model: {
                            name: 'modelName',
                        },
                    },
                },
            ),
        ).toEqual({
            prefix: {
                key: {
                    name: 'modelName',
                },
            },
        })
    })

    it('Проверка REMOVE', () => {
        expect(
            models(
                {
                    datasource: [
                        {
                            name: 'test',
                        },
                    ],
                },
                {
                    type: removeModel.type,
                    payload: {
                        prefix: 'datasource',
                        key: 0,
                    },
                },
            ),
        ).toEqual({
            datasource: {},
        })
    })

    it('Проверка UPDATE', () => {
        expect(
            models(
                {
                    edit: {
                        editKey: [{ a: 1 }, { b: 2 }],
                    },
                },
                {
                    type: updateModel.type,
                    payload: {
                        prefix: 'edit',
                        key: 'editKey',
                        field: '[1].field1',
                        value: 'value1',
                    },
                },
            ),
        ).toEqual({
            edit: {
                editKey: [{ a: 1 }, { b: 2, field1: 'value1' }],
            },
        })
        expect(
            models(
                {
                    edit: {
                        editKey: {},
                    },
                },
                {
                    type: updateModel.type,
                    payload: {
                        prefix: 'edit',
                        key: 'editKey',
                        field: 'field1',
                        value: 'value1',
                    },
                },
            ),
        ).toEqual({
            edit: {
                editKey: {
                    field1: 'value1',
                },
            },
        })
        expect(
            models(
                {
                    edit: {},
                },
                {
                    type: updateModel.type,
                    payload: {
                        prefix: 'edit',
                        key: 'editKey',
                        field: 'field1',
                        value: 'value1',
                    },
                },
            ),
        ).toEqual({
            edit: {
                editKey: {
                    field1: 'value1',
                },
            },
        })
        expect(
            models(
                {
                    edit: {
                        editKey0: {
                            field0: 'value0',
                        },
                    },
                },
                {
                    type: updateModel.type,
                    payload: {
                        prefix: 'edit',
                        key: 'editKey1',
                        field: 'field1',
                        value: 'value1',
                    },
                },
            ),
        ).toEqual({
            edit: {
                editKey0: {
                    field0: 'value0',
                },
                editKey1: {
                    field1: 'value1',
                },
            },
        })
        expect(
            models(
                {
                    edit: {
                        editKey: { a: { b: 2 } },
                    },
                },
                {
                    type: updateModel.type,
                    payload: {
                        prefix: 'edit',
                        key: 'editKey',
                        field: 'a.b',
                        value: 'value1',
                    },
                },
            ),
        ).toEqual({
            edit: {
                editKey: { a: { b: 'value1' } },
            },
        })
        expect(
            models(
                {
                    edit: {
                        editKey: {},
                    },
                },
                {
                    type: updateModel.type,
                    payload: {
                        prefix: 'edit',
                        key: 'editKey',
                        field: 'a.b',
                        value: 'value1',
                    },
                },
            ),
        ).toEqual({
            edit: {
                editKey: { a: { b: 'value1' } },
            },
        })
    })

    it('Проверка REMOVE_ALL', () => {
        expect(
            models(
                {
                    datasource: {},
                    multi: {},
                    filter: {},
                    resolve: {
                        testKey: {
                            name: 'new name',
                        },
                    },
                    edit: {
                        testKey: {
                            name: 'new name',
                        },
                    },
                },
                {
                    type: removeAllModel.type,
                    payload: {
                        key: 'testKey',
                    },
                },
            ),
        ).toEqual({
            datasource: {},
            multi: {},
            filter: {},
            resolve: {},
            edit: {},
        })
    })
})
