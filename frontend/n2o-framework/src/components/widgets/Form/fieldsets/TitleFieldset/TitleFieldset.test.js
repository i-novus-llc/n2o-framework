import React from 'react'

import TitleFieldset from './TitleFieldset'

const setup = (propsOverride) => {
    const props = {
        label: 'label',
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
        expect(wrapper.find('.title-fieldset-text').text()).toEqual('label')
        expect(wrapper.find('.title-fieldset-subtitle').text()).toEqual('subTitle')
    })
    it('Отрисовывается линия', () => {
        const wrapper = setup({
            showLine: true,
        })
        expect(wrapper.find('.title-fieldset-line').exists()).toEqual(true)
    })
})
