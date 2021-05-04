import React from 'react'
import { mount } from 'enzyme'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'
import sinon from 'sinon'

import withListContainer from './withListContainer'

const dataUrl = 'test'

const mockStore = configureMockStore()
const store = mockStore({})

const delay = ms => new Promise(r => setTimeout(() => r(), ms))

const EmptyComponent = () => null

const setup = (props = {}) => {
    const ComponentWithListContainer = withListContainer(EmptyComponent)

    const wrapper = mount(
        <Provider store={store}>
            <ComponentWithListContainer {...props} />
        </Provider>,
    )

    return {
        props,
        wrapper,
    }
}

describe('withListContainer HOC test', () => {
    it('проверяет создание элемента', () => {
        const { wrapper } = setup()

        expect(wrapper.find('EmptyComponent').exists()).toBeTruthy()
    })

    it('handleScrollEnd с идеальными параметрами', async () => {
        const _fetchData = sinon.spy()
        const { wrapper } = setup({
            page: 1,
            size: 10,
            count: 300,
            labelFieldId: 'label',
            _fetchData,
        })

        wrapper
            .find('EmptyComponent')
            .props()
            .onScrollEnd()
        expect(_fetchData.calledOnce).toBe(true)
        expect(
            _fetchData.calledWith({ page: 2, size: 10, 'sorting.label': 'ASC' }, true),
        ).toBe(true)
    })

    it('handleScrollEnd без page', async () => {
        const _fetchData = sinon.spy()
        const { wrapper } = setup({ size: 10, count: 300, _fetchData })

        wrapper
            .find('EmptyComponent')
            .props()
            .onScrollEnd()
        expect(_fetchData.calledOnce).toBe(false)
    })

    it('handleScrollEnd без size', async () => {
        const _fetchData = sinon.spy()
        const { wrapper } = setup({ page: 1, count: 300, _fetchData })

        wrapper
            .find('EmptyComponent')
            .props()
            .onScrollEnd()
        expect(_fetchData.calledOnce).toBe(false)
    })

    it('handleScrollEnd без count', async () => {
        const _fetchData = sinon.spy()
        const { wrapper } = setup({ page: 1, size: 10, _fetchData })

        wrapper
            .find('EmptyComponent')
            .props()
            .onScrollEnd()
        expect(_fetchData.calledOnce).toBe(false)
    })

    it('handleOpen', () => {
        const _fetchData = sinon.spy()
        const { wrapper } = setup({
            page: 1,
            size: 10,
            labelFieldId: 'label',
            _fetchData,
        })

        wrapper
            .find('EmptyComponent')
            .props()
            .onOpen()

        expect(_fetchData.calledOnce).toBe(true)
        expect(
            _fetchData.calledWith(
                { size: 10, page: 1, 'sorting.label': 'ASC' },
                false,
            ),
        ).toBe(true)
    })

    it('handleSearch', async () => {
        const _fetchData = sinon.spy()
        const { wrapper } = setup({
            page: 1,
            size: 10,
            labelFieldId: 'label',
            _fetchData,
        })

        wrapper
            .find('EmptyComponent')
            .props()
            .onSearch('search string')

        await delay(400)
        expect(_fetchData.calledOnce).toBe(true)
        expect(
            _fetchData.calledWith(
                {
                    size: 10,
                    page: 1,
                    'sorting.label': 'ASC',
                    search: 'search string',
                },
                false,
            ),
        ).toBe(true)
    })

    it('handleSearch проверка таймера вврода', async () => {
        const _fetchData = sinon.spy()
        const { wrapper } = setup({
            page: 1,
            size: 10,
            labelFieldId: 'label',
            _fetchData,
        })
        const searchFn = wrapper.find('EmptyComponent').props().onSearch

        searchFn()
        searchFn()
        searchFn()

        await delay(400)
        expect(_fetchData.calledOnce).toBe(true)
    })

    it('handleItemOpen', () => {
        const _fetchData = sinon.spy()
        const { wrapper } = setup({
            page: 1,
            size: 10,
            labelFieldId: 'label',
            _fetchData,
        })

        wrapper
            .find('EmptyComponent')
            .props()
            .handleItemOpen('parent_id_value')

        expect(_fetchData.calledOnce).toBe(true)
        expect(
            _fetchData.calledWith(
                {
                    size: 10,
                    page: 1,
                    'sorting.label': 'ASC',
                    'filter.parent_id': 'parent_id_value',
                },
                true,
            ),
        ).toBe(true)
    })
})
