import React from 'react'
import { mount } from 'enzyme'
import { BrowserRouter as Router } from 'react-router-dom'
import { shallowToJson } from 'enzyme-to-json'

import SimpleHeader from './SimpleHeader'
import MetaJSON from './simpleHeaderData'

const props = MetaJSON

const setup = (propOverrides, defaultProps = props) => {
    const props = { ...defaultProps, ...propOverrides }

    const wrapper = mount(
        <Router>
            <SimpleHeader {...props} />
        </Router>,
    )

    return {
        props,
        wrapper,
    }
}

describe('<SimpleHeader />', () => {
    it.skip('проверяет создание компонента', () => {
        const { wrapper } = setup()

        wrapper.update()
        expect(shallowToJson(wrapper)).toMatchSnapshot()
    })
    it('проверяет props', () => {
        const { wrapper } = setup()
        const expectedValue = props.items[1]

        wrapper.update()
        expect(wrapper.find('NavItemContainer').get(1).props.item).toBe(
            expectedValue,
        )
    })
    it('проверяет открытие меню', () => {
        const { wrapper } = setup()

        wrapper.update()
        wrapper
            .find('.dropdown-toggle')
            .first()
            .simulate('click')
        expect(
            wrapper
                .find('.dropdown-menu')
                .first()
                .hasClass('show'),
        ).toBeTruthy()
    })
    it('проверяет, что активный элемент устанавливается правильно', () => {
        const { wrapper } = setup()

        wrapper.update()
        wrapper
            .find('button.dropdown-item')
            .first()
            .simulate('click')
        expect(
            wrapper
                .find('a.nav-link.dropdown-item')
                .last()
                .hasClass('active'),
        ).toBeTruthy()
    })
    it('проверяет баджи', () => {
        const { wrapper } = setup()

        wrapper.update()
        expect(wrapper.find('Badge').exists()).toBeTruthy()
    })
})
