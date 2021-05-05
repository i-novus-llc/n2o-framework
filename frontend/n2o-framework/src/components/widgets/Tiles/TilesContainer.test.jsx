import React from 'react'
import { Provider } from 'react-redux'

import configureStore from '../../../store'
import { WIDGETS } from '../../../core/factory/factoryLevels'
import history from '../../../history'

import TilesContainer from './TilesContainer'
import metadata from './TilesWidget.meta'

const store = configureStore({}, history, {})

const setup = propsOverride => mount(
    <Provider store={store}>
        <TilesContainer
            level={WIDGETS}
            {...metadata.Page_Tiles}
            {...propsOverride}
            id="Page_Tiles"
        />
    </Provider>,
)

describe('<TilesContainer />', () => {
    it('отрисовка', () => {
        const wrapper = setup()

        expect(wrapper).toMatchSnapshot()
    })
})
