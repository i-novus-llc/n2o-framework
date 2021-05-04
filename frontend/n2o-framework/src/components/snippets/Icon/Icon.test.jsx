import React from 'react'
import { shallow } from 'enzyme'

import Icon from './Icon'

const props = {
    className: 'test',
    name: 'test-icon',
    disabled: false,
    style: {
        color: 'red',
    },
    spin: false,
    bordered: false,
    circular: false,
}

describe('<Icon />', () => {
    it('проверяет создание элемента Icon', () => {
        const wrapper = shallow(<Icon {...props} />)

        expect(wrapper.find('i').exists()).toBeTruthy()
    })

    it('проверяет параметр className', () => {
        const wrapper = shallow(<Icon {...props} />)

        expect(wrapper.find(`.${props.className}`).exists()).toBeTruthy()
    })

    it('проверяет стили для элемента', () => {
        const wrapper = shallow(<Icon {...props} />)

        expect(
            wrapper
                .find(`.${props.className}`)
                .getElements()
                .pop().props.style,
        ).toEqual(props.style)
    })

    it('проверяет disabled для иконки', () => {
        props.disabled = true
        const wrapper = shallow(<Icon {...props} />)

        expect(wrapper.find('.disabled').exists()).toBeTruthy()
    })

    it('проверяет класс иконки', () => {
        const wrapper = shallow(<Icon {...props} />)

        expect(wrapper.find(`.${props.name}`).exists()).toBeTruthy()
    })

    it('проверяет вращение иконки', () => {
        props.spin = true
        const wrapper = shallow(<Icon {...props} />)

        expect(wrapper.find('.fa-spin').exists()).toBeTruthy()
    })

    it('проверяет параметр bordered для иконки', () => {
        props.bordered = true
        const wrapper = shallow(<Icon {...props} />)

        expect(wrapper.find('.bordered').exists()).toBeTruthy()
    })

    it('проверяет параметр circular для иконки', () => {
        props.circular = true
        const wrapper = shallow(<Icon {...props} />)

        expect(wrapper.find('.circular').exists()).toBeTruthy()
    })
})
