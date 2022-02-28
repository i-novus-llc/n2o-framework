import React from 'react'

import { MenuContainer } from './MenuContainer'

const setup = (props = {}) => mount(<MenuContainer {...props} render={() => null} />)

const dropdownItem = {
    'id': 'mi1',
    'title': 'Доступ',
    'items': [
        {
            'id': '1',
            'title': 'роль med',
            'href': '/1',
            'linkType': 'inner',
            'type': 'link',
            'security': {
                'custom': {
                    'roles': [
                        'med',
                    ],
                },
            },
        },
        {
            'id': '2',
            'title': 'роль admin',
            'href': '/2',
            'linkType': 'inner',
            'type': 'link',
            'security': {
                'custom': {
                    'roles': [
                        'admin',
                    ],
                },
            },
        },
        {
            'id': '3',
            'title': 'роль edit',
            'href': '/3',
            'linkType': 'inner',
            'type': 'link',
            'security': {
                'custom': {
                    'permissions': [
                        'edit',
                    ],
                },
            },
        },
    ],
    'type': 'dropdown',
}

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

    it('проверка вложенных items, доступен с ролью admin', async () => {
        const wrapper = setup({
            authProvider: (type, config) => {
                if (!config.config.custom.roles.includes('admin')) {
                    throw new Error()
                }
            },
            user: {
                'username': 'Admin',
                'email': null,
                'name': null,
                'surname': null,
                'roles': [
                    'admin',
                ],
                'permissions': [
                    'delete',
                ],
            },
        })

        await wrapper.instance().checkItem(dropdownItem, 'headerItems')

        expect(wrapper.state()).toEqual({
            headerExtraItems: [],
            headerItems: [{
                'id': 'mi1',
                'title': 'Доступ',
                'items': [
                    {
                        'id': '2',
                        'title': 'роль admin',
                        'href': '/2',
                        'linkType': 'inner',
                        'type': 'link',
                        'security': {
                            'custom': {
                                'roles': [
                                    'admin',
                                ],
                            },
                        },
                    },
                ],
                'type': 'dropdown',
            }],
            sidebarItems: [],
            sidebarExtraItems: [],
        })
    })
})
