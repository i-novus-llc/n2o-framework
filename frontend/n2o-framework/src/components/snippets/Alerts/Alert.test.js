import React from 'react'
import sinon from 'sinon'

import Alert from './Alert'

const setup = (propOverrides) => {
    const props = {
        visible: true,
        ...propOverrides,
    }

    const wrapper = mount(<Alert {...props} />)

    return {
        props,
        wrapper,
    }
}

describe('<Alert />', () => {
    it('проверяет скрытие кнопки закрытия по closeButton', () => {
        let { wrapper } = setup({ closeButton: true })

        expect(wrapper.find('.n2o-alert-segment__icon-close')).toHaveLength(1)
        wrapper = setup({ closeButton: false }).wrapper
        expect(wrapper.find('.n2o-alert-segment__icon-close')).toHaveLength(0)
    })

    it('проверяет выполнение onDismiss при закрытии', () => {
        const onDismiss = sinon.spy()
        const { wrapper } = setup({ onDismiss, visible: true, closeButton: true })

        wrapper.find('.n2o-alert-segment__icon-close').simulate('click')
        expect(onDismiss.calledOnce).toEqual(true)
    })

    it('проверяет тогл stacktrace area', () => {
        const { wrapper } = setup({ stacktrace: 'stacktrace' })

        expect(wrapper.find('.n2o-alert-segment__stacktrace-area.visible')).toHaveLength(0)
        wrapper.find('.n2o-alert-segment__stacktrace-details-button').at(0).simulate('click')
        expect(wrapper.find('.n2o-alert-segment__stacktrace-area.visible')).toHaveLength(1)
        wrapper.find('.n2o-alert-segment__stacktrace-details-button').at(0).simulate('click')
        expect(wrapper.find('.n2o-alert-segment__stacktrace-area.visible')).toHaveLength(0)
    })

    it('проверяет показан ли Alert', () => {
        const { wrapper } = setup()

        expect(wrapper.props().visible).toBe(true)
    })

    it('проверяет скрыт ли Alert', () => {
        const { wrapper } = setup({
            visible: false,
        })

        expect(wrapper.props().visible).toBe(false)
    })
})
