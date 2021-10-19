import React from 'react'

import Sorter from './Sorter'

describe('<Sorter />', () => {
    it('проверяет работы вызова callback при смене сортировки', () => {
        const onSort = jest.fn()
        const wrapper = mount(
            <Sorter sortDirection="NONE" columnKey="test" onSort={onSort}>
        Лейбл
            </Sorter>,
        )
        wrapper.simulate('click')
        expect(onSort).toHaveBeenCalledWith('test', 'ASC')
        wrapper.setProps({ sorting: 'ASC' })
        onSort.mockReset()
        wrapper.simulate('click')
        expect(onSort).toHaveBeenCalledWith('test', 'DESC')
        wrapper.setProps({ sorting: 'DESC' })
        onSort.mockReset()
        wrapper.simulate('click')
        expect(onSort).toHaveBeenCalledWith('test', 'NONE')
    })

    it('проверяет работу title', () => {
        const wrapper = shallow(<Sorter title="Супер">Лейбл</Sorter>)
        expect(wrapper.prop('title')).toEqual('Супер')
    })
})
