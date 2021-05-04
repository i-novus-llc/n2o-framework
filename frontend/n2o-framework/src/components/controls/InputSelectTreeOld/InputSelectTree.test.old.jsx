import React from 'react'
import { mount } from 'enzyme'

import InputSelectTree from '../InputSelectTree/InputSelectTree'

const setup = (propOverrides = {}) => {
    const props = {
        loading: false,
        value: '',
        disabled: false,
        placeholder: 'test',
        valueFieldId: 'id',
        labelFieldId: 'id',
        filter: 'includes',
        resetOnBlur: false,
        disabledValues: [],
        hasChildrenFieldId: true,
        options: [
            {
                id: '123412',
                parentId: '',
                icon: 'fa fa-square',
                image: 'https://i.stack.imgur.com/2zqqC.jpg',
                hasChildren: true,
            },
            {
                id: '33',
                parentId: '123412',
                icon: 'fa fa-square',
                image: 'https://i.stack.imgur.com/2zqqC.jpg',
                hasChildren: false,
            },
        ],
        ...propOverrides,
    }

    const wrapper = mount(<InputSelectTree {...props} />)

    return {
        props,
        wrapper,
    }
}

describe('<InputSelectTree />', () => {
    it('проверяет создание элемента', () => {
        const { wrapper } = setup()

        expect(wrapper.find('div.n2o-input-select').exists()).toBeTruthy()
    })

    it('проверяет создание детей', () => {
        const { wrapper } = setup()

        expect(
            wrapper.find('ui.n2o-tree-select-item > li.n2o-tree-select').exists(),
        ).toBeTruthy()
    })

    it('проверяет наличие кнопки раскрытия', () => {
        const { wrapper } = setup()

        expect(wrapper.find('span.tree-toggle').exists()).toBeTruthy()
    })

    it('проверяет раскрытие детей', () => {
        const { wrapper } = setup()

        expect(
            wrapper
                .find('ui.n2o-tree-select-item')
                .first()
                .children()
                .at(1)
                .props().style.display,
        ).toBe('none')
        wrapper
            .find('span.tree-toggle')
            .first()
            .simulate('click')
        expect(
            wrapper
                .find('ui.n2o-tree-select-item')
                .first()
                .children()
                .at(1)
                .props().style.display,
        ).toBe('block')
    })

    it('проверяет чекбокс', () => {
        const { wrapper } = setup({ hasCheckboxes: true })

        expect(
            wrapper
                .find('input.custom-control-input')
                .first()
                .props().checked,
        ).toBeFalsy()
        wrapper
            .find('button.dropdown-item')
            .first()
            .simulate('click')
        expect(
            wrapper
                .find('input.custom-control-input')
                .first()
                .props().checked,
        ).toBeTruthy()
    })
})
