import React from 'react'

import ContentEditable from './ContentEditable'

const setup = props => mount(<ContentEditable {...props} />)

describe('Тесты ContentEditable', () => {
    it('editable = true', () => {
        const wrapper = setup({ editable: true })

        expect(wrapper.find('.editable').exists()).toEqual(true)
    })
    it('editable = false', () => {
        const wrapper = setup({ editable: false })

        expect(wrapper.find('.editable').exists()).toEqual(false)
    })
})
