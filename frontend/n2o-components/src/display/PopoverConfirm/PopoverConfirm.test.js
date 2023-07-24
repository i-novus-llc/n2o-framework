import React from 'react'
import sinon from 'sinon'

import { PopoverConfirm } from './PopoverConfirm'

const props = {
    component: {
        header: 'header',
    },
}

const setupComponent = propsOverride => shallow(<PopoverConfirm {...props.component} {...propsOverride} />)

describe('Тесты PopoverConfirm', () => {
    it('Отрисовывается, если передан header', () => {
        const wrapper = setupComponent()
        expect(wrapper.find('.n2o-popover').exists()).toEqual(true)
    })
    it('Confirm click popover', () => {
        const onClick = sinon.spy()
        const wrapper = setupComponent({ isOpen: true, onConfirm: onClick })
        wrapper
            .find('ButtonGroup')
            .childAt(1)
            .simulate('click')
        expect(onClick.called).toEqual(true)
    })
    it('Проверка onConfirm', () => {
        const onClick = sinon.spy()
        const wrapper = setupComponent({
            onConfirm: onClick,
            isOpen: true,
        })
        wrapper
            .find('.btn-sm')
            .last()
            .simulate('click')
        expect(onClick.called).toEqual(true)
    })
})
