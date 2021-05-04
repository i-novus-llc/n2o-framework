import React from 'react'

import InputMask from './InputMask'

const setup = (propOverrides) => {
    const props = {
    // use this to assign some default props

        ...propOverrides,
    }

    const wrapper = mount(<InputMask {...props} />)

    return {
        props,
        wrapper,
    }
}

describe('<InputMask />', () => {
    it('has input', () => {
        const { wrapper } = setup()
        expect(wrapper.find('input')).toHaveLength(1)
    })
    it('should call onBlur with prop', () => {
        const onBlur = jest.fn()
        const { wrapper } = setup({ onBlur })
        const testValue = '+7999999999'
        const blurEvent = {
            preventDefault() {},
            nativeEvent: {
                target: { value: testValue },
            },
        }
        const input = wrapper.find('input')
        input.simulate('blur', blurEvent)
        expect(onBlur).toBeCalledWith(testValue)
    })
    it('should call onChange with prop', () => {
        const onChange = jest.fn()
        const { wrapper } = setup({ onChange })
        const testValue = '+7999999999'
        const blurEvent = {
            preventDefault() {},
            target: { value: testValue },
        }
        const input = wrapper.find('input')
        input.simulate('change', blurEvent)
        expect(onChange).toBeCalledWith(testValue)
    })
})
