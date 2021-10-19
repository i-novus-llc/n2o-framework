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
            html={'name is {name} , surname is {surname}'}
            data={{ name: 'Tom', surname: 'Sower' }}
        />,
    )
    expect(wrapper.text()).toBe('name is Tom , surname is Sower')
})

it('correct string in html (with placeholders and empty placeholders)', () => {
    const wrapper = render(
        <Html
            html={'name is {name} , surname is {surname}{}{}'}
            data={{ name: 'Tom', surname: 'Sower' }}
        />,
    )
    expect(wrapper.text()).toBe('name is Tom , surname is Sower')
})

it('correct string in html, (with placeholders and wrong keys)', () => {
    const wrapper = render(
        <Html
            html={
                'name is {name}{test} , surname is {surname}{some key}{12345}{TEST KEY}'
            }
            data={{ name: 'Tom', surname: 'Sower' }}
        />,
    )
    expect(wrapper.text()).toBe('name is Tom , surname is Sower')
})
