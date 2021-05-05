import React from 'react'

import InputHidden from './InputHidden'

const setup = () => {
    const props = {
        value: 'Тестовое значение',
    }

    return mount(<InputHidden {...props} />)
}

describe('Компонент InputHidden', () => {
    it('должен отрисоваться', () => {
        const wrapper = setup()

        expect(wrapper.find('input[type="hidden"]').exists()).toEqual(true)
    })
})
