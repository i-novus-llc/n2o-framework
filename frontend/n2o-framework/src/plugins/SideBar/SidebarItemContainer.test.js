import React from 'react'
import { BrowserRouter as Router } from 'react-router-dom'

import { SidebarItemContainer } from './SidebarItemContainer'

const setup = props => mount(
    <Router>
        <SidebarItemContainer {...props} />
    </Router>,
)

describe('Тесты SidebarItemContainer', () => {
    it('Отрисовка link', () => {
        const wrapper = setup({
            itemProps: {
                type: 'link',
                title: 'test',
                href: 'testHref',
            },
        })
        expect(wrapper.find('Link').exists()).toEqual(true)
    })
    it('Отрисовка Dropdown', () => {
        const wrapper = setup({
            itemProps: {
                type: 'dropdown',
                title: 'test',
            },
        })
        expect(wrapper.find('SidebarDropdown').exists()).toEqual(true)
    })
})
