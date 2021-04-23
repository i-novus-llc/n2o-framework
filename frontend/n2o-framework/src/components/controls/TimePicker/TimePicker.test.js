import React from 'react'

import TimePicker from './TimePicker'

const setup = (propOverrides) => {
    const props = {
    // use this to assign some default props

        ...propOverrides,
    }

    const wrapper = mount(<TimePicker {...props} />)

    return {
        props,
        wrapper,
    }
}

describe('<TimePicker />', () => {
    it('проверка дефолтного рендера и проверка открытия popup', () => {
        const { wrapper } = setup()
        expect(wrapper.find('.n2o-pop-up')).toHaveLength(0)
        wrapper.setState({ open: true })
        expect(wrapper.find('.n2o-pop-up')).toHaveLength(1)
        expect(wrapper).toMatchSnapshot()
    })

    it('проверка установки значения', () => {
        const { wrapper } = setup({
            value: '10:20:30',
        })
        expect(
            wrapper
                .find('.n2o-input-text')
                .first()
                .getDOMNode().value,
        ).toBe('10 ч 20 мин 30 сек')
    })

    it('проверка значений - format=digit', () => {
        const { wrapper } = setup({
            format: 'digit',
            defaultValue: '10:20:30',
            mode: ['hours', 'minutes', 'seconds'],
            timeFormat: 'HH:mm:ss',
        })
        const input = wrapper.find('.n2o-input-text')
        input.first().simulate('click')
        expect(wrapper.state().hours).toBe(10)
        expect(wrapper.state().minutes).toBe(20)
        expect(wrapper.state().seconds).toBe(30)
        expect(wrapper.instance().getValue()).toBe('10:20:30')
        expect(wrapper.find('.n2o-time-picker__panel').length).toBe(3)
        wrapper.setProps({
            mode: ['minutes', 'seconds'],
            timeFormat: 'HH:mm:ss',
        })
        expect(wrapper.instance().getValue()).toBe('10:20:30')
        expect(wrapper.find('.n2o-time-picker__panel').length).toBe(2)
        wrapper.setProps({
            mode: ['minutes'],
            timeFormat: 'HH:mm:ss',
        })
        expect(wrapper.instance().getValue()).toBe('10:20:30')
        expect(wrapper.find('.n2o-time-picker__panel').length).toBe(1)
        wrapper.setProps({
            mode: ['hours', 'seconds'],
            timeFormat: 'HH:ss',
        })
        expect(wrapper.instance().getValue()).toBe('10:30')
        expect(wrapper.find('.n2o-time-picker__panel').length).toBe(2)
        wrapper.setProps({
            mode: ['seconds'],
            timeFormat: 'ss',
        })
        expect(wrapper.instance().getValue()).toBe('30')
        expect(wrapper.find('.n2o-time-picker__panel').length).toBe(1)
        wrapper.setProps({
            mode: ['minutes'],
            timeFormat: 'mm:ss',
        })
        expect(wrapper.instance().getValue()).toBe('20:30')
        expect(wrapper.find('.n2o-time-picker__panel').length).toBe(1)
    })

    it('проверка значений - format=symbols', () => {
        const { wrapper } = setup({
            format: 'symbols',
            defaultValue: '10:20:30',
            mode: ['hours', 'minutes', 'seconds'],
            timeFormat: 'HH:mm:ss',
        })
        const input = wrapper.find('.n2o-input-text')
        input.first().simulate('click')
        expect(wrapper.state().hours).toBe(10)
        expect(wrapper.state().minutes).toBe(20)
        expect(wrapper.state().seconds).toBe(30)
        expect(wrapper.instance().getValue()).toBe('10 ч 20 мин 30 сек')
        expect(wrapper.find('.n2o-time-picker__panel').length).toBe(3)
        wrapper.setProps({
            mode: ['minutes', 'seconds'],
            timeFormat: 'HH:mm:ss',
        })
        expect(wrapper.instance().getValue()).toBe('20 мин 30 сек')
        expect(wrapper.find('.n2o-time-picker__panel').length).toBe(2)
        wrapper.setProps({
            mode: ['minutes'],
            timeFormat: 'HH:mm:ss',
        })
        expect(wrapper.instance().getValue()).toBe('20 мин')
        expect(wrapper.find('.n2o-time-picker__panel').length).toBe(1)
        wrapper.setProps({
            mode: ['hours', 'seconds'],
            timeFormat: 'HH:ss',
        })
        expect(wrapper.instance().getValue()).toBe('10 ч 30 сек')
        expect(wrapper.find('.n2o-time-picker__panel').length).toBe(2)
        wrapper.setProps({
            mode: ['seconds'],
            timeFormat: 'ss',
        })
        expect(wrapper.instance().getValue()).toBe('30 сек')
        expect(wrapper.find('.n2o-time-picker__panel').length).toBe(1)
        wrapper.setProps({
            mode: ['minutes'],
            timeFormat: 'mm:ss',
        })
        expect(wrapper.instance().getValue()).toBe('20 мин')
        expect(wrapper.find('.n2o-time-picker__panel').length).toBe(1)
    })

    it('проверка установки значения из popup', () => {
        const { wrapper } = setup()
        const input = wrapper.find('.n2o-input-text')
        input.first().simulate('click')
        wrapper
            .find('.n2o-time-picker__panel')
            .first()
            .find('.n2o-time-picker__panel__item')
            .at(10)
            .simulate('mousedown')
        wrapper
            .find('.n2o-time-picker__panel')
            .at(1)
            .find('.n2o-time-picker__panel__item')
            .at(20)
            .simulate('mousedown')
        wrapper
            .find('.n2o-time-picker__panel')
            .last()
            .find('.n2o-time-picker__panel__item')
            .at(30)
            .simulate('mousedown')
        expect(wrapper.state().hours).toBe(10)
        expect(wrapper.state().minutes).toBe(20)
        expect(wrapper.state().seconds).toBe(30)
        expect(wrapper.instance().getValue()).toBe('10 ч 20 мин 30 сек')
    })
})
