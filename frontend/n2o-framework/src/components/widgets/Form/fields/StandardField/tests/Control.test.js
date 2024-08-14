import React from 'react'

import Control from '../Control'

const setup = propsOverride => shallow(<Control {...propsOverride} />)

describe('Проверка Control', () => {
    it('component === string', () => {
        const wrapper = setup({ component: 'string' })

        expect(wrapper.find('Factory').exists()).toEqual(true)
    })
    it('component !== string', () => {
        const wrapper = setup()

        expect(wrapper.find('Factory').exists()).toEqual(false)
    })
})
