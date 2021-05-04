import React from 'react'

import StatusText from './StatusText'

const props = {
    text: 'text',
    textPosition: 'right',
    color: 'info',
    className: 'custom',
}

const setup = propsOverride => shallow(<StatusText {...props} {...propsOverride} />)

describe('Тесты StatusText', () => {
    it('Отрисовывается компонент', () => {
        const wrapper = setup()

        expect(wrapper.find('.n2o-status-text').exists()).toEqual(true)
    })
    it('Отрисовывается text', () => {
        const wrapper = setup()

        expect(wrapper.find('.n2o-status-text_text').props().children).toBe(
            props.text,
        )
    })
    it('Отрисовывается верный html', () => {
        const wrapper = setup()

        expect(wrapper.html()).toEqual(
            '<div class="n2o-status-text n2o-snippet custom n2o-status-text__right"><span class="n2o-status-text_icon__left bg-info"></span><p class="n2o-status-text_text">text</p></div>',
        )
    })
})
