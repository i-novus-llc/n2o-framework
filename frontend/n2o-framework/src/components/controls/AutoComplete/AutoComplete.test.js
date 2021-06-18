import React from 'react'

import { AutoComplete } from './AutoComplete'

const setup = (propsOverride) => {
    const props = {
        valueFieldId: 'name',
        options: [
            {
                id: 1,
                name: 'a',
            },
            {
                id: 2,
                name: 'ab',
            },
            {
                id: 3,
                name: 'abc',
            },
        ],
    }

    return mount(<AutoComplete {...props} {...propsOverride} />)
}

describe('<AutoCompelte />', () => {
    it('Компонент отрисовался', () => {
        const wrapper = setup()

        expect(wrapper.find('.n2o-autocomplete').exists()).toBe(true)
    })

    it('popup открывается', () => {
        const wrapper = setup()

        expect(wrapper.find('.n2o-pop-up').exists()).toBe(false)

        wrapper.find('input').simulate('click')

        expect(wrapper.find('.n2o-pop-up').exists()).toBe(true)
    })

    it('список отрисовывается', () => {
        const wrapper = setup()

        wrapper.find('input').simulate('click')

        expect(wrapper.find('button.n2o-eclipse-content').length).toBe(3)
    })

    it('значение изменяется', () => {
        const wrapper = setup()
        const input = wrapper.find('input')

        input.simulate('click')
        input.simulate('change', { target: { value: 'a' } })

        expect(wrapper.state().input).toBe('a')
    })

    it('список фильтруется', () => {
        const wrapper = setup()
        const input = wrapper.find('input')

        input.simulate('click')
        input.simulate('change', { target: { value: 'a' } })
        expect(wrapper.find('button.n2o-eclipse-content').length).toBe(3)
        input.simulate('change', { target: { value: 'ab' } })
        expect(wrapper.find('button.n2o-eclipse-content').length).toBe(2)
        input.simulate('change', { target: { value: 'abc' } })
        expect(wrapper.find('button.n2o-eclipse-content').length).toBe(1)
        input.simulate('change', { target: { value: 'abcd' } })
        expect(wrapper.find('button.n2o-eclipse-content').length).toBe(0)
    })

    describe('мод tags', () => {
        it('значение проставляется', () => {
            const wrapper = setup({
                tags: true,
            })
            const textarea = wrapper.find('textarea')

            textarea.simulate('change', { target: { value: 'some value' } })
            textarea.simulate('keydown', { key: 'Enter' })
            textarea.simulate('change', { target: { value: 'some another value' } })
            textarea.simulate('keydown', { key: 'Enter' })
            expect(wrapper.state().value).toEqual([
                'some value',
                'some another value',
            ])
        })

        it('значение выбирается из списка', () => {
            const wrapper = setup({
                tags: true,
            })
            const textarea = wrapper.find('textarea')
            textarea.simulate('click')
            wrapper.update()

            const buttons = wrapper.find('button.n2o-eclipse-content')
            buttons.first().simulate('click')

            textarea.simulate('click')
            buttons.last().simulate('click')

            expect(wrapper.state().value).toEqual(['a', 'abc'])
        })

        it('значение удаляется', () => {
            const wrapper = setup({
                tags: true,
                value: ['a', 'ab', 'abc'],
            })
            expect(wrapper.state().value).toEqual(['a', 'ab', 'abc'])
            wrapper
                .find('button.close')
                .first()
                .simulate('click')
            expect(wrapper.state().value).toEqual(['ab', 'abc'])
            wrapper
                .find('button.close')
                .last()
                .simulate('click')
            expect(wrapper.state().value).toEqual(['ab'])
            wrapper
                .find('button.close')
                .first()
                .simulate('click')
            expect(wrapper.state().value).toEqual([])
        })
    })
})
