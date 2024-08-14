import React from 'react'
import { BrowserRouter as Router } from 'react-router-dom'
import { Provider } from 'react-redux'
import configureMockStore from 'redux-mock-store'

import { ITEM_SRC } from '../../constants'
import FactoryProvider from '../../../core/factory/FactoryProvider'
import createFactoryConfig from '../../../core/factory/createFactoryConfig'

import { NavItemContainer } from './NavItemContainer'

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

describe('Тесты NavItemContainer', () => {
    it('Dropdown', () => {
        const wrapper = setup({
            itemProps: {
                id: '2131',
                title: 'test',
                src: ITEM_SRC.DROPDOWN,
                items: [{ title: 'test1', href: '/', linkType: 'inner', src: ITEM_SRC.LINK }],
            },
        })

        expect(wrapper.find('Dropdown').exists()).toEqual(true)
    })
    it('Link', () => {
        const wrapper = setup({
            itemProps: {
                id: '2131',
                label: 'test',
                src: ITEM_SRC.LINK,
                href: 'testHref',
            },
        })

        expect(wrapper.find('Link').exists()).toEqual(true)
    })
    it('target = _blank', () => {
        const wrapper = setup({
            itemProps: {
                id: '2131',
                label: 'test',
                src: ITEM_SRC.LINK,
                href: 'testHref',
                target: '_blank',
                linkType: 'outer',
            },
        })

        expect(wrapper.find('OuterLink').props().target).toEqual('_blank')
    })
})
