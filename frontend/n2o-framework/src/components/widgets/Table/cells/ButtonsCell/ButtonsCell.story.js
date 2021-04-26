import React from 'react'
import map from 'lodash/map'
import { storiesOf } from '@storybook/react'

import Table from '../../Table'
import TextTableHeader from '../../headers/TextTableHeader'
import TextCell from '../TextCell/TextCell'
import AuthButtonContainer from '../../../../../core/auth/AuthLogin'
import Factory from '../../../../../core/factory/Factory'
import Toolbar from '../../../../buttons/Toolbar'

import metadata from './ButtonsCell.meta'
import ButtonsCell, { ButtonsCell as ButtonCellComponent } from './ButtonsCell'

const stories = storiesOf('Ячейки/ButtonsCell', module)

stories.addParameters({
    info: {
        propTables: [ButtonCellComponent],
        propTablesExclude: [Table, ButtonsCell, Factory, AuthButtonContainer],
    },
})
const toolbar1 = [
    {
        buttons: [
            {
                id: 'testBtn21',
                label: 'Кнопка',
                src: 'StandardButton',
                color: 'success',
                tooltipFieldId: 'tooltip',
            },
            {
                id: 'testBtn24',
                label: 'Кнопка',
                src: 'StandardButton',
                color: 'primary',
            },
            {
                id: 'testBtn27',
                label: 'Кнопка',
                src: 'StandardButton',
                color: 'primary',
            },
            {
                id: 'testBtn29',
                label: 'Кнопка',
                src: 'StandardButton',
                color: 'primary',
            },
        ],
    },
]

const examplesDataSource = [
    {
        description: 'Кнопки',
        buttons: [
            {
                id: '1',
                title: 'Первая',
                hint: 'Первая',
                size: 'md',
                color: 'primary',
            },
            {
                id: '2',
                title: 'Вторая',
                hint: 'Вторая',
                size: 'md',
                color: 'secondary',
            },
            {
                id: '3',
                title: 'Третья',
                hint: 'Третья',
                size: 'md',
                color: 'warning',
            },
        ],
    },
    {
        description: 'Кнопки в виде ссылок',

        buttons: [
            {
                id: '4',
                title: 'Первая',
                hint: 'Первая',
                size: 'md',
            },
            {
                id: '5',
                title: 'Вторая',
                hint: 'Вторая',
                size: 'md',
            },
            {
                id: '6',
                title: 'Третья',
                hint: 'Третья',
                size: 'md',
            },
        ],
    },
    {
        description: 'Кнопки с иконками',

        buttons: [
            {
                id: '7',
                title: 'Первая',
                hint: 'Первая',
                size: 'md',
                icon: 'fa fa-telegram',
                color: 'primary',
            },
            {
                id: '8',
                title: 'Вторая',
                hint: 'Вторая',
                size: 'md',
                icon: 'fa fa-vk',
                color: 'secondary',
            },
            {
                id: '9',
                title: 'Третья',
                hint: 'Третья',
                size: 'md',
                icon: 'fa fa-facebook',
                color: 'warning',
            },
        ],
    },
    {
        description: 'Кнопки в виде ссылок с иконками',

        buttons: [
            {
                id: '10',
                title: 'Первая',
                hint: 'Первая',
                icon: 'fa fa-telegram',
                size: 'md',
            },
            {
                id: '11',
                title: 'Вторая',
                hint: 'Вторая',
                icon: 'fa fa-vk',
                size: 'md',
            },
            {
                id: '12',
                title: 'Третья',
                hint: 'Третья',
                icon: 'fa fa-facebook',
                size: 'md',
            },
        ],
    },
    {
        description: 'Кнопка с выпадающим списком',

        buttons: [
            {
                id: '13',
                title: 'Первая',
                icon: '',
                hint: 'Первая',
                color: 'primary',
            },
            {
                id: '14',
                title: 'Вторая',
                icon: '',
                hint: 'Вторая',
                color: 'danger',
                subMenu: [
                    {
                        id: '15',
                        title: 'Первый',
                        icon: '',
                        color: 'primary',
                    },
                    {
                        id: '16',
                        title: 'Второй',
                        icon: '',
                    },
                ],
            },
        ],
    },
    {
        description: 'Кнопки в виде ссылок с выпадающим списком',
        buttons: [
            {
                id: '17',
                title: 'Первая',
                icon: '',
                hint: 'Первая',
            },
            {
                id: '18',
                title: 'Вторая',
                icon: '',
                hint: 'Вторая',
                subMenu: [
                    {
                        id: '19',
                        title: 'Первый',
                        icon: '',
                    },
                    {
                        id: '20',
                        title: 'Второй',
                    },
                ],
            },
            {
                id: '21',
                title: 'Третья',
                icon: 'fa fa-vk',
                hint: 'Третья',
            },
        ],
    },
    {
        description:
      'Использование разделителей, заголовок и иконок в выпадающем списке',
        buttons: [
            {
                id: '22',
                title: 'Вторая',
                icon: 'fa fa-vk',
                hint: 'Вторая',
                subMenu: [
                    {
                        id: '23',
                        header: true,
                        title: 'Заголовок',
                    },
                    {
                        id: '24',
                        divider: true,
                    },
                    {
                        id: '25',
                        title: 'С иконкой',
                        icon: 'fa fa-telegram',
                    },
                ],
            },
        ],
    },
    {
        description: 'Позиция тултипа',
        buttons: [
            {
                id: 'Left21',
                title: 'Left',
                hintPosition: 'left',
                hint: 'Left',
            },
            {
                id: 'Right21',
                title: 'Right',
                hintPosition: 'right',
                hint: 'Right',
            },
            {
                id: 'Top21',
                title: 'Top',
                hintPosition: 'top',
                hint: 'Top',
            },
            {
                id: 'Bottom21',
                title: 'Bottom',
                hintPosition: 'bottom',
                hint: 'Bottom',
            },
            {
                id: 'Left22',
                title: 'Left',
                hintPosition: 'left',
                icon: 'fa fa-vk',
                hint: 'Left',
                subMenu: [
                    {
                        id: '23',
                        header: true,
                        title: 'Заголовок',
                    },
                ],
            },
            {
                id: 'Right22',
                title: 'Right',
                icon: 'fa fa-vk',
                hintPosition: 'right',
                hint: 'Right',
                subMenu: [
                    {
                        id: '23',
                        header: true,
                        title: 'Заголовок',
                    },
                ],
            },
            {
                id: 'Top22',
                title: 'Top',
                hintPosition: 'top',
                icon: 'fa fa-vk',
                hint: 'Top',
                subMenu: [
                    {
                        id: '23',
                        header: true,
                        title: 'Заголовок',
                    },
                ],
            },
            {
                id: 'Bottom22',
                title: 'Bottom',
                hintPosition: 'bottom',
                icon: 'fa fa-vk',
                hint: 'Bottom',
                subMenu: [
                    {
                        id: '23',
                        header: true,
                        title: 'Заголовок',
                    },
                ],
            },
        ],
    },
    {
        description: 'Вызов действия',
        buttons: [
            {
                id: '26',
                title: 'Действие',
                icon: 'fa fa-vk',
                subMenu: [
                    {
                        id: '27',
                        title: 'Модальное окно',
                        header: true,
                    },
                    {
                        id: '28',
                        divider: true,
                    },
                    {
                        id: '29',
                        title: 'Открыть',
                        action: {
                            src: 'perform',
                            options: {
                                type: 'n2o/overlays/INSERT',
                                payload: {
                                    pageUrl: '/page/widget/create',
                                    size: 'sm',
                                    visible: true,
                                    closeButton: true,
                                    title: 'Новое модальное окно',
                                    pageId: 'undefined',
                                },
                            },
                        },
                    },
                ],
            },
        ],
    },
]

const createTable = data => map(data, ({ buttons, description }) => {
    const tableProps = {
        cells: [
            {
                component: TextCell,
                id: 'description',
                style: { width: '200px' },
            },
            {
                component: ButtonsCell,
                id: 'buttonCells',
                fieldKey: 'buttonCells',
                tooltipFieldId: 'tooltip',
                buttons,
            },
        ],
        headers: [
            {
                component: TextTableHeader,
                id: 'description',
                label: 'Свойства',
            },
            {
                component: TextTableHeader,
                id: 'buttonCells',
                label: 'Отображение',
            },
        ],
        datasource: [{ description, tooltip: ['tooltip', 'body'] }],
    }

    return <Table {...tableProps} />
})

stories
    .add('Метаданные', () => {
        const props = {
            fieldKey: metadata.fieldKey,
            id: metadata.toolbar[0].buttons[0].id,
            className: metadata.className,
            buttons: [
                {
                    title: metadata.toolbar[0].buttons[0].title,
                    icon: metadata.toolbar[0].buttons[0].icon,
                    hint: metadata.toolbar[0].buttons[0].hint,
                    size: metadata.toolbar[0].buttons[0].size,
                    visible: metadata.toolbar[0].buttons[0].visible,
                    disabled: metadata.toolbar[0].buttons[0].disabled,
                    color: metadata.toolbar[0].buttons[0].color,
                    action: metadata.toolbar[0].buttons[0].action,
                },
                {
                    title: 'Скрытая кнопка',
                    color: 'success',
                    security: {
                        roles: ['admin'],
                    },
                },
                {
                    title: 'Скрытый dropdown',
                    color: 'warning',
                    subMenu: [
                        {
                            title: 'Элемент 1',
                        },
                    ],
                    security: {
                        roles: ['admin'],
                    },
                },
                {
                    title: 'Еще',
                    color: 'secondary',
                    subMenu: [
                        {
                            title: 'Элемент 1 (не скрыт)',
                        },
                        {
                            title: 'Элемент 2 (скрыт)',
                            security: {
                                roles: ['admin'],
                            },
                        },
                    ],
                },
            ],
        }

        return (
            <div>
                <small>
          Введите
                    {' '}
                    <mark>admin</mark>
, чтобы увидеть скрытый кнопку в ячейке
                </small>
                <AuthButtonContainer />
                <br />
                {createTable([
                    {
                        description: 'Кнопки',
                        buttons: props.buttons,
                    },
                ])}
            </div>
        )
    })
    .add('Примеры', () => (
        <div style={{ paddingBottom: 50 }}>{createTable(examplesDataSource)}</div>
    ))
