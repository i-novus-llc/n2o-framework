import React from 'react'
import { storiesOf } from '@storybook/react'

import Table from '../../Table'
import TextTableHeader from '../../headers/TextTableHeader'
import Factory from '../../../../../core/factory/Factory'

import progressBarStyles from './progressBarStyles'
import ProgressBarCell from './ProgressBarCell'

const stories = storiesOf('Ячейки/Индикатор', module)

stories.addParameters({
    info: {
        propTables: [ProgressBarCell],
        propTablesExclude: [Table, Factory],
    },
})

stories.add('Анимация', () => {
    const tableProps = {
        headers: [
            {
                id: 'header',
                component: TextTableHeader,
                label: 'С анимацией',
            },
            {
                id: 'header',
                component: TextTableHeader,
                label: 'Без анимацией',
            },
        ],
        cells: [
            {
                component: ProgressBarCell,
                id: 'now',
                model: {
                    now: 55,
                },
                color: progressBarStyles.DEFAULT,
                animated: true,
            },
            {
                component: ProgressBarCell,
                id: 'now',
                model: {
                    now: 55,
                },
                color: progressBarStyles.DEFAULT,
                animated: false,
            },
        ],
        datasource: [
            {
                id: 'now',
                now: 55,
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
