import React from 'react'
import { Provider } from 'react-redux'

import history from '../../../history'
import configureStore from '../../../store'

import ToggleColumn from './ToggleColumn'

const store = configureStore(
    {
        columns: {
            someWidget: {
                surname: {
                    visible: false,
                },
                name: {
                    visible: false,
                },
            },
        },
    },
    history,
    {},
)

const setup = (propsOverride = {}) => {
    const entityKey = 'someWidget'

    return mount(
        <Provider store={store}>
            <ToggleColumn entityKey={entityKey} {...propsOverride} />
        </Provider>,
    )
}

describe('<ToggleColumn />', () => {
    it('компонент должен отрисоваться', () => {
        const wrapper = setup()

        expect(wrapper.find('DropdownItem').exists()).toBeTruthy()
        expect(wrapper.find('DropdownItem').length).toBe(2)
    })

    it('должен вызвать toggleVisibility', () => {
        const wrapper = setup()

        expect(store.getState().columns.someWidget.name.visible).toBeFalsy()
        wrapper
            .find('DropdownItem')
            .last()
            .simulate('click')

        expect(store.getState().columns.someWidget.surname.visible).toBeFalsy()
        expect(store.getState().columns.someWidget.name.visible).toBeTruthy()
    })
})
