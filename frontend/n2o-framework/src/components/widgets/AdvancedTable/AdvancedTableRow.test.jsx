import React from 'react'

import AdvancedTableRow from './AdvancedTableRow'

const setup = (propsOverride) => {
    const props = {
        children: [<td>test</td>],
        model: {
            id: 1,
        },
    }

    return mount(<AdvancedTableRow {...props} {...propsOverride} />)
}

describe('<AdvancedTableRow />', () => {
    it('компонент отрисовывается', () => {
        const wrapper = setup()

        expect(wrapper.find('tr').exists()).toBeTruthy()
        expect(wrapper.find('td').exists()).toBeTruthy()
    })

    it('параметры проставляются', () => {
        const wrapper = setup({
            className: 'test-class',
            isRowActive: true,
            rowClass: 'bg-transparent',
        })

        expect(wrapper.find('tr').props().className).toBe(
            'test-class n2o-table-row n2o-advanced-table-row table-active bg-transparent',
        )
    })
})
