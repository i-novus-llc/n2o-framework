import React from 'react'
import { mount } from 'enzyme'
import sinon from 'sinon'

import N2OSelect from './N2OSelect'

const props = {
    loading: false,
    disabled: false,
    placeholder: 'vvv',
    valueFieldId: 'id',
    labelFieldId: 'id',
    filter: 'includes',
    hasSearch: true,
    resetOnBlur: false,
    disabledValues: [],
    options: [
        {
            id: 123412,
            icon: 'fa fa-square',
            image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
        {
            id: '33',
            icon: 'fa fa-square',
            image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
    ],
}

const setup = (propOverrides, defaultProps = props) => {
    const props = { ...defaultProps, ...propOverrides }

    const wrapper = mount(<N2OSelect {...props} />)

    return {
        props,
        wrapper,
    }
}

describe('<N2OSelect />', () => {
    it('проверяет создание элемента', () => {
        const { wrapper } = setup()

        expect(wrapper.find('div.n2o-input-select').exists()).toBeTruthy()
    })

    it('проверяет параметр value', () => {
        const { wrapper } = setup({ value: '123' })

        expect(wrapper.find('InputAddon').props().item).toBe('123')
    })

    it('проверяет параметр placeholder', () => {
        const { wrapper } = setup()

        expect(wrapper.find('input.form-control').props().placeholder).toBe(
            props.placeholder,
        )
    })

    it('проверяет параметр onSelect', () => {
        const { wrapper, props } = setup()
        const expectedValue = props.options[props.options.length - 1]

        wrapper
            .find('button.n2o-eclipse-content')
            .last()
            .simulate('click')

        wrapper.update()
        expect(wrapper.find('InputAddon').props().item).toEqual(expectedValue)
    })

    it('проверяет reset в N2OSelect', () => {
        const { wrapper } = setup()
        wrapper
            .find('button.n2o-eclipse-content')
            .last()
            .simulate('click')
        wrapper
            .find('.n2o-input-clear')
            .at(0)
            .simulate('click')
        expect(
            wrapper
                .find('N2OSelect')
                .last()
                .state().selected,
        ).toEqual([])
    })
    it('проверяет cleanable в N2OSelect', () => {
        const { wrapper } = setup({ cleanable: false })
        wrapper
            .find('button.n2o-eclipse-content')
            .last()
            .simulate('click')

        expect(wrapper.find('.n2o-input-clear')).toHaveLength(0)
    })
    it('проверяет счетчик в placeholder', () => {
        const { wrapper } = setup({
            type: 'checkboxes',
            selectFormatOne: '{size} объект',
            selectFormatFew: '{size} объекта',
            selectFormatMany: '{size} объектов',
        })

        wrapper
            .find('CheckboxN2O')
            .first()
            .simulate('click')

        expect(wrapper.find('.n2o-input-items').contains('1 объект')).toEqual(true)
    })
    it('проверяет счетчик в placeholder c универсальным заголовком', () => {
        const { wrapper } = setup({
            type: 'checkboxes',
            selectFormat: 'Объектов {size} шт',
        })

        wrapper
            .find('CheckboxN2O')
            .first()
            .simulate('click')

        expect(wrapper.find('.n2o-input-items').contains('Объектов 1 шт')).toEqual(
            true,
        )
    })
})
