import React from 'react'

import SnippetRating from './SnippetRating'

describe('<SnippetRating />', () => {
    it('Создание компонента ', () => {
        const wrapper = mount(<SnippetRating />)
        expect(wrapper.exists()).toBeTruthy()
    })

    it('проверка max', () => {
        const wrapper = mount(<SnippetRating max={10} />)
        expect(wrapper.find('label').length).toBe(11)
    })
    it('проверка rating', () => {
        const wrapper = mount(<SnippetRating rating={5} max={10} />)
        expect(wrapper.find('input[checked=true]').length).toBe(1)
    })
    it('проверка half', () => {
        const wrapper = mount(<SnippetRating rating={5} max={10} half />)
        expect(wrapper.find('label').length).toBe(21)
        expect(wrapper.find('.rating__label--half').length).toBe(10)
    })
})
