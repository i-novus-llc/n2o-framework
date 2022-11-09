import React from 'react'

import FilterButtonsField from './FilterButtonsField'

const setup = propsOverride => mount(<FilterButtonsField {...propsOverride} />)

describe('Проверка FilterButtonsFiled', () => {
    it('visible = false', () => {
        const wrapper = setup({ visible: false })
        expect(wrapper.find('Buttons').children().exists()).toEqual(false)
    })
})
