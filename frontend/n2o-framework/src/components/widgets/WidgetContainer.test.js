import React from 'react'
import { Provider } from 'react-redux'

import history from '../../history'
import configureStore from '../../store'

import WidgetContainer from './WidgetContainer'

const NullComponent = () => null
const store = configureStore({}, history, {})

const setup = (propsOverride) => {
    const Component = WidgetContainer({}, 'tree')(NullComponent)
    return mount(
        <Provider store={store}>
            <Component {...propsOverride} />
        </Provider>,
    )
}

describe('<WidgetContainer />', () => {
    it('componentDidMount -> onFetch', () => {
        const wrapper = setup({
            fetchOnInit: true,
            visible: true,
            widgetId: 'testWidgetId',
            pageId: 'testId',
        })
        expect(store.getState().widgets.testWidgetId.pageId).toBe('testId')
    })
    it('componentDidUpdate -> onFetch', () => {
        const wrapper = setup({
            fetchOnInit: false,
            visible: false,
            widgetId: 'testWidgetId',
            pageId: 'testId',
        })
        wrapper.setProps({ visible: false })
        expect(store.getState().widgets.testWidgetId.pageId).toBe('testId')
    })
})
