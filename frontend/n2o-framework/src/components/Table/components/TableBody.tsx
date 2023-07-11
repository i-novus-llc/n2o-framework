import React, { createContext, useCallback, useMemo, VFC } from 'react'

import { TableBodyProps } from '../types/props'
import { SelectionType } from '../enum'
import { useTableActions } from '../provider/TableActions'

import { SelectionCell } from './selection'
import { Rows } from './Rows'
import Table from './basic'

export const rowPropsContext = createContext(null)

export const TableBody: VFC<TableBodyProps> = (props) => {
    const { row, cells, selection, hasSecurityAccess, ...otherProps } = props
    const { click, ...otherRowProps } = row || {}

    const { onDispatchRowAction, setFocusOnRow } = useTableActions()

    const needSelectionComponent = selection === SelectionType.Radio || selection === SelectionType.Checkbox
    const hasSelection = selection !== SelectionType.None
    const isAccessRowActionClick = click && hasSecurityAccess
    const hasOnClick = Boolean(isAccessRowActionClick || hasSelection)
    const rowOnClickAction = useCallback((data) => {
        if (hasSelection) {
            setFocusOnRow(data.id, data)
        }

        if (isAccessRowActionClick) {
            onDispatchRowAction(click, data)
        }
    }, [click, hasSelection, onDispatchRowAction, setFocusOnRow, isAccessRowActionClick])

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
                {...otherRowProps}
                {...otherProps}
                onClick={hasOnClick ? rowOnClickAction : undefined}
                cells={resolvedCells}
                selection={selection}
            />
        </Table.Body>
    )
}

TableBody.displayName = 'TableBody'
