import React from 'react'
import { Provider } from 'react-redux'
import values from 'lodash/values'
import isObject from 'lodash/isObject'
import isFunction from 'lodash/isFunction'
import sinon from 'sinon'

import factoryResolver from '../../../core/factory/factoryResolver'
import history from '../../../history'
import configureStore from '../../../store'

import { AdvancedTableContainer } from './AdvancedTableContainer'

const store = configureStore({}, history, {})

const props = {
    widgetId: 'tableWidget',
    cells: values(
        factoryResolver([
            {
                src: 'TextCell',
                id: 'name',
            },
        ]),
    ),
    headers: values(
        factoryResolver([
            {
                src: 'TextTableHeader',
                id: 'name',
                sortable: false,
                label: 'Имя',
                width: '50px',
            },
        ]),
    ),
    datasource: [
        {
            id: 1,
            name: 'first name',
        },
    ],
    sorting: {},
    onSort: () => {},
}

const setup = propsOverride => mount(
    <Provider store={store}>
        <AdvancedTableContainer {...props} {...propsOverride} />
    </Provider>,
)
const setupShallow = propsOverride => shallow(<AdvancedTableContainer {...props} {...propsOverride} />)

describe('<AdvancedTableContainer />', () => {
    it('правильно отработал маппинг колонок', () => {
        const wrapper = setup()

        expect(wrapper.find(AdvancedTableContainer).state().columns[0].id).toBe(
            'name',
        )
        expect(
            wrapper.find(AdvancedTableContainer).state().columns[0].sortable,
        ).toBe(false)
        expect(wrapper.find(AdvancedTableContainer).state().columns[0].width).toBe(
            '50px',
        )
        expect(
            isObject(wrapper.find(AdvancedTableContainer).state().columns[0].title),
        ).toBe(true)
        expect(
            wrapper.find(AdvancedTableContainer).state().columns[0].dataIndex,
        ).toBe('name')
        expect(
            wrapper.find(AdvancedTableContainer).state().columns[0].columnId,
        ).toBe('name')
        expect(wrapper.find(AdvancedTableContainer).state().columns[0].key).toBe(
            'name',
        )
        expect(
            isFunction(wrapper.find(AdvancedTableContainer).state().columns[0].render),
        ).toBe(true)
    })

    it('правильно отработал маппинг данных', () => {
        const wrapper = setup()

        expect(wrapper.find(AdvancedTableContainer).state().data).toEqual([
            {
                id: 1,
                name: 'first name',
                key: 1,
            },
        ])
    })

    it('правильно отрабатывает фильтрация', () => {
        const onSetFilter = sinon.spy()
        const onFetch = sinon.spy()

        const wrapper = setupShallow({
            filters: {
                name: 'Sergey',
            },
            onSetFilter,
            onFetch,
        })

        wrapper.instance().handleSetFilter({
            id: 'name',
            value: 'Ivan',
        })

        expect(wrapper.instance().filterValue).toEqual({ name: 'Ivan' })
        expect(onSetFilter.calledOnce).toBe(true)
        expect(onSetFilter.getCall(0).args[0]).toEqual({ name: 'Ivan' })
        expect(onFetch.calledOnce).toBe(true)
    })
})
