import React from 'react'

import List from './List'

const setup = props => mount(<List {...props} />)

describe('Тесты List', () => {
    it('avatar отрисовывается', () => {
        const wrapper = setup({ avatar: true })

        expect(wrapper.find('.avatar').exists()).toEqual(true)
    })
    it('avatar не отрисовывается', () => {
        const wrapper = setup()

        expect(wrapper.find('.avatar').exists()).toEqual(false)
    })
})
