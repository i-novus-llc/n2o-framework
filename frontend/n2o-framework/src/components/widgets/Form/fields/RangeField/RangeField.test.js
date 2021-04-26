import React from 'react'

import { RangeField } from './RangeField'

const setup = propsOverride => shallow(<RangeField {...propsOverride} />)

describe('Проверка RangeField', () => {
    it('visible = false', () => {
        const wrapper = setup({ visible: false })
        expect(wrapper.children().exists()).toEqual(false)
    })
})
