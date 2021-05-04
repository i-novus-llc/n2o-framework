import React from 'react'
import { mount } from 'enzyme'
import Card from 'reactstrap/lib/Card'

import Panel from './Panel'
import PanelShortHand from './PanelShortHand'

const setup = (propOverrides) => {
    const props = {
        className: 'test',
        color: 'danger',
        icon: 'fa fa-square',
        headerTitle: 'header',
        footerTitle: 'footer',
        hasTabs: false,
        fullScreen: true,
        open: true,
        collapsible: false,
        ...propOverrides,
    }

    const wrapper = mount(
        <PanelShortHand {...props}>
            <h1>123</h1>
        </PanelShortHand>,
    )

    return {
        props,
        wrapper,
    }
}

describe('<PanelShortHand />', () => {
    it('проверяет создание панели', () => {
        const { wrapper, props } = setup()

        expect(wrapper.find(`.${props.className}`).exists()).toBeTruthy()
    })

    it('проверяет стиль панели', () => {
        const { wrapper, props } = setup()

        expect(wrapper.find(Card).props().color).toBe(props.color)
    })

    it('проверяет скрытие футера', () => {
        const { wrapper, props } = setup({ footerTitle: null })

        expect(wrapper.find('.panel-footer').exists()).toBeFalsy()
    })

    it('проверяет возможность скрытия', () => {
        const { wrapper, props } = setup()

        expect(
            wrapper
                .find('.panel-title')
                .find('a')
                .exists(),
        ).toBe(props.collapsible)
    })

    it('проверяет раскрытие панели', () => {
        const { wrapper, props } = setup()

        expect(wrapper.find(Panel).props().open).toBe(props.open)
    })

    it('проверяет создание иконки', () => {
        const { wrapper, props } = setup()

        expect(wrapper.find('i').exists()).toBe(Boolean(props.icon))
    })

    it('проверяет наличие табов', () => {
        const { wrapper, props } = setup()

        expect(wrapper.find('.tab-content').exists()).toBe(props.hasTabs)
    })

    it('проверяет возможность открывать на весь экран', () => {
        const { wrapper, props } = setup()

        expect(wrapper.find('.fa-expand').exists()).toBe(props.fullScreen)
    })

    it('проверяет работу открытия на весь экран', () => {
        const { wrapper, props } = setup({ fullScreen: true })

        wrapper
            .find('li.fullscreen-toggle')
            .children()
            .simulate('click')
        expect(wrapper.find('div.panel-fullscreen').exists()).toBeTruthy()
    })
})
