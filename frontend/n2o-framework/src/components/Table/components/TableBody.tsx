import React, { useMemo, VFC } from 'react'

import { TableBodyProps } from '../types/props'
import { Selection } from '../enum'
import { BodyCell } from '../../../ducks/table/Table'

import { SelectionCell } from './selection'
import { Rows } from './Rows'
import Table from './basic'

export const TableBody: VFC<TableBodyProps> = ({ cells, selection, row, ...otherProps }) => {
    const needSelectionComponent = selection === Selection.Radio || selection === Selection.Checkbox
    const resolvedCells = useMemo(() => {
        if (needSelectionComponent) {
            const selectionCellConfig = {
                component: SelectionCell,
                selection,
                id: 'selectionCell',
                fieldId: '',
            }

            return [selectionCellConfig, ...cells]
        }

        return cells
    }, [cells, needSelectionComponent, selection]) as BodyCell[]

    return (
        <Table.Body>
            <Rows
                {...otherProps}
                {...row}
                cells={resolvedCells}
                selection={selection}
            />
        </Table.Body>
    )
}

TableBody.displayName = 'TableBody'
