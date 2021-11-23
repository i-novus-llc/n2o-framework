import React from 'react'

import { NavItemImage } from './NavItemImage'

const props = {
    title: 'title',
    imageSrc: '/static/img/vika.jpg',
}

describe('NavItemImage', () => {
    it('gets correct classes', () => {
        const wrapper = shallow(<NavItemImage {...props} />)

        expect(wrapper.find('img.rounded-circle.mr-2').exists()).toBeTruthy()
    })
    it('has alt attribute', () => {
        const wrapper = shallow(<NavItemImage {...props} />)

        expect(wrapper.find('img.rounded-circle').prop('alt')).toEqual(props.title)
    })
})
