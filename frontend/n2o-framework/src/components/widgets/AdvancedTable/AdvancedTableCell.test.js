import React from 'react'

import AdvancedTableCell from './AdvancedTableCell'

const COMPONENT_CLASS = '.n2o-advanced-table-cell-expand'

const setup = (propsOverride) => {
    const props = {
        children: 'test',
    }

    return mount(<AdvancedTableCell {...props} {...propsOverride} />)
}

describe('<AdvancedTableCell>', () => {
    it('компонент отрисовывается', () => {
        const wrapper = setup()

        expect(wrapper.find(COMPONENT_CLASS).exists()).toBeTruthy()
    })

    it('все параметры корректно проставляются', () => {
        const wrapper = setup({
            hasSpan: true,
            record: {
                span: {
                    colSpan: 3,
                    rowSpan: 2,
                },
            },
        })

        expect(wrapper.find('td').props().colSpan).toBe(3)
        expect(wrapper.find('td').props().rowSpan).toBe(2)
        expect(wrapper.find(COMPONENT_CLASS).text()).toBe('test')
    })
})
