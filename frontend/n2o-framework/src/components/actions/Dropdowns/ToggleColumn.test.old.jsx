import React from 'react'
import { mount, shallow } from 'enzyme'
import sinon from 'sinon'

import { ToggleColumn } from './ToggleColumn'

const setup = (propsOverride) => {
    const props = {
        columns: ['name', 'surname'],
    }

    return mount(<ToggleColumn {...props} {...propsOverride} />)
}

describe('<ToggleColumn />', () => {
    it('компонент должен отрисоваться', () => {
        const wrapper = setup()

        expect(wrapper.find('DropdownItem').exists()).toBeTruthy()
        expect(wrapper.find('DropdownItem').length).toBe(2)
    })

    it('должен вернуть null', () => {
        expect(
            setup({ columns: null })
                .find('DropdownItem')
                .exists(),
        ).toBeFalsy()
    })

    it('должен вызвать toggleVisibility', () => {
        const dispatch = sinon.spy()
        const wrapper = setup({
            widgetId: 'testWidget',
            dispatch,
        })

        wrapper
            .find('DropdownItem')
            .last()
            .simulate('click')

        expect(dispatch.calledOnce).toBeTruthy()
        expect(dispatch.getCall(0).args[0].payload.columnId).toBe(1)
        expect(dispatch.getCall(0).args[0].payload.key).toBe('testWidget')
    })
})
