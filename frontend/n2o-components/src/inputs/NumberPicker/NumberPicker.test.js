import React from 'react'
import sinon from 'sinon'

import { NumberPicker } from './NumberPicker'

const setup = (propOverrides) => {
    const props = {
    // use this to assign some default props

        ...propOverrides,
    }

    const wrapper = mount(<NumberPicker {...props} />)

    return {
        props,
        wrapper,
    }
}

describe('<NumberPicker />', () => {
    it('нажатие на плюс вызывает onChange', () => {
        const onChange = sinon.spy()
        const { wrapper } = setup({
            onChange,
            value: 2,
        })

        wrapper
            .find('.n2o-number-picker__button')
            .last()
            .simulate('click')
        expect(onChange.calledOnce).toBe(true)
    })
    it('нажатие на минус вызывает onChange', () => {
        const onChange = sinon.spy()
        const { wrapper } = setup({
            onChange,
            value: 2,
        })

        wrapper
            .find('.n2o-number-picker__button')
            .first()
            .simulate('click')
        expect(onChange.calledOnce).toBe(true)
    })
    it('не срабатывает onChange при disabled', () => {
        const onChange = sinon.spy()
        const { wrapper } = setup({
            onChange,
            disabled: true,
            value: 2,
        })

        const button = wrapper.find('.n2o-number-picker__button')

        button.first().simulate('click')
        button.last().simulate('click')
        expect(onChange.notCalled).toBe(true)
    })
})
