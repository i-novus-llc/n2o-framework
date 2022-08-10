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
            checkSecurity: (config) => {

                if (!config.page.permissions.includes('test')) {
                    throw new Error()
                }
            },
            user: {
                permissions: ['test'],
            },
        })

        const hasAccessItem = {
            id: 'test1',
            href: '/google.com',
            label: 'button',
            security: {
                page: {
                    permissions: ['test'],
                },
            },
        }

        expect(await wrapper.instance().checkItem(hasAccessItem)).toBe(hasAccessItem)
        expect(await wrapper.instance().checkItem(
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
        )).toBe(null)
    })

    it('проверка  extraItems', async () => {
        const wrapper = setup({
            checkSecurity: (config) => {
                if (!config.page.permissions.includes('test')) {
                    throw new Error()
                }
            },
            user: {
                permissions: ['test'],
            },
        })
        const hasAccessItem = {
            id: 'test1',
            href: '/google.com',
            label: 'button',
            security: {
                page: {
                    permissions: ['test'],
                },
            },
        }

        expect(await wrapper.instance().checkItem(hasAccessItem)).toEqual(hasAccessItem)
        expect(await wrapper.instance().checkItem(
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
        )).toBe(null)
    })

    it('проверка вложенных items, доступен с ролью admin', async () => {
        const wrapper = setup({
            checkSecurity: (config) => {
                if (!config.custom.roles.includes('admin')) {
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

        expect(await wrapper.instance().checkItem(dropdownItem)).toEqual({
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
                }
            ],
            'type': 'dropdown',
        })
    })
})
