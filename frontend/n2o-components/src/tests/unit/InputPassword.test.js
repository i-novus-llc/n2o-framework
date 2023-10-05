import React from 'react'
import sinon from 'sinon'

import {InputPassword} from '../../inputs/InputPassword'

const setup = (propsOverrides) => {
    const props = { ...propsOverrides }

    const wrapper = mount(<InputPassword {...props} />)

    return {
        props,
        wrapper,
    }
}

describe('<InputPassword />', () => {
    it('проверяет создание элемента', () => {
        const { wrapper } = setup()

        expect(wrapper.find('.n2o-input-password').exists()).toBeTruthy()
    })

    it('проверяет параметр showPasswordBtn', () => {
        const { wrapper } = setup({ showPasswordBtn: true })

        expect(wrapper.find('.n2o-input-password-toggler').exists()).toBeTruthy()
    })

    it('проверяет состояния showPass', () => {
        const { wrapper } = setup({ showPasswordBtn: true })
        const button = wrapper.find('.n2o-input-password-toggler').last()
        button.simulate('click')
        expect(wrapper.find('input').props().type).toBe('text')
        button.simulate('click')
        expect(wrapper.find('input').props().type).toBe('password')
    })
})
