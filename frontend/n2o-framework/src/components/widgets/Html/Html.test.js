import React from 'react'
import { render } from 'enzyme'

import Html from './Html.jsx'

it('correct class in html', () => {
    const wrapper = render(
        <Html
            html={'<h1 class="some">name is {name} , surname id {surname}</h1>'}
        />,
    )

    expect(wrapper.find('.some')).toHaveLength(1)
    expect(wrapper.text()).toBe('name is {name} , surname id {surname}')
})

it('correct string in html (with placeholders)', () => {
    const wrapper = render(
        <Html
            html="`'<div class=\'test\'>'+name+' : '+second+'</div>'`"
            data={{ name: 'Tom', second: 'Sower' }}
        />,
    )
    expect(wrapper.text()).toBe("Tom : Sower")
})

it('right html with placeholders', () => {
    const wrapper = render(
        <Html
            html="`'<div class=\'test\'><h1 class=\'test\'>'+name+' : '+second+'</h1></div>'`"
            data={{ name: 'Tom', second: 'Sower', test: 'test' }}
        />,
    )
    console.log('Point!', wrapper.html())
    expect(wrapper.html()).toEqual("<div class=\"test\"><h1 class=\"test\">Tom : Sower</h1></div>")
})

it('right html with placeholders and lines break', () => {
    const wrapper = render(
        <Html
            html="`'<span>\n<span>what is my name</span>\n<span>'+full+'</span>\n</span>'`"
            data={{ name: 'Tom', second: 'Sower', full: 'Tom Sower' }}
        />,
    )

    expect(wrapper.html()).toEqual("<span>\n" +
        "<span>what is my name</span>\n" +
        "<span>Tom Sower</span>\n" +
        "</span>")
})
