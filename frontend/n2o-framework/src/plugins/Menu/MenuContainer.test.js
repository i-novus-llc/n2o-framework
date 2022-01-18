import React from 'react'

import { MenuContainer } from './MenuContainer'

const setup = (props = {}) => mount(<MenuContainer {...props} render={() => null} />)

describe('Проверка MenuContainer', () => {
    it('проверка  items', async () => {
        const wrapper = setup({
            authProvider: (type, config) => {
                if (!config.config.page.permissions.includes('test')) {
                    throw new Error()
                }
            },
            user: {
                permissions: ['test'],
            },
        })

        await wrapper.instance().checkItem(
            {
                id: 'test1',
                href: '/google.com',
                label: 'button',
                security: {
                    page: {
                        permissions: ['test'],
                    },
                },
            },
            'headerItems',
        )

        await wrapper.instance().checkItem(
            {
                id: 'test1',
                href: '/google.com',
                label: 'button',
                security: {
                    page: {
                        permissions: ['anotherPermission'],
                    },
                },
            },
            'headerItems',
        )
        expect(wrapper.state()).toEqual({
            headerItems: [
                {
                    id: 'test1',
                    href: '/google.com',
                    label: 'button',
                    security: {
                        page: {
                            permissions: ['test'],
                        },
                    },
                },
            ],
            headerExtraItems: [],
            sidebarItems: [],
            sidebarExtraItems: [],
        })
    })

    it('проверка  extraItems', async () => {
        const wrapper = setup({
            authProvider: (type, config) => {
                if (!config.config.page.permissions.includes('test')) {
                    throw new Error()
                }
            },
            user: {
                permissions: ['test'],
            },
        })

        await wrapper.instance().checkItem(
            {
                id: 'test1',
                href: '/google.com',
                label: 'button',
                security: {
                    page: {
                        permissions: ['test'],
                    },
                },
            },
            'headerExtraItems',
        )

        await wrapper.instance().checkItem(
            {
                id: 'test1',
                href: '/google.com',
                label: 'button',
                security: {
                    page: {
                        permissions: ['anotherPermission'],
                    },
                },
            },
            'headerExtraItems',
        )
        expect(wrapper.state()).toEqual({
            headerExtraItems: [
                {
                    id: 'test1',
                    href: '/google.com',
                    label: 'button',
                    security: {
                        page: {
                            permissions: ['test'],
                        },
                    },
                },
            ],
            headerItems: [],
            sidebarItems: [],
            sidebarExtraItems: [],
        })
    })

    it('правильно удаляет subItems', () => {
        const wrapper = setup()
        wrapper.setState({
            items: [
                {
                    id: 'menuItem0',
                    label: 'ссылка',
                    href: '/',
                    type: 'link',
                    icon: 'icon',
                    linkType: 'outer',
                    security: {
                        roles: ['admin'],
                    },
                },
                {
                    id: 'menuItem1',
                    label: 'список',
                    href: '/pageRoute',
                    type: 'dropdown',
                    linkType: 'inner',
                    items: [
                        {
                            id: 'menuItem2',
                            label: 'Название страницы',
                            href: '/pageRoute',
                            type: 'link',
                            linkType: 'inner',
                        },
                        {
                            id: 'menuItem3',
                            label: 'элемент списка №2',
                            href: '/pageRoute1',
                            type: 'link',
                            linkType: 'inner',
                        },
                    ],
                },
            ],
            extraItems: [],
        })
        wrapper.instance().setSubItem(
            {
                id: 'menuItem2',
                label: 'Название страницы',
                href: '/pageRoute',
                type: 'link',
                linkType: 'inner',
            },
            'items',
            'menuItem1',
        )
        console.log('Yo!!!', wrapper.state())
        expect(wrapper.state().items[1].items).toEqual([
            {
                id: 'menuItem3',
                label: 'элемент списка №2',
                href: '/pageRoute1',
                type: 'link',
                linkType: 'inner',
            },
        ])
    })

    it('Правильно удаляет пустые dropdown', () => {
        const wrapper = setup({header: {}})
        wrapper.setState({
            headerItems: [
                {
                    id: 'menuItem0',
                    label: 'ссылка',
                    href: '/',
                    type: 'link',
                    icon: 'icon',
                    linkType: 'outer',
                    security: {
                        roles: ['admin'],
                    },
                },
                {
                    id: 'menuItem1',
                    label: 'список',
                    href: '/pageRoute',
                    type: 'dropdown',
                    linkType: 'inner',
                    items: [],
                },
            ],
        })
        expect(wrapper.instance().mapRenderProps().header.menu.items).toEqual([
            {
                id: 'menuItem0',
                label: 'ссылка',
                href: '/',
                type: 'link',
                icon: 'icon',
                linkType: 'outer',
                security: {
                    roles: ['admin'],
                },
            },
        ])
    })
})
