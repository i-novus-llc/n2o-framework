import { getFieldsKeys } from './utils'
import fieldsets2 from './tests/fieldsetsForTest'

const fieldsets1 = [
    {
        src: 'StandardFieldset',
        rows: [
            {
                cols: [
                    {
                        fields: [
                            {
                                src: 'StandardField',
                                id: 'surname',
                                label: 'Фамилия',
                                dependency: [],
                                control: {
                                    src: 'InputText',
                                    id: 'surname',
                                },
                            },
                        ],
                    },
                    {
                        fields: [
                            {
                                src: 'StandardField',
                                id: 'name',
                                label: 'Имя',
                                dependency: [],
                                control: {
                                    src: 'InputText',
                                    id: 'name',
                                },
                            },
                        ],
                    },
                ],
            },
            {
                cols: [
                    {
                        size: 8,
                        fields: [
                            {
                                src: 'StandardField',
                                id: 'gender',
                                enabled: false,
                                label: 'Пол',
                                dependency: [],
                                control: {
                                    src: 'RadioGroup',
                                    id: 'gender',
                                    valueFieldId: 'id',
                                    labelFieldId: 'name',
                                    size: 30,
                                    dataProvider: {
                                        url: 'n2o/data/ProtoGender',
                                        queryMapping: {},
                                    },
                                    type: 'n2o',
                                },
                            },
                        ],
                    },
                    {
                        fields: [
                            {
                                src: 'StandardField',
                                id: 'vip',
                                enabled: false,
                                dependency: [],
                                control: {
                                    src: 'Checkbox',
                                    id: 'vip',
                                    label: 'VIP',
                                },
                            },
                        ],
                    },
                ],
            },
        ],
    },
]

const testState = {
    models: {
        resolve: {
            formName: {
                field1: {
                    item1: 'test',
                    item2: 'test2',
                },
                field2: {
                    item1: 'testtest',
                    item2: {
                        id: 1,
                    },
                },
            },
        },
    },
}

describe('Проверка until js', () => {
    describe('getFieldsKeys', () => {
        it('правильно мапит ключи', () => {
            expect(getFieldsKeys(fieldsets1)).toEqual([
                'surname',
                'name',
                'gender',
                'vip',
            ])
            expect(getFieldsKeys(fieldsets2)).toEqual([
                'dduDate',
                'dduDeveloper',
                'dduDeveloperInn',
                'dduConcession',
                'dduConcessionHelp',
            ])
        })
    })
})
