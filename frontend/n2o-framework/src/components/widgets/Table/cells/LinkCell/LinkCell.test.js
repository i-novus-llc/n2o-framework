import React from 'react'
import sinon from 'sinon'
import { Provider } from 'react-redux'
import configureMockStore from 'redux-mock-store'
import { Link, HashRouter } from 'react-router-dom'

import FactoryProvider from '../../../../../core/factory/FactoryProvider'
import createFactoryConfig from '../../../../../core/factory/createFactoryConfig'

import LinkCell, { LinkCell as LinkComponent } from './LinkCell'
import meta from './LinkCell.meta'

const setupLinkCell = (propsOverride) => {
    const props = {
        ...meta,
        tooltipFieldId: 'tooltip',
        model: {
            name: 'test name',
            tooltip: ['tooltip', 'body'],
        },
    }
    return mount(
        <Provider store={configureMockStore()({ toolbar: {} })}>
            <FactoryProvider config={createFactoryConfig()}>
                <HashRouter>
                    <LinkCell {...props} {...propsOverride} />
                </HashRouter>
            </FactoryProvider>
        </Provider>,
    )
}

describe('Тесты LinkCell', () => {
    it('Отрисовывается', () => {
        const wrapper = setupLinkCell({
            model: {
                name: 'test name',
            },
        })
        expect(wrapper.find('Button').exists()).toEqual(true)
    })
    it('Отрисовывается icon', () => {
        const wrapper = setupLinkCell({
            icon: 'fa fa-plus',
            type: 'icon',
        })
        expect(wrapper.find('.fa.fa-plus').exists()).toEqual(true)
    })

    it('Отрисовыается ссылка по таргету "application"', () => {
        const wrapper = setupLinkCell({
            url: '/n2o/test',
            target: 'application',
        })
        expect(wrapper.find('a').exists()).toEqual(true)
    })
    it('Отрисовывается ссылка по таргету "self"', () => {
        const wrapper = setupLinkCell({
            url: '/n2o/self/test',
            target: 'self',
        })
        expect(wrapper.find('a[href="/n2o/self/test"]').exists()).toEqual(true)
    })
    it('Отрисовывается ссылка по таргету "_blank"', () => {
        const wrapper = setupLinkCell({
            url: 'https://google.com',
            target: '_blank',
        })
        expect(wrapper.find('a[target="_blank"]').exists()).toEqual(true)
    })
    it('Cell обернут тултипом', () => {
        const wrapper = setupLinkCell({
            url: 'https://google.com',
            target: '_blank',
        })
        expect(wrapper.find('.list-text-cell__trigger').exists()).toEqual(true)
    })
})
