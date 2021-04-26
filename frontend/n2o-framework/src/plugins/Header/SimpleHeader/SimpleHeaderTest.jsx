import React from 'react'

import SimpleHeader from './SimpleHeader'
import SimpleHeaderContainer from './SimpleHeaderContainer'

const SimpleHeaderTest = () => (
    <div>
        <SimpleHeader
            items={[
                {
                    id: 'link',
                    label: 'link',
                    href: '/test',
                    type: 'link',
                },
                {
                    id: 'dropdown',
                    label: 'dropdown',
                    href: '/',
                    type: 'dropdown',
                    subItems: [
                        { id: 'test1', label: 'test2', href: '/' },
                        { id: 'test123', label: 'test1', href: '/' },
                    ],
                },
                {
                    id: 'test',
                    label: 'test',
                    href: '/',
                    type: 'dropdown',
                    subItems: [
                        { id: 'test123s', label: 'test1', href: '/' },
                        { id: 'test12asd3', label: 'test1', href: '/' },
                    ],
                },
            ]}
            extraItems={[
                {
                    id: '213',
                    label: 'ГКБ №7',
                    href: '/test',
                    type: 'text',
                },
                {
                    id: '2131',
                    label: 'Постовая медсестра',
                    href: '/',
                    type: 'dropdown',
                    subItems: [
                        { label: 'test1', href: '/', linkType: 'inner' },
                        { label: 'test1', href: '/' },
                    ],
                },
                {
                    id: '2131',
                    label: 'admin',
                    type: 'dropdown',
                    subItems: [
                        { label: 'test/test', href: '/#/test/test' },
                        { label: 'test1', href: '/' },
                    ],
                },
            ]}
            brand="N2O"
            brandImage="http://getbootstrap.com/assets/brand/bootstrap-solid.svg"
            activeId="link"
        />
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
        <p>sdfsfdsdf</p>
    </div>
)

export default SimpleHeaderTest
