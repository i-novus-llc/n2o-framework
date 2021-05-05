import React, { Fragment } from 'react'
import { storiesOf } from '@storybook/react'

import Input from '../../controls/Input/Input'
import TextCell from '../../widgets/Table/cells/TextCell/TextCell'
import TextTableHeader from '../../widgets/Table/headers/TextTableHeader'
import Table from '../../widgets/Table/Table'
import TableCell from '../../widgets/Table/TableCell'
import TableRow from '../../widgets/Table/TableRow'

import CoverSpinner from './CoverSpinner'
import Spinner from './Spinner'
import InlineSpinner from './InlineSpinner'

const stories = storiesOf('UI Компоненты/Спиннеры', module)

const tableData = [
    { id: '1', name: 'Foo', surname: 'Bar', birthday: '01.01.2001' },
    { id: '2', name: 'X', surname: 'Y', birthday: '01.01.1001' },
    { id: '3', name: 'Test', surname: 'Tset', birthday: '01.01.0001' },
]

stories.addParameters({
    info: {
        propTablesExclude: [Table, TextCell, TableCell, TableRow],
    },
})

stories
    .add('Базовый функционал', () => (
        <div>
            <div style={{ display: 'flex' }}>
                <Input />
                <InlineSpinner />
            </div>
            <div style={{ marginTop: 50, position: 'relative' }}>
                <Table>
                    <Table.Header>
                        <Table.Row>
                            <Table.Cell
                                as="th"
                                component={TextTableHeader}
                                id="name"
                                sortable={false}
                                label="Имя"
                            />
                            <Table.Cell
                                as="th"
                                component={TextTableHeader}
                                id="surname"
                                sortable={false}
                                label="Фамилия"
                            />
                            <Table.Cell
                                as="th"
                                component={TextTableHeader}
                                id="birthday"
                                sortable={false}
                                label="Дата рождения"
                            />
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {tableData.map(data => (
                            <Table.Row>
                                <Table.Cell
                                    component={TextCell}
                                    model={data}
                                    id="name"
                                    fieldKey="name"
                                />
                                <Table.Cell id="surname">
                                    <TextCell model={data} fieldKey="surname" />
                                </Table.Cell>
                                <Table.Cell
                                    component={TextCell}
                                    model={data}
                                    id="birthday"
                                    fieldKey="birthday"
                                />
                            </Table.Row>
                        ))}
                    </Table.Body>
                </Table>
                <CoverSpinner message="Таблица загружается..." />
            </div>
        </div>
    ))
    .add('Компонент', () => {
        const props = {
            loading: true,
            type: 'cover',
            text: 'text',
            delay: 5000,
        }

        return (
            <>
                <Spinner {...props}>
                    <div>
                        <div style={{ display: 'flex' }}>
                            <Input />
                        </div>
                        <div style={{ marginTop: 50, position: 'relative' }}>
                            <Table>
                                <Table.Header>
                                    <Table.Row>
                                        <Table.Cell
                                            as="th"
                                            component={TextTableHeader}
                                            id="name"
                                            sortable={false}
                                            label="Имя"
                                        />
                                        <Table.Cell
                                            as="th"
                                            component={TextTableHeader}
                                            id="surname"
                                            sortable={false}
                                            label="Фамилия"
                                        />
                                        <Table.Cell
                                            as="th"
                                            component={TextTableHeader}
                                            id="birthday"
                                            sortable={false}
                                            label="Дата рождения"
                                        />
                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    {tableData.map(data => (
                                        <Table.Row>
                                            <Table.Cell
                                                component={TextCell}
                                                model={data}
                                                id="name"
                                                fieldKey="name"
                                            />
                                            <Table.Cell id="surname">
                                                <TextCell model={data} fieldKey="surname" />
                                            </Table.Cell>
                                            <Table.Cell
                                                component={TextCell}
                                                model={data}
                                                id="birthday"
                                                fieldKey="birthday"
                                            />
                                        </Table.Row>
                                    ))}
                                </Table.Body>
                            </Table>
                        </div>
                    </div>
                </Spinner>
            </>
        )
    })
