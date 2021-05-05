import React from 'react'
import { mount, shallow } from 'enzyme'

import ProgressControl from './ProgressControl'

const propsFromSimpleProgressControl = {
    src: 'Progress',
    barText: 'Этот Label должен отрисоваться',
    value: 50,
    max: 100,
    animated: false,
    striped: false,
    color: 'success',
    className: 'progress-control-container',
    barClassName: 'progress-control',
}

const propsFromMultiProgressControl = {
    src: 'Progress',
    multi: true,
    max: 100,
    className: 'progress-control-container',
    barClassName: 'progress-control',
    bars: [
        {
            id: 'progress1',
            bar: true,
            animated: true,
            barText: 'success 25%',
            color: 'success',
        },
        {
            id: 'progress2',
            bar: true,
            animated: false,
            striped: true,
            barText: 'info 25%',
            color: 'info',
        },
        {
            id: 'progress3',
            bar: true,
            animated: false,
            barText: 'danger 25%',
            color: 'danger',
        },
        {
            id: 'progress4',
            bar: true,
            animated: false,
            barText: 'warning 25%',
            color: 'warning',
        },
    ],
    value: {
        progress1: 25,
        progress2: 25,
        progress3: 25,
        progress4: 25,
    },
}

describe('<ProgressControl />', () => {
    it('проверяет создание элемента ProgressControl, без props', () => {
        const wrapper = mount(<ProgressControl />)

        expect(wrapper.find('.progress').exists()).toBeTruthy()
        expect(wrapper.find('.progress-bar').exists()).toBeTruthy()
    })
    it('верная отрисовка with props, default multi = false', () => {
        const wrapper = mount(
            <ProgressControl {...propsFromSimpleProgressControl} />,
        )

        expect(wrapper.find('.progress-control-container').exists()).toBeTruthy()
        expect(wrapper.find('.progress-control').exists()).toBeTruthy()
        expect(wrapper.text()).toBe('Этот Label должен отрисоваться')
        expect(wrapper.props().multi === false).toBeTruthy()
    })
    it('верно беруться values по id из bars, multi = true', () => {
        const wrapper = mount(
            <ProgressControl {...propsFromMultiProgressControl} />,
        )

        expect(wrapper.props().multi === true).toBeTruthy()
        expect(wrapper.text()).toBe('success 25%info 25%danger 25%warning 25%')
    })
})
