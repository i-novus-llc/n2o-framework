import React from 'react'
import { mount } from 'enzyme'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'

import { setModel } from '../../actions/models'
import { dataRequestWidget } from '../../actions/widgets'

import WidgetFilters from './WidgetFilters'

const mockStore = configureMockStore()

const delay = timeout => new Promise(res => setTimeout(res, timeout))

const defaultStateObj = {
    models: {
        filter: {},
    },
}

const setup = (storeObj, propOverrides = {}) => {
    const props = { ...propOverrides }

    const store = mockStore(storeObj || defaultStateObj)

    const wrapper = mount(
        <Provider store={store}>
            <WidgetFilters {...props} />
        </Provider>,
    )

    return {
        props,
        wrapper,
        store,
    }
}

describe('<WidgetFilters />', () => {
    it('проверяет создание элемента', () => {
        const { wrapper } = setup()
        expect(wrapper.find(WidgetFilters).exists()).toBeTruthy()
    })
    it('searchOnChange', async () => {
        const { wrapper, store } = setup(
            {
                models: {
                    filter: {
                        test: 6,
                    },
                },
            },
            {
                fieldsets: [],
                widgetId: 'test',
                searchOnChange: true,
            },
        )
        expect(
            wrapper
                .find('ReduxForm')
                .at(1)
                .props().form,
        ).toBe('test_filter')

        wrapper
            .find('ReduxForm')
            .at(1)
            .props()
            .onChange('test')

        expect(store.getActions()[0]).toEqual({
            meta: {
                form: 'test_filter',
                keepDirty: false,
                keepValues: undefined,
                updateUnregisteredFields: false,
            },
            payload: 6,
            type: '@@redux-form/INITIALIZE',
        })
        expect(store.getActions()[1]).toEqual(setModel('filter', 'test', 'test'))
        await delay(2000)
        expect(store.getActions()[4]).toEqual(
            dataRequestWidget('test', { page: 1 }),
        )
    })
})
