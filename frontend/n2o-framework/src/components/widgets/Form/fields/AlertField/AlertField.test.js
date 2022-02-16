import React from 'react'

import AlertField from './AlertField'

const setup = propsOverride => mount(<AlertField {...propsOverride} />)

describe('Проверка AlertField', () => {
    const props = {
        title: 'title',
        text: 'text',
        style: {},
        className: 'customClass',
        color: 'success',
        closeButton: 'true',
        href: 'http://example.org/',
    }

    const emptyProps = {}

    it('Alert отрисовался', () => {
        const wrapper = setup(props)

        expect(wrapper.find('.n2o-alert-field').exists()).toBeTruthy()
    })

    it('Alert отрисовался без props', () => {
        const wrapper = setup(emptyProps)

        expect(wrapper.find('.n2o-alert-field').exists()).toBeTruthy()
    })
})
