import React from 'react'
import { storiesOf } from '@storybook/react'

import Table from '../../Table'
import TextTableHeader from '../../headers/TextTableHeader'
import Factory from '../../../../../core/factory/Factory'

import IconJson from './IconCell.meta.json'
import IconCell from './IconCell'
import { iconCellTypes, textPlaceTypes } from './cellTypes'

const stories = storiesOf('Ячейки/Иконки', module)

stories.addParameters({
    info: {
        propTables: [IconCell],
        propTablesExclude: [Table, Factory],
    },
})

stories.add('Метаданные', () => {
    const props = {
        id: IconJson.id,
        type: IconJson.type,
        textPlace: IconJson.textPlace,
        icon: IconJson.icon,
        hint: IconJson.hint,
        model: {
            name: 'Иван',
            age: '12',
            tooltipFieldId: ['tooltip', 'body'],
        },
    }

    const tableProps = {
        headers: [
            {
                id: 'id',
                component: TextTableHeader,
                label: 'Ячейка с иконкой',
            },
        ],
        cells: [
            {
                component: IconCell,
                ...props,
            },
        ],
        datasource: [
            {
                id: 'id',
                name: 'alesha',
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
