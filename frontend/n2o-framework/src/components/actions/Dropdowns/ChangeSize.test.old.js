import React from 'react'
import { mount, shallow } from 'enzyme'
import sinon from 'sinon'

import { ChangeSize } from './ChangeSize'

const setup = (propsOverride) => {
    const props = {}

    return mount(<ChangeSize {...props} {...propsOverride} />)
}

describe('<ChangeSize />', () => {
    it('компоненты должны отрисоваться', () => {
        const wrapper = setup()

        expect(wrapper.find('DropdownItem').exists()).toBeTruthy()
        expect(wrapper.find('DropdownItem').length).toBe(4)
    })

    it('должен вызвать resize', () => {
        const dispatch = sinon.spy()
        const wrapper = setup({
            dispatch,
        })

        wrapper
            .find('DropdownItem')
            .last()
            .simulate('click')
        expect(dispatch.calledTwice).toBeTruthy()
        expect(dispatch.getCall(0).args[0].payload.size).toBe(50)
        expect(dispatch.getCall(1).args[0].payload.options.size).toBe(50)
        expect(dispatch.getCall(1).args[0].payload.options.page).toBe(1)
    })
})
