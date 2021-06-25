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
            'items',
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
            'items',
        )
        expect(wrapper.state()).toEqual({
            items: [
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
            extraItems: [],
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
            'extraItems',
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
            'extraItems',
        )
        expect(wrapper.state()).toEqual({
            extraItems: [
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
            items: [],
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
