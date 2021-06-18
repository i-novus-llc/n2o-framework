import React from 'react'

import TextTableHeader from '../../headers/TextTableHeader'
import Table from '../../Table'

import StatusCell from './StatusCell'

const setup = (propOverrides) => {
    const props = {
        headers: [
            {
                id: 'StatusCell',
                component: TextTableHeader,
                label: 'StatusCell',
            },
        ],
        cells: [
            {
                id: 'secondary',
                component: StatusCell,
                color: 'info',
                fieldKey: 'test',
                tooltipFieldId: 'tooltip',
            },
        ],
        datasource: [{ test: 'test-text', tooltip: ['tooltip', 'body'] }],
    }

    return mount(
        <Table
            headers={props.headers}
            cells={props.cells}
            datasource={props.datasource}
            redux={false}
            {...propOverrides}
        />,
    )
}

describe('Тесты <StatusCell />', () => {
    it('отрисовка при color equal true с текстом', () => {
        const wrapper = setup()
        expect(wrapper.find('.bg-info').exists()).toEqual(true)
        expect(wrapper.find('StatusCell').contains('test-text')).toEqual(true)
    })
    it('Cell обернут тултипом', () => {
        const wrapper = setup()
        expect(wrapper.find('.list-text-cell__trigger').exists()).toEqual(true)
    })
})
