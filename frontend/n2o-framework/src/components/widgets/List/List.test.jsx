import React from 'react'

import { List } from './List'

const setup = (propsOverride) => {
    const props = {
        data: [
            {
                image: {
                    src: 'google.com',
                    alt: 'test',
                },
                header: 'header',
                subHeader: 'subHeader',
                rightTop: 'rightTop',
                rightBottom: 'rightBottom',
                body: 'body',
                extra: 'extra',
            },
        ],
    }

    return mount(<List {...props} {...propsOverride} />)
}

describe('Проверка List', () => {
    it('Компонент отрисовывается', () => {
        const wrapper = setup()

        expect(wrapper.find('.n2o-widget-list').exists()).toEqual(true)
    })

    it('renderRow отрисовывает строки', () => {
        const wrapper = setup()
        const rowItem = wrapper
            .instance()
            .renderRow({ index: 0, key: 0, style: {}, parent: {} })

        expect(React.isValidElement(rowItem)).toEqual(true)
    })
})
