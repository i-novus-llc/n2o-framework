import React from 'react'
import { mount } from 'enzyme'

import Collapse, { Panel } from './Collapse'

const setup = (propOverrides) => {
    const props = {
    // use this to assign some default props

        ...propOverrides,
    }

    return mount(
        <Collapse>
            <Panel {...props} />
        </Collapse>,
    )
}

describe('<Collapse />', () => {
    it('проверяет создание элемента Collapse', () => {
        const wrapper = setup()

        expect(wrapper.find('.n2o-collapse').exists()).toBeTruthy()
    })

    it('проверяет параметр type', () => {
        let wrapper = setup()

        expect(wrapper.find('.default').exists()).toBeTruthy()

        wrapper = setup({ type: 'line' })
        expect(wrapper.find('.line').exists()).toBeTruthy()

        wrapper = setup({ type: 'divider' })
        expect(wrapper.find('.divider').exists()).toBeTruthy()
    })
})
