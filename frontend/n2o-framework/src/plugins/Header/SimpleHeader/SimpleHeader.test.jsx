import React from 'react'
import { Provider } from 'react-redux'
import { mount } from 'enzyme'
import { BrowserRouter as Router } from 'react-router-dom'

import { makeStore } from './__test__/utils'
import SimpleHeader from './SimpleHeader'
import headerJSON from './simpleHeaderData.json'

const { store } = makeStore()

const setup = props => mount(
    <Provider store={store}>
        <Router>
            <SimpleHeader {...props} />
        </Router>
    </Provider>,
)

describe('SimpleHeader', () => {
    it('Переданные элементы хедера отрисовались', () => {
        const header = setup(headerJSON)

        header.update()

        expect(header.find('Navbar').exists()).toBeTruthy()
        expect(header.find('Nav').exists()).toBeTruthy()
        expect(header.find('SidebarSwitcher').exists()).toBeTruthy()
        expect(header.find('Logo').exists()).toBeTruthy()
        expect(header.find('span.badge').exists()).toBeTruthy()
        expect(header.find('SearchBar').exists()).toBeTruthy()
    })

    it('Открытие меню', () => {
        const header = setup(headerJSON)

        header.update()

        header
            .find('.dropdown-toggle')
            .first()
            .simulate('click')
        expect(
            header
                .find('.dropdown-menu')
                .first()
                .hasClass('show'),
        ).toBeTruthy()
    })

    it('проверяет, что активный элемент устанавливается правильно', () => {
        const header = setup(headerJSON)

        header.update()

        header
            .find('button.dropdown-item')
            .first()
            .simulate('click')
        expect(
            header
                .find('a.nav-link.dropdown-item')
                .last()
                .hasClass('active'),
        ).toBeTruthy()
    })
})
