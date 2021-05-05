import React from 'react'
import { mount } from 'enzyme'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'

import InputSelectTreeContainer from './InputSelectTreeContainer'

const mockStore = configureMockStore()
const store = mockStore({})

const setup = (propOverrides = {}) => {
    const props = {
        loading: false,
        value: '',
        disabled: false,
        placeholder: 'test',
        valueFieldId: 'id',
        labelFieldId: 'id',
        filter: 'includes',
        resetOnBlur: false,
        disabledValues: [],
        hasChildrenFieldId: true,
        data: [
            {
                id: '123412',
                parentId: '',
                icon: 'fa fa-square',
                image: 'https://i.stack.imgur.com/2zqqC.jpg',
                hasChildren: true,
            },
            {
                id: '33',
                parentId: '123412',
                icon: 'fa fa-square',
                image: 'https://i.stack.imgur.com/2zqqC.jpg',
                hasChildren: false,
            },
        ],
        ...propOverrides,
    }

    const wrapper = mount(
        <Provider store={store}>
            <InputSelectTreeContainer {...props} />
        </Provider>,
    )

    return {
        props,
        wrapper,
    }
}

describe('<InputSelectTree />', () => {
    it('проверяет создание элемента', () => {
        const { wrapper } = setup()

        expect(wrapper.find('InputSelectTreeContainer').exists()).toBeTruthy()
    })
})
