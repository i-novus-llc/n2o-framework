import React from 'react'
import { Provider } from 'react-redux'
import sinon from 'sinon'

import configureStore from '../../../store'
import history from '../../../history'

import { ChangeSize } from './ChangeSize'

const store = configureStore(
    {
        widgets: {
            someWidget: {
                size: 1,
            },
        },
    },
    history,
    {},
)

const setup = (propsOverride) => {
    const props = {}

    return mount(
        <Provider store={store}>
            <ChangeSize entityKey="someWidget" {...props} {...propsOverride} />
        </Provider>,
    )
}

describe('<ChangeSize/>', () => {
    it('компонент должен отрисоваться', () => {
        const wrapper = setup()

        expect(wrapper.find('DropdownItem').exists()).toBeTruthy()
        expect(wrapper.find('DropdownItem').length).toBe(4)
    })

    it('должен вызвать resize', () => {
        const dispatch = sinon.spy()
        const wrapper = setup({
            dispatch,
        })

        wrapper
            .find('DropdownItem')
            .last()
            .simulate('click')

        expect(store.getState().widgets.someWidget.size).toBe(50)
    })
})
