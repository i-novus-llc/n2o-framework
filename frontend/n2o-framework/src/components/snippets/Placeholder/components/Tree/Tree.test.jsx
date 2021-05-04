import React from 'react'

import Tree from './Tree'

const setup = props => mount(<Tree {...props} />)

describe('Тесты Tree', () => {
    it('chevron отрисовывается', () => {
        const wrapper = setup({ chevron: true })

        expect(wrapper.find('.fa-angle-right').exists()).toEqual(true)
    })
    it('chevron не отрисовывается', () => {
        const wrapper = setup()

        expect(wrapper.find('.fa-angle-right').exists()).toEqual(false)
    })
})
