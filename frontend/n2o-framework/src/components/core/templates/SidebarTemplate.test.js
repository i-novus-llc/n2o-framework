import React from 'react'
import { Provider } from 'react-redux'
import { BrowserRouter as Router } from 'react-router-dom'
import { mount } from 'enzyme'

import SidebarTemplate from './SidebarTemplate'
import { makeStore } from '../../../plugins/Header/SimpleHeader/__test__/utils'

const { store } = makeStore()

const setup = (props) => {
    return mount(
        <Provider store={store}>
            <Router>
                <SidebarTemplate {...props} />
            </Router>
        </Provider>,
    )
}

describe('<SidebarTemplate />', () => {
    it('компонент должен отрисоваться', () => {
        const wrapper = setup({items : []})

        expect(wrapper.find('.application').exists()).toBeTruthy()
    })

    it('должны отрисоваться вложенные компоненты', () => {
        const wrapper = setup()

        expect(wrapper.find('.body-container').exists()).toBeTruthy()
        expect(wrapper.find('SideBar').exists()).toBeTruthy()
        expect(wrapper.find('.application-body').exists()).toBeTruthy()
        expect(wrapper.find('Footer').exists()).toBeTruthy()
    })
})
