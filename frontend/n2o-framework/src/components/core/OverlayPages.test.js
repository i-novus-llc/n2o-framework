import React from 'react'
import { Provider } from 'react-redux'
import { mount } from 'enzyme'
import mockStore from 'redux-mock-store'

import FactoryProvider from '../../core/factory/FactoryProvider'
import createFactoryConfig from '../../core/factory/createFactoryConfig'

import { OverlayPages } from './OverlayPages'

const setup = propsOverride => mount(
    <Provider
        store={mockStore()({ pages: { test: {} }, overlays: [{ name: 'test' }] })}
    >
        <FactoryProvider config={createFactoryConfig({})}>
            <OverlayPages {...propsOverride} />
        </FactoryProvider>
    </Provider>,
)

describe('<OverlayPages />', () => {
    it('Мод modal', () => {
        const wrapper = setup({
            overlays: [
                {
                    pageId: 'test',
                    name: 'test',
                    visible: true,
                    mode: 'modal',
                },
            ],
        })

        expect(wrapper.find('.n2o-overlay-pages').exists()).toBeTruthy()
    })
})

describe('<OverlayPages />', () => {
    it('Мод dialog', () => {
        const wrapper = setup({
            overlays: [
                {
                    pageId: 'test',
                    name: 'test',
                    visible: true,
                    mode: 'dialog',
                },
            ],
        })

        expect(wrapper.find('.n2o-overlay-pages').exists()).toBeTruthy()
    })
})

describe('<OverlayPages />', () => {
    it('Мод drawer', () => {
        const wrapper = setup({
            overlays: [
                {
                    pageId: 'test',
                    name: 'test',
                    visible: true,
                    mode: 'drawer',
                },
            ],
        })

        expect(wrapper.find('.n2o-overlay-pages').exists()).toBeTruthy()
    })
})
