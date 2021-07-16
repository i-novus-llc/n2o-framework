import React from 'react'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'

import createFactoryConfig from '../../../core/factory/createFactoryConfig'
import FactoryProvider from '../../../core/factory/FactoryProvider'

import Dropdown from './Dropdown'

const mockStore = configureMockStore()

const setup = (props) => {
    const store = mockStore({ toolbar: {} })
    const wrapper = mount(
        <Provider store={store}>
            <FactoryProvider config={createFactoryConfig({})}>
                <Dropdown {...props} />
            </FactoryProvider>
        </Provider>,
    )

    return { store, wrapper }
}

describe('<Dropdown />', () => {
    it('Создание', () => {
        const { wrapper } = setup()
        expect(wrapper.find('.n2o-dropdown').exists()).toBeTruthy()
    })
})
