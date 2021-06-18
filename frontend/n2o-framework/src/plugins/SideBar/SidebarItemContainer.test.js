import React from 'react'
import { BrowserRouter as Router } from 'react-router-dom'

import SidebarItemContainer from './SidebarItemContainer'

const setup = props => mount(
    <Router>
        <SidebarItemContainer {...props} />
    </Router>,
)

describe('Тесты SidebarItemContainer', () => {
    it('Отрисовка link', () => {
        const wrapper = setup({
            item: {
                type: 'link',
                label: 'test',
                href: 'testHref',
            },
        })
        expect(wrapper.find('Link').exists()).toEqual(true)
    })
    it('Отрисовка Dropdown', () => {
        const wrapper = setup({
            item: {
                type: 'dropdown',
                label: 'test',
            },
        })
        expect(wrapper.find('SidebarDropdown').exists()).toEqual(true)
    })
})
