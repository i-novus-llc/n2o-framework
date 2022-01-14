import React from 'react'
import { mount } from 'enzyme'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'

import WidgetFilters from './WidgetFilters'

const mockStore = configureMockStore()

const delay = timeout => new Promise(res => setTimeout(res, timeout))

const defaultStateObj = {
    models: {
        filter: {},
    },
}

const setup = (storeObj, propOverrides = {}) => {
    const props = { ...propOverrides }

    const store = mockStore(storeObj || defaultStateObj)

    const wrapper = mount(
        <Provider store={store}>
            <WidgetFilters {...props} />
        </Provider>,
    )

    return {
        props,
        wrapper,
        store,
    }
}

describe('<WidgetFilters />', () => {
    it('проверяет создание элемента', () => {
        const { wrapper } = setup()
        expect(wrapper.find(WidgetFilters).exists()).toBeTruthy()
    })
})
