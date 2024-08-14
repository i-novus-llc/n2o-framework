import React from 'react'

import Tree from './Tree'

const setupComponent = propsOverride => mount(<Tree {...propsOverride} />)

describe('Тесты Tree', () => {
    it('Отрисовка с Filter', () => {
        const wrapper = setupComponent({ filter: 'startsWith' })

        expect(wrapper.find('Filter'))
    })
    it('Отрисовка с ExpandBtn', () => {
        const wrapper = setupComponent({ ExpandBtn: true })

        expect(wrapper.find('ExpandBtn'))
    })
    it('Отрисовка с icon-wrapper', () => {
        const wrapper = setupComponent({ showLine: false })

        expect(wrapper.find('.icon-wrapper'))
    })
})
