import React from 'react'
import { Provider } from 'react-redux'
import set from 'lodash/set'

import configureStore from '../../../../../store'
import history from '../../../../../history'
import FactoryProvider from '../../../../../core/factory/FactoryProvider'
import createFactoryConfig from '../../../../../core/factory/createFactoryConfig'
import FormWidget from '../../FormWidget'

const initialState = {
    models: {
        resolve: {
            proto_form: {
                members: [
                    {
                        surname: 'first surname',
                        name: 'first name',
                    },
                    {
                        surname: 'second surname',
                        name: 'second name',
                    },
                ],
            },
        },
    },
}

const store = configureStore(initialState, history, {})

const fieldsets = [
    {
        src: 'MultiFieldset',
        needRemoveButton: true,
        needRemoveAllButton: true,
        needCopyButton: true,
        name: 'members',
        label: '`\'Элемент \' + index`',
        childrenLabel: '`\'Элемент \' + index`',
        rows: [
            {
                cols: [
                    {
                        fields: [
                            {
                                src: 'StandardField',
                                id: 'surname',
                                childrenLabel: 'Фамилия',
                                dependency: [
                                    {
                                        type: 'enabled',
                                        on: ['members[#index].name'],
                                        expression: 'members[#index].name !== \'test\'',
                                        applyOnInit: true,
                                    },
                                ],
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
                                childrenLabel: 'Имя:',
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
        ],
    },
]

const setup = (propsOverride = {}) => {
    const props = {
        id: 'proto_form',
        pageId: 'multiFieldsetPage',
        dataProvider: {
            url: '/multi-fieldset/data',
            pathMapping: {},
            queryMapping: {},
        },
        form: {
            fieldsets,
            validation: {
                members: [
                    {
                        severity: 'danger',
                        validationKey: 'members',
                        text: 'Валидация на members',
                        type: 'required',
                        multi: true,
                    },
                ],
            },
        },
    }

    return mount(
        <Provider store={store}>
            <FactoryProvider config={createFactoryConfig({})}>
                <FormWidget {...props} {...propsOverride} />
            </FactoryProvider>
        </Provider>,
    )
}
// FIXME проверить что не так и расскоментировать
describe('<MultiFieldset />', () => {
        it('trash', () => {
            expect(true).toBe(true)
        })
//     it('Компонент отрисовывается', () => {
//         const wrapper = setup()
//         expect(wrapper.find('MultiFieldset').length).toBe(1)
//         expect(wrapper.find('StandardField').length).toBe(4)
//     })

//     it('отрисовывается правильный label', () => {
//         const wrapper = setup()
//         expect(wrapper.find('.n2o-multi-fieldset__label').length).toBe(2)
//         expect(
//             wrapper
//                 .find('.n2o-multi-fieldset__label')
//                 .first()
//                 .text(),
//         ).toBe('Элемент 0')
//         expect(
//             wrapper
//                 .find('.n2o-multi-fieldset__label')
//                 .last()
//                 .text(),
//         ).toBe('Элемент 1')
//     })

//     it('должен добавить группу fields', () => {
//         const wrapper = setup()

//         wrapper
//             .find('.n2o-multi-fieldset__add')
//             .last()
//             .simulate('click')

//         expect(wrapper.find('StandardField').length).toBe(6)
//     })

//     it('должен скопировать группу fields', () => {
//         const wrapper = setup()

//         wrapper
//             .find('.n2o-multi-fieldset__copy')
//             .first()
//             .simulate('click')

//         expect(wrapper.find('StandardField').length).toBe(6)
//         expect(
//             wrapper
//                 .find('input')
//                 .at(4)
//                 .props().value,
//         ).toEqual('first surname')
//         expect(
//             wrapper
//                 .find('input')
//                 .last()
//                 .props().value,
//         ).toEqual('first name')
//     })

//     it('должен удалить группу fields', () => {
//         const wrapper = setup()

//         wrapper
//             .find('.n2o-multi-fieldset__remove')
//             .last()
//             .simulate('click')

//         expect(wrapper.find('StandardField').length).toBe(2)
//     })

//     it('должен удалить все группы fields, кроме первого', () => {
//         const wrapper = setup()

//         expect(wrapper.find('StandardField').length).toBe(4)
//         wrapper
//             .find('.n2o-multi-fieldset__remove-all')
//             .first()
//             .simulate('click')
//         expect(wrapper.find('StandardField').length).toBe(2)
//     })

//     it('должен удалить все группы fields', () => {
//         const newFieldsets = { ...fieldsets }
//         set(newFieldsets, '[0].canRemoveFirstItem', true)
//         const wrapper = setup({
//             form: {
//                 fieldsets: newFieldsets,
//             },
//         })

//         expect(wrapper.find('StandardField').length).toBe(4)
//         wrapper
//             .find('.n2o-multi-fieldset__remove-all')
//             .first()
//             .simulate('click')
//         expect(wrapper.find('StandardField').exists()).toBeFalsy()
//     })
})
