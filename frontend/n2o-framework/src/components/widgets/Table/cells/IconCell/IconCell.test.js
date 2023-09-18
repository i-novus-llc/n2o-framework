import React from 'react'

import IconCell from './IconCell'
import { textPlaceTypes } from './cellTypes'

const props = {
    id: 'name',
    model: {
        name: 'text',
        age: '12',
    },
    textPlace: textPlaceTypes.RIGHT,
    icon: 'fa fa-minus',
}

describe('<IconCell />', () => {
    it('проверяет создание элемента IconText', () => {
        const wrapper = mount(<IconCell {...props} />)

        expect(wrapper.find('Text span').text()).toEqual(props.model[props.id])
    })

    it('проверяет класс иконки', () => {
        const wrapper = mount(<IconCell {...props} />)
        expect(wrapper.children().getElements()[0].props.icon).toEqual(props.icon)
    })

    it('проверяет расположение текста, верно применился класс', () => {
        props.textPlace = textPlaceTypes.LEFT
        const wrapper = mount(<IconCell {...props} />)
        expect(
            wrapper.find('.icon-cell-container.icon-cell-container__text-left').html(),
        ).toEqual(
            '<div class="icon-cell-container icon-cell-container__text-left"><i class="n2o-icon fa fa-minus"></i><div class="n2o-cell-text"><span class="n2o-snippet">text</span></div></div>',
        )
    })

    it('проверяет типы ячейки', () => {
        let wrapper = mount(<IconCell {...props} />)
        expect(wrapper.find('.n2o-cell-text').exists()).toBeTruthy()
    })
})
