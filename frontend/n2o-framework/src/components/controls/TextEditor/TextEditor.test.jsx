import React from 'react'
import sinon from 'sinon'

import TextEditor from './TextEditor'

const setup = (propsOverride) => {
    const props = {
        value: '<h1>header</h1>',
        className: 'test-class',
    }

    return mount(<TextEditor {...props} {...propsOverride} />)
}

describe('Проверка компонента TextEditor', () => {
    it('Компонент создается и отрисовывается', () => {
        const wrapper = setup()

        expect(wrapper.find('.test-class').exists()).toEqual(true)
    })

    it('Значение прокидывается и проставляется', () => {
        const wrapper = setup()

        expect(wrapper.state().value).toEqual('<h1>header</h1>')
        expect(
            wrapper.find('.public-DraftStyleDefault-ltr span span').text(),
        ).toEqual('header')
    })
    it('onBlur/onFocus вызываются', () => {
        const onFocus = sinon.spy()
        const onBlur = sinon.spy()
        const wrapper = setup({
            onFocus,
            onBlur,
        })

        expect(onFocus.calledOnce).toEqual(false)
        expect(onBlur.calledOnce).toEqual(false)
        wrapper.find('.rdw-editor-main').simulate('focus')
        expect(onFocus.calledOnce).toEqual(true)
        wrapper.find('.rdw-editor-main').simulate('blur')
        expect(onBlur.calledOnce).toEqual(true)
    })
    it('Не отрисовывается по visible = false', () => {
        const wrapper = setup({
            visible: false,
        })

        expect(wrapper.find('.n2o-text-editor').exists()).toEqual(false)
    })
})
