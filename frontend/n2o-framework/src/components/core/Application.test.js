import React from 'react'
import { shallow } from 'enzyme'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'

import Application from './Application'

const setup = (propOverrides) => {
    const props = {
    // use this to assign some default props

        ...propOverrides,
    }

    const wrapper = shallow(
        <Provider dtore={configureMockStore()({})}>
            <Application {...props} />
        </Provider>,
    )

    return {
        props,
        wrapper,
    }
}

describe('<Application />', () => {
    it('tests component existence', () => {
        const { wrapper } = setup()
        expect(wrapper.exists()).toBe(true)
    })
})
