import React from 'react'

import LineFieldset from './LineFieldset'

const setup = (propsOverride) => {
    const props = {
        render: () => {},
        rows: [],
        title: 'title',
        subTitle: 'subTitle',
        showLine: true,
        collapsible: false,

    }

    return mount(<LineFieldset {...props} {...propsOverride} />)
}

describe('Тесты LineFieldset', () => {
    it('Отрисовывается TitleFieldset', () => {
        const wrapper = setup({
            visible: true,
        })
        expect(wrapper.find('.title-fieldset').exists()).toEqual(true)
    })
    it('Отрисовывается CollapseFieldset', () => {
        const wrapper = setup({
            render: () => {},
            rows: [],
            type: 'line',
            label: 'label',
            expand: true,
            hasArrow: true,
            collapsible: true,
            visible: true,
        })
        expect(wrapper.find('.n2o-collapse').exists()).toEqual(true)
    })
})
