import React from 'react'
import { storiesOf } from '@storybook/react'
import fetchMock from 'fetch-mock'
import { page } from 'N2oStorybook/fetchMock'

import { WIDGETS } from '../../../core/factory/factoryLevels'
import Factory from '../../../core/factory/Factory'

import metadata from './TreeWidget.meta'

const stories = storiesOf('Виджеты/Дерево', module)

const data = {
    list: [
        { id: '1', label: '0-0-0' },
        { id: '12', label: '0-0-1', parentId: '1' },
        { id: '13', label: '0-0-2', parentId: '1' },
        { id: '2', label: '0-1-0' },
        { id: '21', label: '0-1-1', parentId: '2' },
        { id: '22', label: '0-1-2', parentId: '2' },
        { id: '23', label: '0-1-3', parentId: '2' },
        { id: '3', label: '0-2-0' },
        { id: '4', label: '0-3-0' },
        { id: '41', label: '0-3-1', parentId: '4' },
        { id: '42', label: '0-3-2', parentId: '4' },
        { id: '44', label: '0-4-1', parentId: '42' },
        { id: '45', label: '0-4-1', parentId: '42' },
    ],
}

const dataIcons = {
    list: [
        { id: '1', label: 'Accessibility Icons' },
        {
            id: '12',
            label: 'fa fa-volume-control-phone',
            parentId: '1',
            icon: 'fa fa-volume-control-phone',
        },
        { id: '13', label: 'fa fa-blind', parentId: '1', icon: 'fa fa-blind' },
        { id: '15', label: 'fa fa-deaf', parentId: '1', icon: 'fa fa-deaf' },
        { id: '2', label: 'Hand Icons' },
        {
            id: '21',
            label: 'fa fa-hand-o-right',
            parentId: '2',
            icon: 'fa fa-hand-o-right',
        },
        {
            id: '22',
            label: 'fa fa-hand-pointer-o',
            parentId: '2',
            icon: 'fa fa-hand-pointer-o',
        },
        {
            id: '23',
            label: 'fa fa-thumbs-up',
            parentId: '2',
            icon: 'fa fa-thumbs-up',
        },
        { id: '4', label: 'Transportation Icons' },
        {
            id: '41',
            label: 'fa fa-ambulance',
            parentId: '4',
            icon: 'fa fa-ambulance',
        },
        { id: '42', label: 'fa fa-taxi', parentId: '4', icon: 'fa fa-taxi' },
        {
            id: '44',
            label: 'fa fa-wheelchair-alt',
            parentId: '4',
            icon: 'fa fa-wheelchair-alt',
        },
        {
            id: '45',
            label: 'fa fa-space-shuttle',
            parentId: '4',
            icon: 'fa fa-space-shuttle',
        },
    ],
}

const dataBadge = {
    list: [
        { id: 1, label: 'Users', icon: 'fa fa-users' },
        {
            id: 12,
            label: 'Declan Saif',
            parentId: 1,
            badge: 'online',
            color: 'primary',
            icon: 'fa fa-user-o',
        },
        {
            id: 13,
            label: 'Oscar Danny',
            parentId: 1,
            badge: 'offline',
            color: 'danger',
            icon: 'fa fa-user-o',
        },
        {
            id: 15,
            label: 'Raphael Wayne',
            parentId: 1,
            badge: 'offline',
            color: 'danger',
            icon: 'fa fa-user-o',
        },
        { id: 2, label: 'Admins', icon: 'fa fa-users' },
        {
            id: 21,
            label: 'Anika Tobias',
            parentId: 2,
            icon: 'fa fa-hand-o-right',
            badge: 'online',
            color: 'primary',
        },
        {
            id: 22,
            label: 'Robbie Ashley (Bobbie)',
            parentId: 2,
            icon: 'fa fa-hand-pointer-o',
            badge: 'online',
            color: 'primary',
        },
        {
            id: 23,
            label: 'Leroy Jared',
            parentId: 2,
            icon: 'fa fa-thumbs-up',
            badge: 'online',
            color: 'primary',
        },
    ],
}

stories

    .add('Метаданные', () => {
        fetchMock.restore().get('begin:n2o/data', data)

        return (
            <Factory level={WIDGETS} {...metadata.Page_Tree} id="Page_Tree" />
        )
    })
    .add('Иконки', () => {
        fetchMock.restore().get('begin:n2o/data', dataIcons)

        return (
            <Factory level={WIDGETS} {...metadata.Page_Tree} id="Page_Tree" />
        )
    })
    .add('Баджи', () => {
        fetchMock.restore().get('begin:n2o/data', dataBadge)

        return (
            <Factory level={WIDGETS} {...metadata.Page_Tree} id="Page_Tree" />
        )
    })
    .add('Multi режим (ctrl+click)', () => {
        fetchMock.restore().get('begin:n2o/data', dataBadge)

        const meta = { ...metadata.Page_Tree, multiselect: true }

        return <Factory level={WIDGETS} {...meta} id="Page_Tree" />
    })
    .add('блокировка ноды и чекбокса', () => {
        fetchMock.restore().get('begin:n2o/data', () => {
            const data = { ...dataBadge }

            data.list[0].disabled = true
            data.list[6].disabled = true

            return data
        })

        const meta = {
            ...metadata.Page_Tree,
            hasCheckboxes: true,
            multiselect: true,
        }

        return <Factory level={WIDGETS} {...meta} id="Page_Tree" />
    })
    .add('showLine', () => {
        fetchMock.restore().get('begin:n2o/data', dataBadge)

        const meta = { ...metadata.Page_Tree, showLine: true }

        return <Factory level={WIDGETS} {...meta} id="Page_Tree" />
    })
    .add('Экшен на клик', () => {
        fetchMock.restore().get('begin:n2o/data', dataBadge)
        fetchMock.get('begin:n2o/page', page)

        const rowClick = {
            src: 'perform',
            options: {
                type: 'n2o/overlays/INSERT',
                payload: {
                    pageUrl: '/Uid',
                    size: 'sm',
                    visible: true,
                    closeButton: true,
                    title: 'Новое модальное окно',
                    pageId: 'Uid',
                },
            },
        }

        const meta = { ...metadata.Page_Tree, rowClick }

        return <Factory level={WIDGETS} {...meta} id="Page_Tree" />
    })
    .add('Placeholder', () => {
        fetchMock.restore().get('begin:n2o/data', () => new Promise(res => setTimeout(() => {
            res(dataIcons)
        }, 3000)))

        const newMeta = { ...metadata }

        newMeta.Page_Tree.placeholder = {
            rows: 5,
            chevron: true,
            type: 'tree',
        }

        return <Factory level={WIDGETS} {...newMeta.Page_Tree} id="Page_Tree" />
    })
