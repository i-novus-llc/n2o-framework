import React from 'react'
import { BrowserRouter as Router } from 'react-router-dom'

import { SidebarItemContainer } from './SidebarItemContainer'
import {ITEM_SRC} from '../constants'

const setup = props => mount(
    <Router>
        <SidebarItemContainer {...props} />
    </Router>,
)

describe('Тесты SidebarItemContainer', () => {
    it('Отрисовка link', () => {
        const wrapper = setup({
            itemProps: {
                src: ITEM_SRC.LINK,
                title: 'test',
                href: 'testHref',
            },
        })
        expect(wrapper.find('Link').exists()).toEqual(true)
    })
    it('Отрисовка Dropdown', () => {
        const wrapper = setup({
            itemProps: {
                src: ITEM_SRC.DROPDOWN,
                title: 'test',
            },
        })
        expect(wrapper.find('SidebarDropdown').exists()).toEqual(true)
    })
})
