import React from 'react'

import TitleFieldset from './TitleFieldset'

const setup = (propsOverride) => {
    const props = {
        title: 'title',
        subTitle: 'subTitle',
    }
    return mount(
        <TitleFieldset render={() => {}} {...props} {...propsOverride} />,
    )
}

describe('Тесты TitleFieldset', () => {
    it('Отрисовывается', () => {
        const wrapper = setup()
        expect(wrapper.find('.title-fieldset').exists()).toEqual(true)
    })
    it('Отрисовываются все тексты', () => {
        const wrapper = setup()
        expect(wrapper.find('.title-fieldset-text').text()).toEqual('title')
        expect(wrapper.find('.title-fieldset-subtitle').text()).toEqual('subTitle')
    })
    it('Отрисовывается линия', () => {
        const wrapper = setup({
            showLine: true,
        })
        expect(wrapper.find('.title-fieldset-line').exists()).toEqual(true)
    })
})
