import React from 'react'
import sinon from 'sinon'

import { AdvancedTableExpandIcon } from './AdvancedTableExpandIcon'

const setup = (propsOverride) => {
    const props = {
        record: {
            children: [],
        },
    }

    return mount(<AdvancedTableExpandIcon {...props} {...propsOverride} />)
}

describe('<AdvancedTableExpandIcon />', () => {
    it('компонент отрисовывается', () => {
        const wrapper = setup()

        expect(wrapper.find('.n2o-advanced-table-expand').exists()).toBeTruthy()
    })

    it('компонент не отрисовывается без саб контента', () => {
        const wrapper = setup({
            record: {},
        })

        expect(wrapper.find('span').props().className).toBe('')
        expect(wrapper.find('span').text()).toBe('')
    })

    it('компонент открыт', () => {
        const wrapper = setup({
            expanded: true,
        })

        expect(wrapper.find('.fa-angle-down').exists()).toBeTruthy()
    })

    it('компонент закрыт', () => {
        const wrapper = setup({
            expanded: false,
        })

        expect(wrapper.find('.fa-angle-right').exists()).toBeTruthy()
    })

    it('отрабатывает функция открытия', () => {
        const onExpand = sinon.spy()
        const wrapper = setup({
            onExpand,
        })

        wrapper.find('.n2o-advanced-table-expand').simulate('click')
        expect(onExpand.calledOnce).toBeTruthy()
    })
})
