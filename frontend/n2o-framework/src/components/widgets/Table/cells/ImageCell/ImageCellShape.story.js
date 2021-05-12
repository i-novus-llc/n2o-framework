import React from 'react'
import { storiesOf } from '@storybook/react'

import TextTableHeader from '../../headers/TextTableHeader'
import Table from '../../Table'
import Factory from '../../../../../core/factory/Factory'

import imageShapes from './imageShapes'
import ImageCell, { ImageCell as ImageCellComponent } from './ImageCell'

const stories = storiesOf('Ячейки/Изображение', module)
stories.addParameters({
    info: {
        propTables: [ImageCellComponent],
        propTablesExclude: [Table, Factory],
    },
})

const IMAGEURL =
  'https://sf-applications.s3.amazonaws.com/Bear/wallpapers/05/july-2020-wallpaper_desktop-3840x1600.png'

stories.add('Компонент', () => {
    const tableProps = {
        headers: [
            {
                id: 'rounded',
                component: TextTableHeader,
                label: 'Закруглённый',
            },
            {
                id: 'circle',
                component: TextTableHeader,
                label: 'Круглый',
            },
            {
                id: 'square',
                component: TextTableHeader,
                label: 'Квадрат',
            },
        ],
        cells: [
            {
                id: 'rounded',
                component: ImageCell,
                shape: imageShapes.ROUNDED,
                fieldKey: 'url',
                title: '`key`',
                description: 'desc',
            },
            {
                id: 'circle',
                component: ImageCell,
                shape: imageShapes.CIRCLE,
                fieldKey: 'url',
                title: '`key`',
                description: 'desc',
                width: '220px',
                height: '220px',
            },
            {
                id: 'square',
                component: ImageCell,
                shape: imageShapes.SQUARE,
                fieldKey: 'url',
                title: '`key`',
                description: 'desc',
            },
        ],
        datasource: [
            {
                id: 'data',
                rounded: IMAGEURL,
                circle: IMAGEURL,
                square: IMAGEURL,
                url:
          'https://sf-applications.s3.amazonaws.com/Bear/wallpapers/05/july-2020-wallpaper_desktop-3840x1600.png',
                key: 'Title',
                description: 'Description',
            },
        ],
    }

    return (
        <Table
            headers={tableProps.headers}
            cells={tableProps.cells}
            datasource={tableProps.datasource}
        />
    )
})

stories.add('Статусы', () => {
    const statuses = [
        {
            src: 'Status',
            statusFieldId: '`status1`',
            statusIconFieldId: '`icon1`',
            statusPlace: '`place1`',
        },
        {
            src: 'Status',
            statusFieldId: '`status2`',
            statusIconFieldId: '`icon2`',
            statusPlace: '`place2`',
        },
        {
            src: 'Status',
            statusFieldId: '`status3`',
            statusIconFieldId: '`icon3`',
            statusPlace: '`place3`',
        },
        {
            src: 'Status',
            statusFieldId: '`status4`',
            statusIconFieldId: '`icon4`',
            statusPlace: '`place4`',
        },
    ]

    const tableProps = {
        headers: [
            {
                id: 'rounded',
                component: TextTableHeader,
                label: 'Закруглённый',
            },
        ],
        cells: [
            {
                id: 'rounded',
                component: ImageCell,
                shape: imageShapes.ROUNDED,
                fieldKey: 'url',
                title: '`key`',
                description: 'desc',
                width: '600px',
                statuses,
            },
        ],
        datasource: [
            {
                id: 'data',
                rounded: IMAGEURL,
                circle: IMAGEURL,
                square: IMAGEURL,
                url:
          'https://sf-applications.s3.amazonaws.com/Bear/wallpapers/05/july-2020-wallpaper_desktop-3840x1600.png',
                key: 'Title',
                description: 'Description',
                status1: 'status 1 top left',
                status2: 'status 2 top right',
                status3: 'status 3 bottom left',
                status4: 'status 4 bottom right',
                icon1: 'fa fa-edit',
                icon2: 'fa fa-edit',
                icon3: 'fa fa-edit',
                icon4: 'fa fa-edit',
                place1: 'top-left',
                place2: 'top-right',
                place3: 'bottom-left',
                place4: 'bottom-right',
            },
        ],
    }

    return (
        <Table
            headers={tableProps.headers}
            cells={tableProps.cells}
            datasource={tableProps.datasource}
        />
    )
})
