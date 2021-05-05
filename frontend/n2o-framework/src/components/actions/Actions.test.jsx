import React from 'react'
import { Provider } from 'react-redux'
import mockStore from 'redux-mock-store'
import { mount, shallow } from 'enzyme'

import { Actions } from './Actions'

const setup = (propsOverride) => {
    const props = {
        toolbar: [
            {
                buttons: [
                    {
                        id: 'testButton1',
                        label: 'Text Button 1',
                    },
                    {
                        id: 'testButton2',
                        label: 'Test Button 2',
                    },
                ],
            },
        ],
    }

    return mount(
        <Provider store={mockStore()({})}>
            <Actions {...props} {...propsOverride} />
        </Provider>,
    )
}

describe('<Actions />', () => {
    it('компонент должен отрисоваться', () => {
        const wrapper = setup()

        expect(wrapper.find('ButtonContainer').exists()).toBe(true)
        expect(wrapper.find('ButtonContainer').length).toBe(2)
    })

    it('должен отрисоваться Dropdown', () => {
        const wrapper = setup({
            toolbar: [
                {
                    buttons: [
                        {
                            id: 'testButton1',
                            label: 'Text Button 1',
                            subMenu: [
                                {
                                    id: 'testButtonSub1',
                                },
                            ],
                        },
                    ],
                },
            ],
        })

        expect(wrapper.find('Dropdown').exists()).toBeTruthy()
    })
})
