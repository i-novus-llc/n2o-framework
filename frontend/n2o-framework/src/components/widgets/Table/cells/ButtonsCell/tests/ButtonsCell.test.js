import React from 'react'
import { mount } from 'enzyme'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'
import sinon from 'sinon'

import ButtonsCell from '../ButtonsCell'
import FactoryProvider from '../../../../../../core/factory/FactoryProvider'
import createFactoryConfig from '../../../../../../core/factory/createFactoryConfig'

const mockStore = configureMockStore()
const store = mockStore({ models: { datasource: {}, resolve: {}, multi: {}, filter: {} } })

const setup = (propOverrides = {}) => {
    const props = { ...propOverrides }

    const wrapper = mount(
        <Provider store={store}>
            <FactoryProvider config={createFactoryConfig()}>
                <ButtonsCell {...props} />
            </FactoryProvider>
        </Provider>,
    )

    return {
        props,
        wrapper,
    }
}

describe('<ButtonsCell />', () => {
    it('Проверяет создание Кнопки', () => {
        const { wrapper } = setup({
            toolbar: [
                {
                    buttons: [{ src: 'LinkButton', title: 'test' }],
                },
            ],
        })

        wrapper.update()
        expect(wrapper.find('Toolbar').exists()).toBeTruthy()
    })
})
