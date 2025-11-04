import React, { useMemo, VFC } from 'react'

import { TableBodyProps } from '../types/props'
import { Selection } from '../enum'
import { BodyCell } from '../../../ducks/table/Table'

import { SelectionCell } from './selection'
import { Rows } from './Rows'

export const TableBody: VFC<TableBodyProps> = ({ cells, selection, row, selectionFixed, ...otherProps }) => {
    const needSelectionComponent = selection === Selection.Radio || selection === Selection.Checkbox
    const resolvedCells = useMemo(() => {
        if (needSelectionComponent) {
            const selectionCellConfig = {
                component: SelectionCell,
                selection,
                id: 'selectionCell',
                fieldId: '',
                elementAttributes: {
                    className: selectionFixed ? 'sticky-cell sticky-left' : '',
                },
            }

            return [selectionCellConfig, ...cells]
        }

        return cells
    }, [cells, needSelectionComponent, selection, selectionFixed]) as BodyCell[]

    return (
        <tbody>
            <Rows
                {...otherProps}
                {...row}
                cells={resolvedCells}
                selection={selection}
            />
        </tbody>
    )
}

TableBody.displayName = 'TableBody'
