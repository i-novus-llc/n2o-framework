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
                title: 'Закруглённый вариант',
            },
            {
                id: 'circle',
                component: ImageCell,
                shape: imageShapes.CIRCLE,
                title: 'Круглый вариант',
            },
            {
                id: 'square',
                component: ImageCell,
                shape: imageShapes.SQUARE,
                title: 'Квадрат',
            },
        ],
        datasource: [
            {
                id: 'data',
                rounded: IMAGEURL,
                circle: IMAGEURL,
                square: IMAGEURL,
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
