import React from 'react'
import sinon from 'sinon'

import Radio from '../Radio/Radio'

import RadioGroup from './RadioGroup'

const setup = (groupOverrides, radioOverrides) => {
    const groupProps = {
    // use this to assign some default props

        ...groupOverrides,
    }

    const radioProps = {
    // use this to assign some default props

        ...radioOverrides,
    }

    const wrapper = mount(
        <RadioGroup {...groupProps}>
            <Radio value="1" {...radioProps} />
            <Radio value="2" {...radioProps} />
        </RadioGroup>,
    )

    return {
        groupProps,
        radioProps,
        wrapper,
    }
}

describe('<RadioGroup />', () => {
    it('creates radios ', () => {
        const { wrapper } = setup()
        expect(wrapper.find('input[type="radio"]')).toHaveLength(2)
    })

    it('sets properties properly', () => {
        const { wrapper } = setup({ name: 'name' })
        expect(wrapper.find(RadioGroup).props().name).toBe('name')
    })

    it('sets properties to input properly', () => {
        const { wrapper } = setup()
        expect(
            wrapper
                .find('input[type="radio"]')
                .first()
                .props().value,
        ).toBe('1')
    })

    it('calls onChange', () => {
        const onChange = sinon.spy()
        const { wrapper } = setup({ onChange })
        wrapper
            .find('input[type="radio"]')
            .first()
            .simulate('change', { target: { checked: true } })
        expect(onChange.calledOnce).toBe(true)
    })

    it('проверяет inline', () => {
        const { wrapper } = setup({ inline: true })

        expect(wrapper.find('div.n2o-radio-inline').exists()).toBeTruthy()
    })
})
