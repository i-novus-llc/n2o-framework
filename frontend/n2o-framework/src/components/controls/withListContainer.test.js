import React from 'react'
import { mount } from 'enzyme'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'
import sinon from 'sinon'

import withListContainer from './withListContainer'

const dataUrl = 'test'

const mockStore = configureMockStore()
const store = mockStore({ alerts: {} })

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

    it('handleSearch', async () => {
        const fetchData = sinon.spy()
        const { wrapper } = setup({
            page: 1,
            size: 10,
            labelFieldId: 'label',
            fetchData,
        })

        wrapper
            .find('EmptyComponent')
            .props()
            .onSearch('search string')

        await delay(400)
        expect(fetchData.calledOnce).toBe(true)
        expect(
            fetchData.calledWith(
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
        const fetchData = sinon.spy()
        const { wrapper } = setup({
            page: 1,
            size: 10,
            labelFieldId: 'label',
            fetchData,
        })
        const searchFn = wrapper.find('EmptyComponent').props().onSearch

        searchFn()
        searchFn()
        searchFn()

        await delay(400)
        expect(fetchData.calledOnce).toBe(true)
    })
})
