import React from 'react'
import { shallow, mount } from 'enzyme'

import ProgressBarCell from './ProgressBarCell'
import progressBarStyles from './progressBarStyles'

const props = {
    id: 'now',
    model: {
        now: 55,
    },
    animated: false,
    striped: false,
    color: progressBarStyles.DANGER,
    size: 'default',
}
const propsWithTooltip = {
    id: 'now',
    tooltipFieldId: 'tooltip',
    model: {
        tooltip: ['tooltip', 'body'],
    },
    animated: false,
    striped: false,
    color: progressBarStyles.DANGER,
    size: 'default',
}

describe('<ProgressBarCell />', () => {
    it('проверяет создание элемента', () => {
        const wrapper = shallow(<ProgressBarCell {...props} />)

        expect(wrapper.getElements()).toHaveLength(1)
    })

    it('проверяет заполнение прогресс бара', () => {
        const wrapper = shallow(<ProgressBarCell {...props} />)
        expect(wrapper.props().model.now).toEqual(props.model[props.id])
    })

    it('проверяет анимированность', () => {
        const wrapper = shallow(<ProgressBarCell {...props} />)

        expect(wrapper.getElements().pop().props.animated).toEqual(props.animated)
    })

    it('проверяет параметр stripped', () => {
        props.disabled = true
        const wrapper = shallow(<ProgressBarCell {...props} />)

        expect(wrapper.getElements().pop().props.striped).toEqual(props.striped)
    })

    it('проверяет цвет', () => {
        const wrapper = shallow(<ProgressBarCell {...props} />)

        expect(wrapper.getElements().pop().props.color).toEqual(props.color)
    })

    it('проверяет размер', () => {
        const wrapper = mount(<ProgressBarCell {...props} />)

        expect(wrapper.find(`div.${props.size}`).exists()).toBeTruthy()
    })
    it('Cell обернут тултипом', () => {
        const wrapper = mount(<ProgressBarCell {...propsWithTooltip} />)

        expect(wrapper.find('.list-text-cell__trigger').exists()).toEqual(true)
    })
})
