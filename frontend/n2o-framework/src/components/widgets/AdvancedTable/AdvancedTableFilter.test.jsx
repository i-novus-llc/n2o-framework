import React from 'react'
import sinon from 'sinon'

import { AdvancedTableFilter } from './AdvancedTableFilter'

const setup = (propsOverride) => {
    const props = {
        id: 1,
        children: [<div>test</div>],
    }

    return shallow(<AdvancedTableFilter {...props} {...propsOverride} />)
}

describe('<AdvancedTableFilter />', () => {
    it('компонент отрисовывается', () => {
        const wrapper = setup()

        expect(
            wrapper.find('.n2o-advanced-table-filter-btn').exists(),
        ).toBeTruthy()
    })

    it('отрабатывает изменение фильтра', () => {
        const onFilter = sinon.spy()
        const wrapper = setup({
            onFilter,
        })

        expect(wrapper.state().value).toBe(null)
        wrapper.instance().onChangeFilter('Ivan')
        expect(wrapper.state().value).toBe('Ivan')
        wrapper.instance().onSetFilter()
        expect(onFilter.calledOnce).toBeTruthy()
        expect(onFilter.getCall(0).args[0]).toEqual({
            id: 1,
            value: 'Ivan',
        })
    })

    it('отрабатывает сброс фильтра', () => {
        const onFilter = sinon.spy()
        const wrapper = setup({
            value: 'Sergey',
            onFilter,
        })

        expect(wrapper.state().value).toBe('Sergey')
        wrapper.instance().onResetFilter()
        expect(wrapper.state().value).toBe('')
        expect(onFilter.calledOnce).toBeTruthy()
    })
})
