import React from 'react'
import { mount } from 'enzyme'
import sinon from 'sinon'

import { GlobalAlerts } from './GlobalAlerts'

const setup = (propsOverride = {}) => {
    const props = {
        alerts: [
            [
                {
                    'field': null,
                    'title': 'Заголовок',
                    'color': 'success',
                    'className': null,
                    'text': 'Привет, мир!',
                    'timeout': 3000,
                    'closeButton': true,
                    'href': null,
                    'placement': 'topRight',
                    'style': null,
                    'stacktrace': null,
                    'time': null,
                    'id': '_i10ex209j',
                },
                {
                    'field': null,
                    'title': 'Заголовок',
                    'color': 'success',
                    'className': null,
                    'text': 'Привет, мир!',
                    'timeout': 5000,
                    'closeButton': true,
                    'href': null,
                    'placement': 'topRight',
                    'style': null,
                    'stacktrace': null,
                    'time': null,
                    'id': '_i10ex209d',
                },
            ],
        ],
    }

    return mount(<GlobalAlerts {...props} {...propsOverride} />)
}

describe('<GlobalAlerts />', () => {
    it('Компонент должен отрисоваться', () => {
        const wrapper1 = setup({
            alerts: [
                [
                    {
                        title: 'Заголовок',
                    },
                ]],
        })
        const wrapper2 = setup({ alerts: [] })

        expect(wrapper1.find('.n2o-alerts-container').exists()).toBeTruthy()
        expect(wrapper2.find('.n2o-alerts-container').exists()).toBeTruthy()
    })
    it('Компонент должен отрисоваться', () => {
        const wrapper = setup()

        expect(wrapper.find('.n2o-alerts-container').exists()).toBeTruthy()
    })

    it('Отрисовка text и title', () => {
        const wrapper = setup()

        expect(
            wrapper
                .find('.n2o-alert-segment__title')
                .at(1)
                .text(),
        ).toBe('Заголовок')

        expect(
            wrapper
                .find('.n2o-alert-segment__text')
                .at(1)
                .text(),
        ).toBe('Привет, мир!')
    })

    it('должен вызваться onDismiss', () => {
        const onDismiss = sinon.spy()
        const wrapper = setup({
            onDismiss,
        })

        wrapper
            .find('.n2o-alert-segment__icon-close')
            .first()
            .simulate('click')

        expect(onDismiss.calledOnce).toBeTruthy()
        expect(onDismiss.getCall(0).args[0]).toBe('topRight')
    })
})
