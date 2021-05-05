import React from 'react'
import sinon from 'sinon'

import Switch from './Switch'

const setup = (propsOverrides) => {
    const props = {
        checked: false,
        ...propsOverrides,
    }

    const wrapper = mount(<Switch {...props} />)

    return {
        wrapper,
        props,
    }
}

describe('<Switch />', () => {
    it('создание свича', () => {
        const { wrapper } = setup()

        expect(wrapper.find('.n2o-switch').exists()).toBeTruthy()
    })

    it('проверка переключения', () => {
        const onChange = sinon.spy()
        const props = { onChange }
        const { wrapper } = setup(props)

        expect(onChange.calledOnce).toBeFalsy()
        wrapper.find('.n2o-switch').simulate('click')
        expect(onChange.withArgs(true).calledOnce).toBeTruthy()
    })
})
