import React from 'react'
import { findDOMNode } from 'react-dom'
import sinon from 'sinon'
import { mount } from 'enzyme'

import InputSelect from './InputSelect'

// хак для Popper
global.cancelAnimationFrame = function (callback) {
    setTimeout(callback, 0)
}

const props = {
    loading: false,
    value: '123',
    disabled: false,
    placeholder: '',
    valueFieldId: 'id',
    labelFieldId: 'id',
    statusFieldId: 'status',
    descriptionFieldId: 'desc',
    enabledFieldId: 'isDisabled',
    filter: 'includes',
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

const props4 = { ...props,
    options: [
        {
            id: 123412,
            icon: 'fa fa-square',
            image: 'https://i.stack.imgur.com/2zqqC.jpg',
            status: 'success',
        },
        {
            id: '33',
            icon: 'fa fa-square',
            image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
    ] }

const props2 = {
    loading: false,
    value: '123',
    disabled: false,
    placeholder: '',
    valueFieldId: 'id',
    labelFieldId: 'id',
    statusFieldId: 'status',
    descriptionFieldId: 'desc',
    enabledFieldId: 'isEnabled',
    filter: 'includes',
    resetOnBlur: false,
    disabledValues: [],
    options: [
        {
            id: 123412,
            icon: 'fa fa-square',
            isEnabled: false,
        },
    ],
}

const props3 = { ...props,
    options: [
        {
            id: 123412,
            icon: 'fa fa-square',
            image: 'https://i.stack.imgur.com/2zqqC.jpg',
            desc: 'Описание',
        },
        {
            id: '33',
            icon: 'fa fa-square',
            image: 'https://i.stack.imgur.com/2zqqC.jpg',
        },
    ] }

const setup = (propOverrides, defaultProps = props) => {
    const props = { ...defaultProps, ...propOverrides }

    const wrapper = mount(<InputSelect {...props} />)

    return {
        props,
        wrapper,
    }
}

describe('<InputSelect />', () => {
    it('проверяет создание элемента', () => {
        const wrapper = mount(<InputSelect {...props} />)

        expect(wrapper.find('div.n2o-input-select').exists()).toBeTruthy()
    })

    it('проверяет параметров loading', async () => {
        const { wrapper } = setup({ loading: true })
        expect(wrapper.find('.spinner-border').exists()).toBeTruthy()
    })

    it('проверяет параметр value', () => {
        const wrapper = mount(<InputSelect {...props} />)

        expect(wrapper.find('input.n2o-inp').props().value).toBe(
            props.value[props.valueFieldId],
        )
    })

    it('проверяет параметр placeholder', () => {
        const wrapper = mount(<InputSelect {...props} />)

        expect(wrapper.find('input.n2o-inp').props().placeholder).toBe(
            props.placeholder,
        )
    })

    it('проверяет параметр labelFieldId', () => {
        const { wrapper } = setup()

        wrapper.find('li').forEach((node, index) => {
            expect(
                node
                    .children()
                    .props()
                    .children.slice(-1)[0],
            ).toBe(props.options[index][props.labelFieldId])
        })
    })

    it('проверяет параметр valueFieldId', () => {
        const { wrapper, props } = setup({ disabledValues: [] })
        const expectedValue =
      props.options[props.options.length - 1][props.valueFieldId]
        wrapper
            .find('InputSelect')
            .last()
            .last()
            .setState({ isExpanded: true })
        wrapper
            .find('button')
            .last()
            .simulate('click')
        expect(wrapper.find('input.n2o-inp').props().value).toBe(expectedValue)
    })

    it('проверяет параметр disabledValues', () => {
        const { wrapper } = setup({ disabledValues: [props.options[0]] })
        wrapper
            .find('InputSelect')
            .last()
            .setState({ isExpanded: true })
        expect(
            wrapper
                .find('button.n2o-eclipse-content')
                .first()
                .props().disabled,
        ).toBeTruthy()
    })

    it('проверяет параметр onChange', () => {
        const onInput = sinon.spy()
        const { wrapper } = setup({ onInput })

        expect(onInput.calledOnce).toEqual(false)
        wrapper.find('input.n2o-inp').simulate('change')
        expect(onInput.calledOnce).toEqual(true)
    })

    it('проверяет параметр onSelect', () => {
        const onChange = sinon.spy()
        const { wrapper } = setup({ onChange })
        wrapper
            .find('InputSelect')
            .last()
            .setState({ isExpanded: true })
        expect(onChange.calledOnce).toEqual(false)
        wrapper
            .find('button.dropdown-item')
            .last()
            .simulate('click')
        expect(onChange.calledOnce).toEqual(true)
    })

    it('проверяет параметр filter', () => {
        const strStart = props.options[0][props.labelFieldId]
            .toString()
            .substr(0, 2)
        const filteredData = props.options.filter(item => item[props.labelFieldId].toString().startsWith(strStart))
        let { wrapper } = setup({ filter: false })
        wrapper
            .find('InputSelect')
            .last()
            .setState({ isExpanded: true })
        wrapper
            .find('input.n2o-inp')
            .simulate('change', { target: { value: 'asddas' } })
        expect(wrapper.find('button.dropdown-item').length).toEqual(
            props.options.length,
        )

        wrapper = setup({ filter: 'startsWith' }).wrapper
        wrapper
            .find('input.n2o-inp')
            .simulate('change', { target: { value: strStart } })
        wrapper
            .find('InputSelect')
            .last()
            .setState({ isExpanded: true })
        expect(wrapper.find('button.dropdown-item').length).toBe(
            filteredData.length,
        )
    })

    // it('проверяет параметр onScroll', () => {
    //   const onScrollEnd = sinon.spy();
    //   const { wrapper } = setup({ onScrollEnd });
    //   wrapper
    //     .find('InputSelect')
    //     .last()
    //     .setState({ isExpanded: true });
    //
    //   expect(onScrollEnd.calledOnce).toEqual(false);
    //   wrapper.find('.n2o-dropdown-control').simulate('scroll');
    //   expect(onScrollEnd.calledOnce).toEqual(true);
    // });

    it.skip('добавление объекта при resetOnBlur = false', () => {
        const wrapper = mount(
            <InputSelect options={props.options} labelFieldId={props.labelFieldId} />,
        )
        wrapper
            .find('input.form-control')
            .simulate('change', { target: { value: 'custom name' } })
            .simulate('blur')

        expect(wrapper.find('InputSelect').state().value).toEqual([
            { [props.labelFieldId]: 'custom name' },
        ])
    })

    it.skip('проверяет очистку input при resetOnBlur = true', () => {
        const wrapper = mount(
            <InputSelect
                options={props.options}
                labelFieldId={props.labelFieldId}
                resetOnBlur
            />,
        )

        wrapper
            .find('input.form-control')
            .simulate('change', { target: { value: 'custom name' } })
            .simulate('blur')

        expect(wrapper.find('InputSelect').state().value).toEqual([])
    })

    it('проверяет параметр format', () => {
        const { wrapper } = setup({ format: '`id+\' \'+id`' })
        let expected = null

        wrapper.find('li').forEach((node, index) => {
            expected = `${props.options[index].id} ${props.options[index].id}`
            expect(
                node
                    .children()
                    .props()
                    .children.slice(-1)[0],
            ).toBe(expected)
        })
    })

    it('проверяет чекбоксы', () => {
        const { wrapper } = setup({ hasCheckboxes: true })
        wrapper
            .find('InputSelect')
            .last()
            .setState({ isExpanded: true })
        expect(wrapper.find('.custom-checkbox').exists()).toBeTruthy()
        wrapper
            .find('button.dropdown-item')
            .first()
            .simulate('click')
        wrapper
            .find('InputSelect')
            .last()
            .setState({ isExpanded: true })
        expect(
            wrapper
                .find('input.custom-control-input')
                .first()
                .props().checked,
        ).toBeTruthy()
    })

    it('проверяет иконки', () => {
        const fieldId = 'icon'
        const { wrapper, props } = setup({ iconFieldId: fieldId })

        wrapper.find('li').forEach((node, index) => {
            expect(
                node
                    .children()
                    .first()
                    .props().class,
            ).toBe(props.options[index][fieldId])
        })
    })

    it('проверяет группировку', () => {
        const fieldId = 'icon'
        const { wrapper } = setup({ groupFieldId: fieldId })
        wrapper
            .find('InputSelect')
            .last()
            .setState({ isExpanded: true })
        expect(wrapper.find('button.dropdown-item').exists()).toBeTruthy()
        expect(wrapper.find('div.dropdown-divider').exists()).toBeTruthy()
    })

    it('проверяет мульти выбор', () => {
        const { wrapper, props } = setup({ multiSelect: true })
        wrapper
            .find('InputSelect')
            .last()
            .setState({ isExpanded: true })
        wrapper
            .find('button.dropdown-item')
            .first()
            .simulate('click')

        expect(
            wrapper
                .find('span.selected-item')
                .last()
                .props().title,
        ).toBe(props.options[0][props.labelFieldId])
    })

    it('проверяет картинку', () => {
        const fieldId = 'image'
        const { wrapper, props } = setup({ imageFieldId: fieldId })
        wrapper
            .find('InputSelect')
            .last()
            .setState({ isExpanded: true })
        wrapper
            .find('button.dropdown-item')
            .first()
            .simulate('click')
        expect(wrapper.find('.selected-item img').props().src).toBe(
            props.options[0][props.imageFieldId],
        )
    })

    it('Отрисовался status color = success, при statusFieldId', () => {
        const wrapper = mount(<InputSelect {...props4} />)
        wrapper
            .find('InputSelect')
            .last()
            .setState({ isExpanded: true })
        expect(
            wrapper.find('.n2o-eclipse-content__with-status').exists(),
        ).toBeTruthy()
        expect(wrapper.find('.bg-success').exists()).toBeTruthy()
    })
    it('Проверяет наличие описания при descriptionFieldId', () => {
        const wrapper = mount(<InputSelect {...props3} />)
        wrapper
            .find('InputSelect')
            .last()
            .setState({ isExpanded: true })
        expect(
            wrapper.find('.n2o-eclipse-content__description').exists(),
        ).toBeTruthy()
        expect(
            wrapper
                .find('.n2o-eclipse-content__description')
                .first()
                .text(),
        ).toBe('Описание')
    })
    it('проверяет disabled элемент при enabledFieldId', () => {
        const wrapper = mount(<InputSelect {...props2} />)
        wrapper
            .find('InputSelect')
            .last()
            .setState({ isExpanded: true })
        expect(
            wrapper.find('.n2o-eclipse-content.dropdown-item').hasClass('disabled'),
        )
    })
})
