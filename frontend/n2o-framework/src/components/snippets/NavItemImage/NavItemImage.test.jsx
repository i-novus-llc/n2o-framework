import React from 'react'

import { NavItemImage } from './NavItemImage'

const props = {
    title: 'title',
    imageSrc: '/static/img/vika.jpg',
    imageShape: 'circle'
}

const imgWidth = '18'

describe('NavItemImage', () => {
    it('gets correct classes for circle', () => {
        const wrapper = shallow(<NavItemImage {...props} />)

        expect(wrapper.find('img.rounded-circle.mr-2').exists()).toBeTruthy()
    })
    it('gets correct classes for rounded', () => {
        const wrapper = shallow(<NavItemImage {...props} imageShape='rounded' />)

        expect(wrapper.find('img.rounded.mr-2').exists()).toBeTruthy()
    })

    it('has alt attribute', () => {
        const wrapper = shallow(<NavItemImage {...props} />)

        expect(wrapper.find('img.rounded-circle').prop('alt')).toEqual(props.title)
    })

    it('has correct width', () => {
        const wrapper = shallow(<NavItemImage {...props} />)

        expect(wrapper.find('img.rounded-circle').prop('width')).toEqual(imgWidth)
    })
})
