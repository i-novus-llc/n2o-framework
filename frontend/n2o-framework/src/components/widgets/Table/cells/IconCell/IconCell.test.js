import React from 'react'
import { shallow } from 'enzyme'

import IconCell from './IconCell'
import { iconCellTypes, textPlaceTypes } from './cellTypes'

const props = {
    id: 'name',
    model: {
        name: 'text',
        age: '12',
    },
    type: iconCellTypes.ICONANDTEXT,
    textPlace: textPlaceTypes.RIGHT,
    icon: 'fa fa-minus',
}

const propsWithTooltip = {
    id: 'name',
    tooltipFieldId: 'tooltip',
    model: {
        name: 'text',
        age: '12',
        tooltip: ['tooltip', 'body'],
    },
    type: iconCellTypes.ICONANDTEXT,
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
            '<div class="icon-cell-container icon-cell-container__text-left"><i class="n2o-icon fa fa-minus"></i><div class="n2o-cell-text"><span class="">text</span></div></div>',
        )
    })

    it('проверяет типы ячейки', () => {
        let wrapper = mount(<IconCell {...props} />)
        expect(wrapper.find('.n2o-cell-text').exists()).toBeTruthy()

        props.type = iconCellTypes.ICON
        wrapper = mount(<IconCell {...props} />)
        expect(wrapper.find('.n2o-cell-text').exists()).toBeFalsy()
    })
    it('Cell обернут тултипом', () => {
        const wrapper = mount(<IconCell {...propsWithTooltip} />)
        expect(wrapper.find('.list-text-cell__trigger').exists()).toEqual(true)
    })
})
