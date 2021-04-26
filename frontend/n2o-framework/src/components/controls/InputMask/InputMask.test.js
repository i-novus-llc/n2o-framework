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
})
