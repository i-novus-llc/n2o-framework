import React from 'react'
import { Provider } from 'react-redux'

import configureStore from '../../../store'
import { WIDGETS } from '../../../core/factory/factoryLevels'
import history from '../../../history'

import CardsContainer from './CardsContainer'
import metadata from './CardsWidget.meta'

const store = configureStore({}, history, {})

const setup = propsOverride => mount(
    <Provider store={store}>
        <CardsContainer
            level={WIDGETS}
            {...metadata.Page_Cards}
            {...propsOverride}
            id="Page_Cards"
        />
    </Provider>,
)

describe('<CardsContainer />', () => {
    it('отрисовка', () => {
        const wrapper = setup()
        expect(wrapper).toMatchSnapshot()
    })
})
