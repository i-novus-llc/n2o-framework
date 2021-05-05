import React from 'react'

import CardItem from './CardItem'

const props = {
    header: 'header',
    meta: 'meta',
    text: 'text',
    image: 'image',
    extra: 'extra',
}

const setupComponent = propsOverride => mount(<CardItem {...props} {...propsOverride} />)

describe('Тесты CardItem', () => {
    it('Передаются props', () => {
        const wrapper = setupComponent()

        expect(wrapper.props()).toMatchObject({
            header: 'header',
            meta: 'meta',
            text: 'text',
            image: 'image',
            extra: 'extra',
        })
    })
    it('Отрисовывается и показывает card полученные в props', () => {
        const wrapper = setupComponent()

        expect(wrapper.find('.n2o-card').exists()).toEqual(true)
        expect(wrapper.find('.card-title').exists()).toEqual(true)
        expect(wrapper.find('.card-subtitle').exists()).toEqual(true)
        expect(wrapper.find('.card-text').exists()).toEqual(true)
        expect(wrapper.find('.card-image').exists()).toEqual(true)
        expect(wrapper.find('.card-body').exists()).toEqual(true)
    })
    it('Отрисовывается children', () => {
        const wrapper = setupComponent({ children: 'test' })

        expect(wrapper.find('.n2o-card').contains('test'))
    })
    it('Отрисовывается по orderedItems', () => {
        const wrapper = setupComponent({
            rows: ['image', 'header', 'meta', 'text', 'extra'],
        })

        expect(
            wrapper
                .find('.n2o-card.card')
                .first()
                .children()
                .first()
                .hasClass('card-image'),
        )
    })
})
