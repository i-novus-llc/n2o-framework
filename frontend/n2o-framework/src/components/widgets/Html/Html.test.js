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
})

it('correct string in html (with placeholders)', () => {
    const wrapper = render(
        <Html
            html="`'<div class='test'><h3 class='class3' style='color:green;'>'+name+' : '+second+'</h3></div>'`"
            data={{ name: 'Tom', second: 'Sower' }}
        />,
    )
    expect(wrapper.text()).toBe("Tom : Sower")
})

it('right html with placeholders', () => {
    const wrapper = render(
        <Html
            html="`'<div class='test'><h3 class='class3' style='color:green;'>'+name+' : '+second+'</h3></div>'`"
            data={{ name: 'Tom', second: 'Sower' }}
        />,
    )

    expect(wrapper.html()).toEqual("<div><div class=\"test\"><h3 class=\"class3\" style=\"color:green;\">Tom : Sower</h3></div></div>")
})
