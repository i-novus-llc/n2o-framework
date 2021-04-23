import React from 'react'
import { Helmet } from 'react-helmet'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'

import DocumentTitle from './DocumentTitle'

const setupContainer = (propOverrides) => {
    const props = {
    // use this to assign some default props

        ...propOverrides,
    }
    const initialState = {
        models: {
            test: {
                test: 'test',
            },
        },
    }
    const mockStore = configureMockStore()
    const store = mockStore(initialState)
    const wrapper = mount(
        <Provider store={store}>
            <DocumentTitle {...props} />
        </Provider>,
    )

    return {
        props,
        wrapper,
    }
}

describe('<DocumentTitle />', () => {
    it('тестирует резолв в DocumentTitle', () => {
        const { wrapper } = setupContainer({
            htmlTitle: '`test ? test : \'dummy\'`',
            modelLink: 'models.test',
        })
        expect(wrapper.find(Helmet).props().title).toBe('test')
    })
})
