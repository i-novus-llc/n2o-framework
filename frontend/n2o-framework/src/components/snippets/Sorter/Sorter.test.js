import React from 'react'

import Sorter from './Sorter'

describe('<Sorter />', () => {
    it('проверяет рендер по умолчанию', () => {
        const wrapper = shallow(<Sorter>Лейбл</Sorter>)
        expect(wrapper).toMatchSnapshot()
    })

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

    it('проверяет различные направление сортировки', () => {
        const wrapper = shallow(<Sorter sortDirection="NONE">Лейбл</Sorter>)
        expect(wrapper).toMatchSnapshot()
        wrapper.setProps({ sorting: 'ASC' })
        expect(wrapper).toMatchSnapshot()
        wrapper.setProps({ sorting: 'DESC' })
        expect(wrapper).toMatchSnapshot()
    })

    it('проверяет работу title', () => {
        const wrapper = shallow(<Sorter title="Супер">Лейбл</Sorter>)
        expect(wrapper.prop('title')).toEqual('Супер')
    })
})
