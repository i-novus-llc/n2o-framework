import React from 'react'
import sinon from 'sinon'

import ListItem from './ListItem'

const setup = propsOverride => mount(
    <ListItem
        leftTop={{
            src: 'google.com',
            alt: 'test',
        }}
        leftBottom="a little description"
        header="header"
        subHeader="subHeader"
        rightTop="rightTop"
        rightBottom="rightBottom"
        body="body"
        extra="extra"
        {...propsOverride}
    />,
)

describe('Проверка ListItem', () => {
    it('секции отрисовываются', () => {
        const wrapper = setup()

        expect(wrapper.find('.n2o-widget-list-item-left-top').exists()).toEqual(
            true,
        )
        expect(wrapper.find('.n2o-widget-list-item-left-bottom').exists()).toEqual(
            true,
        )
        expect(wrapper.find('.n2o-widget-list-item-header').exists()).toEqual(true)
        expect(wrapper.find('.n2o-widget-list-item-subheader').exists()).toEqual(
            true,
        )
        expect(wrapper.find('.n2o-widget-list-item-body').exists()).toEqual(true)
        expect(wrapper.find('.n2o-widget-list-item-right-top').exists()).toEqual(
            true,
        )
        expect(wrapper.find('.n2o-widget-list-item-right-bottom').exists()).toEqual(
            true,
        )
        expect(wrapper.find('.n2o-widget-list-item-extra').exists()).toEqual(true)
    })

    it('срабатывает onClick', () => {
        const onClick = sinon.spy()
        const wrapper = setup({
            onClick,
        })

        wrapper.find('.n2o-widget-list-item').simulate('click')
        expect(onClick.calledOnce).toEqual(true)
    })
})
