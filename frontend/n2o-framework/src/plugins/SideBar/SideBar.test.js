import React from 'react'
import {mount} from 'enzyme'

import { SideBar } from './SideBar'

const props = {
    src: "string",
    className: "sidebarCustomClass",
    style: {},
    logo: {
        title: "N2O logo",
        className: "customSimpleHeaderLogoClassName",
        style: {},
        href: "/",
        src: "https://avatars0.githubusercontent.com/u/25926683?s=200&v=4",
    },
    menu: {
        src: "string",
        items: [{
            id: "menuItem0",
            type: 'dropdown',
            href: "/",
            target: '_blank',
            items:[
                {
                    id: "subMenuItem0",
                    type: 'link',
                    href: "/",
                    target: '_blank',
                    items:[],
                    icon: "fa fa-plus",
                    security: {},
                    title: "subMenuItem0",
                    badge: "badge",
                    badgeColor: "warning"
                },
                {
                    id: "subMenuItem2",
                    type: 'link',
                    href: "/",
                    target: '_blank',
                    items:[],
                    icon: "fa fa-square",
                    security: {},
                    title: "subMenuItem1",
                    badge: "badge",
                    badgeColor: "warning"
                }

            ],
            icon: "fa fa-square",
            security: {},
            title: "menuItem0 test- 1",
        },
            {
                id: "menuItem11",
                type: 'dropdown',
                href: "/",
                target: '_blank',
                items:[
                    {
                        id: "subMenuItem0",
                        type: 'link',
                        href: "/",
                        target: '_blank',
                        items:[],
                        icon: "fa fa-square",
                        security: {},
                        title: "subMenuItem0",
                        badge: "badge",
                        badgeColor: "warning"
                    },
                    {
                        id: "subMenuItem2",
                        type: 'link',
                        href: "/",
                        target: '_blank',
                        items:[],
                        icon: "fa fa-plus",
                        security: {},
                        title: "subMenuItem1",
                        badge: "badge",
                        badgeColor: "warning"
                    }

                ],
                icon: "fa fa-pencil",
                security: {},
                title: "menuItem test 2",
            }
        ]
    },
    extraMenu: {
        src: "string",
        items: [{
            id: "menuItem0",
            type: 'dropdown',
            href: "/",
            target: '_blank',
            items:[
                {
                    id: "subMenuItem0",
                    type: 'link',
                    href: "/",
                    target: '_blank',
                    items:[],
                    icon: "fa fa-square",
                    security: {},
                    title: "subMenuItem0",
                }
            ],
            icon: "fa fa square",
            security: {},
            title: "menuItem0",
        }]
    },
    side: 'left',
    defaultState: 'mini',
    toggledState: 'maxi',
    toggleOnHover: false,
    overlay: false,
}

const setup = props => shallow(<SideBar {...props}  />)
const setup2 = props => mount(<SideBar {...props} controlled={true} sidebarOpen={true}/>)

describe('Тесты SideBar', () => {
    it('Отрисовка в соответствии с props mini view', () => {
        const wrapper = setup(props)
        expect(wrapper.find('.n2o-sidebar').exists()).toEqual(true)
        expect(wrapper.find('.mini').exists()).toEqual(true)
    })

    it('Отрисовка в соответствии с props maxi view', () => {
        const wrapper = setup2(props)
        expect(wrapper.find('.n2o-sidebar').exists()).toEqual(true)
        expect(wrapper.find('.n2o-sidebar__item-title.visible').exists()).toEqual(true)
        expect(wrapper.find('.maxi').exists()).toEqual(true)
    })
})
