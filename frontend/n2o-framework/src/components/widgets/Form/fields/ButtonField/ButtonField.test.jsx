import React from 'react'
import { Provider } from 'react-redux'
import mockStore from 'redux-mock-store'

import ButtonField from './ButtonField'

const setup = () => {
    const props = {
        label: 'Поле "ButtonField"',
    }

    return mount(
        <Provider store={mockStore()({})}>
            <ButtonField {...props} />
        </Provider>,
    )
}

describe('<ButtonField />', () => {
    it('компонент отрисовывается', () => {
        const wrapper = setup()

        expect(wrapper.find('.n2o-button-field').exists()).toBeTruthy()
    })
})
