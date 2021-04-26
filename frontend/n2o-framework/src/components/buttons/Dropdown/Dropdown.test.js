import React from 'react'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'

import createFactoryConfig from '../../../core/factory/createFactoryConfig'
import FactoryProvider from '../../../core/factory/FactoryProvider'

import Dropdown from './Dropdown'

const mockStore = configureMockStore()

const setup = (props) => {
    const store = mockStore({})
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

    it('Создание элементов списка', () => {
        const { wrapper } = setup({
            id: 'id',
            tag: 'tag',
            label: 'label',
            icon: 'icon',
            size: 'size',
            color: 'color',
            outline: 'outline',
            visible: 'visible',
            disabled: false,
            count: 'count',
            subMenu: [
                {
                    src: 'StandardButton',
                    id: 'id',
                    tag: 'tag',
                    label: 'label',
                    icon: 'icon',
                    size: 'size',
                    color: 'color',
                    outline: 'outline',
                    visible: 'visible',
                    disabled: false,
                    count: 'count',
                },
            ],
        })

        // проверка пропсов SimpleButton
        const props = wrapper
            .find('SimpleButton')
            .at(0)
            .props()
        expect(props.caret).toBeTruthy()
        expect(props.className).toBe('n2o-dropdown-control dropdown-toggle')
        expect(props.color).toBe('color')
        expect(props.count).toBe(undefined)
        expect(props.disabled).toBeFalsy()
        expect(props.icon).toBe('icon')
        expect(props.label).toBe('label')
        expect(props.outline).toBe('outline')
        expect(props.size).toBe('size')
        expect(props.visible).toBeTruthy()
    })
})
