import React from 'react'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'
import { mount } from 'enzyme'

import InputSelectContainer from './InputSelectContainer'

const mockStore = configureMockStore()
const store = mockStore({ alerts: {} })

const props = {
    loading: false,
    value: '123',
    disabled: false,
    placeholder: 'vvv',
    valueFieldId: 'id',
    labelFieldId: 'id',
    filter: 'includes',
    resetOnBlur: false,
    disabledValues: [],
    queryId: 'options',
    size: 50,
    options: [],
}

const setup = (propOverrides, defaultProps = props) => {
    const props = { ...defaultProps, ...propOverrides }

    const wrapper = mount(
        <Provider store={store}>
            <InputSelectContainer {...props} />
        </Provider>,
    )

    return {
        props,
        wrapper,
    }
}

describe('<InputSelectContainer />', () => {
    it('проверяет создание элемента', () => {
        const { wrapper } = setup()

        expect(wrapper.find('div.n2o-input-select').exists()).toBeTruthy()
    })
})
