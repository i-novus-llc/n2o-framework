import React, { createContext, useMemo, VFC } from 'react'

import { TableBodyProps } from '../types/props'
import { SelectionType } from '../enum'

import { SelectionCell } from './selection'
import { Rows } from './Rows'
import Table from './basic'

export const rowPropsContext = createContext(null)

export const TableBody: VFC<TableBodyProps> = (props) => {
    const { cells, selection, row, ...otherProps } = props
    const needSelectionComponent = selection === SelectionType.Radio || selection === SelectionType.Checkbox
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
    }, [cells, needSelectionComponent, selection])

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
