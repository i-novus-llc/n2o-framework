import React from 'react'
import { storiesOf } from '@storybook/react'
import { Route, Switch } from 'react-router-dom'
import fetchMock from 'fetch-mock'
import { page } from 'N2oStorybook/fetchMock'

import Table from '../../Table'
import TextTableHeader from '../../headers/TextTableHeader'
import Factory from '../../../../../core/factory/Factory'

import LinkCell, { LinkCell as LinkCellComponent } from './LinkCell'
import LinkCellJson from './LinkCell.meta'
import LinkCellWithPerformJSON from './LinkCellWithPerform.meta'

const stories = storiesOf('Ячейки/Ссылка', module)

stories.addParameters({
    info: {
        propTables: [LinkCellComponent],
        propTablesExclude: [Table, Route, Switch, Factory],
    },
})

stories
    .add('Метаданные', () => {
        const props = {
            id: LinkCellJson.id,
            fieldKey: LinkCellJson.fieldKey,
            className: LinkCellJson.className,
            action: LinkCellJson.action,
        }

        const tableProps = {
            headers: [
                {
                    id: 'header',
                    component: TextTableHeader,
                    label: 'Обычная ссылка',
                },
            ],
            cells: [
                {
                    component: LinkCell,
                    ...props,
                },
            ],
            datasource: [
                {
                    id: 'id',
                    name: 'Ссылка на /page/widget/test',
                },
            ],
        }

        return (
            <>
                <Table
                    headers={tableProps.headers}
                    cells={tableProps.cells}
                    datasource={tableProps.datasource}
                />
                <Switch>
                    <Route
                        path="*"
                        component={({ match }) => match.url !== '/' && (
                            <span>
                  Сработал роутер для:
                                {' '}
                                <pre>{match.url}</pre>
                            </span>
                        )
                        }
                    />
                </Switch>
            </>
        )
    })
    .add('LinkCell с иконкой и тултипом', () => {
        fetchMock.restore().get('begin:n2o/page', page)

        const tableProps = {
            headers: [
                {
                    id: 'header',
                    component: TextTableHeader,
                    label: 'Текст и иконка',
                },
                {
                    id: 'header2',
                    component: TextTableHeader,
                    label: 'Только иконка',
                },
            ],
            cells: [
                {
                    component: LinkCell,
                    ...LinkCellWithPerformJSON,
                    icon: 'fa fa-pencil',
                    type: 'iconAndText',
                    tooltipFieldId: 'tooltip',
                },
                {
                    component: LinkCell,
                    ...LinkCellWithPerformJSON,
                    icon: 'fa fa-download',
                    fieldKey: 'none',
                    type: 'icon',
                },
            ],
            datasource: [
                {
                    id: 'id',
                    name: 'Изменить',
                    tooltip: ['tooltip', 'body'],
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
    .add('Отправка экшена', () => {
        fetchMock.restore().get('*', page)

        const tableProps = {
            headers: [
                {
                    id: 'header',
                    component: TextTableHeader,
                    label: 'Отправка экшена',
                },
            ],
            cells: [
                {
                    component: LinkCell,
                    ...LinkCellWithPerformJSON,
                    type: 'text',
                },
            ],
            datasource: [
                {
                    id: 'id',
                    name: 'Ссылка на открытие модального окна',
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
    .add('LinkCell с action', () => {
        const tableProps = {
            headers: [
                {
                    id: 'header',
                    component: TextTableHeader,
                    label: 'Отправка экшена',
                },
            ],
            cells: [
                {
                    component: LinkCell,
                    ...LinkCellWithPerformJSON,
                    type: 'text',
                },
            ],
            datasource: [
                {
                    id: 'id',
                    name: 'Ссылка с экшеном',
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
    .add('Ссылка с url и target=_blank', () => {
        const tableProps = {
            headers: [
                {
                    id: 'header',
                    component: TextTableHeader,
                    label: 'Отправка экшена',
                },
            ],
            cells: [
                {
                    component: LinkCell,
                    ...LinkCellWithPerformJSON,
                    type: 'text',
                    url: 'https://google.com/',
                    target: '_blank',
                },
            ],
            datasource: [
                {
                    id: 'id',
                    name: 'Ссылка с url',
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
