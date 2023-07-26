import React from 'react'
import { BrowserRouter as Router } from 'react-router-dom'

import {ITEM_SRC} from '../constants'
import { Provider } from 'react-redux'
import FactoryProvider from '../../core/factory/FactoryProvider'
import createFactoryConfig from '../../core/factory/createFactoryConfig'
import  { NavItemContainer } from './NavItemContainer'
import configureMockStore from 'redux-mock-store'

const mockStore = configureMockStore()
const store = mockStore({})

const setup = props => mount(
    <Provider store={store}>
        <FactoryProvider config={createFactoryConfig({})}>
            <Router>
                <NavItemContainer {...props} />
            </Router>
        </FactoryProvider>
    </Provider>,
)

describe('Тесты SidebarItemContainer', () => {
    it('Отрисовка link', () => {
        const wrapper = setup({
            itemProps: {
                src: ITEM_SRC.LINK,
                title: 'test sidebar item',
                href: 'testHref',
            },
        })
        expect(wrapper.find('Link').exists()).toEqual(true)
    })
    it('Отрисовка Dropdown', () => {
        const wrapper = setup({
            itemProps: {
                src: ITEM_SRC.DROPDOWN,
                title: 'test sidebar dropdown item',
                items: [{ title: 'test1', href: '/', linkType: 'inner', src: ITEM_SRC.LINK }],
            },
        })
        expect(wrapper.find('DropdownWrapper').exists()).toEqual(true)
    })
})
