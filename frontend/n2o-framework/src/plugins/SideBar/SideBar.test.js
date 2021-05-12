import React from 'react'

import { SideBar } from './SideBar'

const props = {
    brand: 'N2O Framework',
    brandImage: 'https://test.png',
    className: 'test',
    controlled: false,
    visible: true,
    userBox: true,
}

const setup = props => mount(<SideBar {...props} />)

describe('Тесты SideBar', () => {
    it('Отрисовка в соответствии с props', () => {
        const wrapper = setup(props)
        expect(wrapper.find('.n2o-nav-brand__text').exists()).toEqual(true)
        expect(wrapper.find('img').exists()).toEqual(true)
        expect(wrapper.find('.n2o-sidebar__toggler').exists()).toEqual(true)
        expect(wrapper.find('UserBox').exists()).toEqual(true)
    })
})
