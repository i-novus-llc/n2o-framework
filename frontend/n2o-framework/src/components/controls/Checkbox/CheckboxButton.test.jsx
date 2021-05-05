import React from 'react'
import { shallow } from 'enzyme'
import sinon from 'sinon'

import CheckboxButton from './CheckboxButton'

const setup = (propsOverrides) => {
    const props = {
        value: '123',
        checked: false,
        ...propsOverrides,
    }

    const wrapper = mount(<CheckboxButton {...props} />)

    return {
        wrapper,
        props,
    }
}

describe('<CheckboxN2O />', () => {
    it('создание чекбокса', () => {
        const { wrapper } = setup()

        expect(wrapper.find('input[type="checkbox"]').exists()).toBeTruthy()
    })

    it('классы чекбокса', () => {
        const { wrapper } = setup({ name: 'name', checked: true, disabled: true })

        expect(wrapper.find('label.btn').exists()).toBeTruthy()
        expect(wrapper.find('label.btn-secondary').exists()).toBeTruthy()
        expect(wrapper.find('label.active').exists()).toBeTruthy()
        expect(wrapper.find('label.disabled').exists()).toBeTruthy()
    })

    it('onFocus', () => {
        const onFocus = sinon.spy()
        const { wrapper } = setup({ onFocus })

        wrapper.find('Input').simulate('focus')
        expect(onFocus.calledOnce).toBe(true)
    })

    it('onBlur', () => {
        const onBlur = sinon.spy()
        const { wrapper } = setup({ onBlur })

        wrapper.find('Input').simulate('blur')
        expect(onBlur.calledOnce).toBe(true)
    })

    it('onChange', () => {
        const onChange = sinon.spy()
        const { wrapper } = setup({ onChange })

        wrapper.find('Input').simulate('change')
        expect(onChange.calledOnce).toBe(true)
    })

    it('value и checked', () => {
        const { wrapper } = setup({ value: true })

        expect(wrapper.find('Input').props().value).toBe(true)
        expect(wrapper.find('Input').props().checked).toBe(false)
    })
})
