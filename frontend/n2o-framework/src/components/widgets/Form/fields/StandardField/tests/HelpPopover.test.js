import React from 'react'

import HelpPopover from '../HelpPopover'

const setupPopover = (propsOverride) => {
    const props = {
        id: 'test',
        help: 'подсказка',
    }
    return shallow(<HelpPopover {...props} {...propsOverride} />)
}

describe('Тесты HelpPopover', () => {
    it('Отрисовывается, если переданы id и help', () => {
        const wrapper = setupPopover()
        expect(wrapper.find('.n2o-popover').exists()).toEqual(true)
    })
    it('Показывает подсказку', () => {
        const wrapper = setupPopover()
        wrapper.find('.n2o-popover-btn').simulate('focus')
        expect(wrapper.find('.n2o-popover-body').exists()).toEqual(true)
    })
})
