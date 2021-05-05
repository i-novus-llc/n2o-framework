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

stories.add('Цвета', () => {
    const tableProps = {
        headers: [
            {
                id: 'header',
                component: TextTableHeader,
                label: 'Стандратный',
            },
            {
                id: 'header',
                component: TextTableHeader,
                label: 'Зелёный',
            },
            {
                id: 'header',
                component: TextTableHeader,
                label: 'Голубой',
            },
            {
                id: 'header',
                component: TextTableHeader,
                label: 'Оранжевый',
            },
            {
                id: 'header',
                component: TextTableHeader,
                label: 'Красный',
            },
        ],
        cells: [
            {
                component: ProgressBarCell,
                id: 'now',
                model: {
                    now: 55,
                },
                color: 'default',
            },
            {
                component: ProgressBarCell,
                id: 'now',
                model: {
                    now: 55,
                },
                color: progressBarStyles.SUCCESS,
            },
            {
                component: ProgressBarCell,
                id: 'now',
                model: {
                    now: 55,
                },
                color: progressBarStyles.INFO,
            },
            {
                component: ProgressBarCell,
                id: 'now',
                model: {
                    now: 55,
                },
                color: progressBarStyles.WARNING,
            },
            {
                component: ProgressBarCell,
                id: 'now',
                model: {
                    now: 55,
                },
                color: progressBarStyles.DANGER,
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
