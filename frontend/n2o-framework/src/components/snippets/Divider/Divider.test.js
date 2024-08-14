import React from 'react'
import { mount, shallow } from 'enzyme/build'
import renderer from 'react-test-renderer'

import { Divider } from './Divider'

const setup = (propOverrides) => {
    const props = {
        children: 'title',
        // use this to assign some default props

        ...propOverrides,
    }

    return mount(<Divider {...props} />)
}

describe('<Divider />', () => {
    it('проверяет создание элемента Divider', () => {
        const wrapper = shallow(<Divider />)

        expect(wrapper.find('.divider-h').exists()).toBeTruthy()
        expect(wrapper.find('.divider_horizontal').exists()).toBeTruthy()
    })
    it('проверяет наличие children', () => {
        const wrapper = setup()

        expect(wrapper.find('span').exists()).toBeTruthy()
    })
    it('проверка на соответствие заголовка = title', () => {
        const wrapper = setup()

        expect(wrapper.text()).toBe('title')
    })
})
