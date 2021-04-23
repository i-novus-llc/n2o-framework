import React from 'react'

import AdvancedTableExpandedRenderer from './AdvancedTableExpandedRenderer'

const setup = (propsOverride) => {
    const props = {
        expandedFieldId: 'expandedContent',
    }

    return mount(<AdvancedTableExpandedRenderer {...props} {...propsOverride} />)
}

describe('<AdvancedTableExpandedRenderer />', () => {
    it('отрисовывается table контент', () => {
        const wrapper = setup({
            record: {
                expandedContent: {
                    type: 'table',
                    columns: [
                        {
                            id: '1.1',
                            title: 'sub row',
                            dataIndex: 'sub row',
                        },
                    ],
                    data: [
                        {
                            id: '1.1',
                            subName: 'sub value',
                        },
                    ],
                },
            },
        })

        expect(wrapper.find('Table').exists()).toBeTruthy()
    })

    it('отрисовывается html', () => {
        const wrapper = setup({
            record: {
                expandedContent: {
                    type: 'html',
                    value: '<div class="test">test value</div>',
                },
            },
        })

        expect(
            wrapper.find('.n2o-advanced-table-expanded-row-content').exists(),
        ).toBeTruthy()
    })

    it('отрисовывается текстовый контент', () => {
        const wrapper = setup({
            record: {
                expandedContent: {
                    value: 'test value',
                },
            },
        })

        expect(
            wrapper.find('.n2o-advanced-table-expanded-row-content').exists(),
        ).toBeTruthy()
        expect(
            wrapper.find('.n2o-advanced-table-expanded-row-content').text(),
        ).toBe('test value')
    })
})
