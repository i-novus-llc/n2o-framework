import React from 'react'
import sinon from 'sinon'

import { Table } from './Table'
import TextTableHeader from './headers/TextTableHeader'
import TextCell from './cells/TextCell/TextCell'
import TableRow from './TableRow'

const colors = [
    'table-danger',
    'table-success',
    'table-warning',
    'table-info',
    'table-active',
]
const setup = (propOverrides) => {
    const tableData = [
        {
            id: '1',
            name: 'Foo',
            surname: 'Bar',
            birthday: '01.01.2001',
            rowColor: colors[0],
        },
        {
            id: '2',
            name: 'X',
            surname: 'Y',
            birthday: '01.01.1001',
            rowColor: colors[1],
        },
        {
            id: '3',
            name: 'Test',
            surname: 'Tset',
            birthday: '01.01.0001',
            rowColor: colors[2],
        },
        {
            id: '4',
            name: 'Test',
            surname: 'Tset',
            birthday: '01.01.0001',
            rowColor: colors[3],
        },
        {
            id: '5',
            name: 'Test',
            surname: 'Tset',
            birthday: '01.01.0001',
            rowColor: colors[4],
        },
    ]
    const headers = [
        { id: 'name', component: TextTableHeader, sortable: false, label: 'Имя' },
        {
            id: 'surname',
            component: TextTableHeader,
            sortable: false,
            label: 'Фамилия',
        },
        {
            id: 'birthday',
            component: TextTableHeader,
            sortable: false,
            sorting: 'ASC',
            label: 'Дата рождения',
        },
    ]
    const cells = [
        { id: 'name', component: TextCell, fieldKey: 'name' },
        { id: 'surname', component: TextCell, fieldKey: 'surname' },
        { id: 'birthday', component: TextCell, fieldKey: 'birthday' },
    ]
    return mount(
        <Table
            datasource={tableData}
            headers={headers}
            cells={cells}
            redux={false}
            isActive
            {...propOverrides}
        />,
    )
}

describe('Тесты hasSelect и hasFocus', () => {
    it('проверяет вызов обработчика клика на TableRow', () => {
        const wrapper = setup({ hasFocus: true })
        const handleRow = sinon.spy()
        const stub = sinon.stub(Table.prototype, 'handleRow').callsFake(handleRow)
        wrapper
            .find('tbody tr')
            .at(0)
            .simulate('click')
        stub.restore()
        expect(handleRow.calledOnce).toEqual(true)
    })

    it('проверяет вызывается ли функция установки нового индекса фокуса в state', () => {
        const wrapper = setup({ hasFocus: true, hasSelect: false })
        const setNewFocusIndex = sinon.spy()
        const stub = sinon
            .stub(Table.prototype, 'setNewFocusIndex')
            .callsFake(setNewFocusIndex)
        wrapper
            .find('tbody tr')
            .at(0)
            .simulate('click')
        stub.restore()
        expect(setNewFocusIndex.calledOnce).toEqual(true)
    })

    it('проверяет правильно ли устанавливается значение инфдекса фокуса в state', () => {
        const wrapper = setup({ hasFocus: true, hasSelect: false })
        wrapper
            .find('tbody tr')
            .at(1)
            .simulate('click')
        expect(wrapper.state().focusIndex).toEqual(1)
    })

    it('проверяет вызывается ли функция установки нового индекса селекта в state', () => {
        const wrapper = setup({ hasFocus: false, hasSelect: true })
        const setNewSelectIndex = sinon.spy()
        const stub = sinon
            .stub(Table.prototype, 'setNewSelectIndex')
            .callsFake(setNewSelectIndex)
        wrapper
            .find('tbody tr')
            .at(0)
            .simulate('click')
        stub.restore()
        expect(setNewSelectIndex.calledOnce).toEqual(true)
    })

    it('проверяет правильно ли устанавливается значение инфдекса селекта в state', () => {
        const wrapper = setup({ hasFocus: false, hasSelect: true })
        wrapper
            .find('tbody tr')
            .at(1)
            .simulate('click')
        expect(wrapper.state().selectIndex).toEqual(1)
    })

    it('проверяет правильно ли устанавливается значение индекса селекта при рендере', () => {
        const wrapper = setup({ hasFocus: false, hasSelect: true })
        expect(wrapper.find('.table-active').exists()).toBeTruthy()
    })

    it('проверяет вызывается ли функция установки новых индексов селекта и фокуса в state', () => {
        const wrapper = setup({ hasFocus: false, hasSelect: true })
        const setSelectAndFocus = sinon.spy()
        const stub = sinon
            .stub(Table.prototype, 'setNewSelectIndex')
            .callsFake(setSelectAndFocus)
        wrapper
            .find('tbody tr')
            .at(1)
            .simulate('click')
        stub.restore()
        expect(setSelectAndFocus.calledOnce).toEqual(true)
    })

    it('проверяет правильно ли устанавливается значение инфдексов фокуса и селекта в state', () => {
        const wrapper = setup({ hasFocus: true, hasSelect: true })
        wrapper
            .find('tbody tr')
            .at(1)
            .simulate('click')
        expect(wrapper.state().selectIndex).toEqual(1)
        expect(wrapper.state().focusIndex).toEqual(1)
    })

    it('проверяет, что при клике TableRow  находится в фокусе при hasFocus', () => {
        document.activeElement.blur()
        const wrapper = setup({ hasFocus: true, hasSelect: true })
        const row = wrapper.find('tbody tr').at(1)
        row.simulate('click')
        expect(row.type().toLowerCase()).toEqual(
            document.activeElement.tagName.toLowerCase(),
        )
    })

    it('проверяет, что при клике TableRow  находится в фокусе при hasFocus == false', () => {
        document.activeElement.blur()
        const wrapper = setup({ hasFocus: false })
        const row = wrapper.find('tbody tr').at(1)
        row.simulate('click')
        expect(
            row.type().toLowerCase() === document.activeElement.tagName.toLowerCase(),
        ).toBeFalsy()
    })
})

describe('Тесты цветов', () => {
    it('Проверка цветов колонок', () => {
        const wrapper = setup({
            rowColor:
        '`id == \'1\' ? \'table-red\' : id == \'2\' ? \'table-blue\' : id == \'3\' ? \'table-white\' : \'table-green\'`',
        })

        expect(
            wrapper
                .find('tbody tr')
                .at(0)
                .props()
                .className.includes('table-red'),
        ).toBeTruthy()
        expect(
            wrapper
                .find('tbody tr')
                .at(1)
                .props()
                .className.includes('table-blue'),
        ).toBeTruthy()
        expect(
            wrapper
                .find('tbody tr')
                .at(2)
                .props()
                .className.includes('table-white'),
        ).toBeTruthy()
        expect(
            wrapper
                .find('tbody tr')
                .at(3)
                .props()
                .className.includes('table-green'),
        ).toBeTruthy()
        expect(
            wrapper
                .find('tbody tr')
                .at(4)
                .props()
                .className.includes('table-green'),
        ).toBeTruthy()
    })
})
