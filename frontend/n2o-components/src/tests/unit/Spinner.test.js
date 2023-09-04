import React from 'react'

import { Spinner } from '../../layouts/Spinner/Spinner'

const setup = props => mount(<Spinner {...props} />)

describe('Тесты Spinner', () => {
    it('Отрисовывается spinner cover', async () => {
        const wrapper = setup({ loading: true, type: 'cover', delay: 2000 })
        wrapper.setState({ showSpinner: true })
        expect(wrapper.find('.spinner-cover').exists()).toEqual(true)
    })
    it('Отрисовывается spinner background', async () => {
        const wrapper = setup({
            endTimeout: false,
            loading: true,
            type: 'cover',
            transparent: false,
            delay: 2000,
        })
        wrapper.setState({ showSpinner: true })
        expect(wrapper.find('.spinner-background').exists()).toEqual(true)
    })
    it('Отрисовывается children', () => {
        const wrapper = setup({
            endTimeout: false,
            loading: true,
            type: 'cover',
            children: <div className="test" />,
        })
        expect(wrapper.find('.test').exists()).toBe(true)
    })
})
