import React from 'react'

import Measure from '../Measure'

const setup = propsOverride => shallow(<Measure {...propsOverride} />)

describe('Проверка Measure', () => {
    it('value = true', () => {
        const wrapper = setup({ value: 'value' })

        expect(wrapper.find('span').exists()).toEqual(true)
    })
})
