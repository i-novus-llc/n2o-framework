import React from 'react'
import { storiesOf } from '@storybook/react'

import TextTableHeader from '../../headers/TextTableHeader'
import Table from '../../Table'
import Factory from '../../../../../core/factory/Factory'

import ImageCellJson from './ImageCell.meta.json'
import ImageCell, { ImageCell as ImageCellComponent } from './ImageCell'
import imageShapes from './imageShapes'

const stories = storiesOf('Ячейки/Изображение', module)

stories.addParameters({
    info: {
        propTables: [ImageCellComponent],
        propTablesExclude: [Table, Factory],
    },
})

const IMAGEURL =
  'https://sf-applications.s3.amazonaws.com/Bear/wallpapers/05/july-2020-wallpaper_desktop-3840x1600.png'

stories
    .add('С тултипом', () => {
        const props = {
            id: ImageCellJson.id,
            shape: ImageCellJson.shape,
            style: ImageCellJson.style,
            className: ImageCellJson.className,
            title: ImageCellJson.title,
            model: {
                url:
          'https://sf-applications.s3.amazonaws.com/Bear/wallpapers/05/july-2020-wallpaper_desktop-3840x1600.png',
                tooltip: 'tooltip',
            },
        }

        const tableProps = {
            headers: [
                {
                    id: 'knobs',
                    component: TextTableHeader,
                    label: 'Ячейка с картинкой',
                },
            ],
            cells: [
                {
                    component: ImageCell,
                    tooltipFieldId: 'tooltip',
                    ...props,
                },
            ],
            datasource: [
                {
                    id: 'data',
                    rounded: IMAGEURL,
                    circle: IMAGEURL,
                    square: IMAGEURL,
                    tooltip: 'tooltip',
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
    .add('Вызов действия при клике на картинку', () => {
        const action = {
            src: 'dummyImpl',
        }

        const tableProps = {
            headers: [
                {
                    id: 'knobs',
                    component: TextTableHeader,
                    label: 'Ячейка с картинкой',
                },
            ],
            cells: [
                {
                    component: ImageCell,
                    action,
                    ...ImageCellJson,
                    model: {
                        url:
              'https://sf-applications.s3.amazonaws.com/Bear/wallpapers/05/july-2020-wallpaper_desktop-3840x1600.png',
                    },
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
    .add('Метаданные', () => {
        const props = {
            id: ImageCellJson.id,
            shape: ImageCellJson.shape,
            style: ImageCellJson.style,
            className: ImageCellJson.className,
            title: ImageCellJson.title,
            model: {
                url:
          'https://sf-applications.s3.amazonaws.com/Bear/wallpapers/05/july-2020-wallpaper_desktop-3840x1600.png',
            },
        }

        const tableProps = {
            headers: [
                {
                    id: 'knobs',
                    component: TextTableHeader,
                    label: 'Ячейка с картинкой',
                },
            ],
            cells: [
                {
                    component: ImageCell,
                    ...props,
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
