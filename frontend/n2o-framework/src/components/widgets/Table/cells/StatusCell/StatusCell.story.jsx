import React from 'react'
import { storiesOf } from '@storybook/react'

import TextTableHeader from '../../headers/TextTableHeader'
import Table from '../../Table'
import Factory from '../../../../../core/factory/Factory'

import StatusCell from './StatusCell'

const stories = storiesOf('Ячейки/Статус', module)

stories.addParameters({
    info: {
        propTables: [StatusCell],
        propTablesExclude: [Table, Factory],
    },
})

stories
    .add('Компонент', () => {
        const props = {
            headers: [
                {
                    id: 'StatusCell',
                    component: TextTableHeader,
                    label: 'StatusCell',
                },
            ],
            cells: [
                {
                    id: 'secondary',
                    component: StatusCell,
                    color: 'info',
                    fieldKey: 'test',
                },
            ],
            datasource: [{ test: 'текст статуса' }],
        }

        return (
            <Table
                headers={props.headers}
                cells={props.cells}
                datasource={props.datasource}
            />
        )
    })
    .add('С тултипом', () => {
        const props = {
            headers: [
                {
                    id: 'StatusCell',
                    component: TextTableHeader,
                    label: 'StatusCell',
                },
            ],
            cells: [
                {
                    id: 'secondary',
                    component: StatusCell,
                    color: 'info',
                    fieldKey: 'test',
                    tooltipFieldId: 'tooltip',
                },
            ],
            datasource: [{ test: 'текст статуса', tooltip: ['tooltip', 'body'] }],
        }

        return (
            <Table
                headers={props.headers}
                cells={props.cells}
                datasource={props.datasource}
            />
        )
    })
