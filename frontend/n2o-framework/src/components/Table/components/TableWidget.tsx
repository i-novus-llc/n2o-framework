import React, { useMemo, useRef, VFC } from 'react'
import { createContext, useContext } from 'use-context-selector'

import { getAllValuesByKey } from '../utils'
import { TableProps } from '../models/props'
import { SelectionType } from '../enum'

import Table from './basic'
import '../style.scss'
import { TableHeader } from './TableHeader'
import { TableBody } from './TableBody'

const tablePropsContext = createContext<React.MutableRefObject<TableProps> | null>(null)

export const useTableProps = () => {
    const context = useContext(tablePropsContext)

    if (!context) {
        throw Error('useTableProps must be used in tablePropsContext.Provider')
    }

    return context
}

export const TableWidget: VFC<TableProps> = ((props) => {
    const {
        bodyRow,
        headerRow,
        expandedRows,
        selectedRows,
        data,
        selection,
        rowRenderFieldKey,
        selectedKey,
        treeDataKey,
        sorting,
        headerCell,
        bodyCell,
        focusedRowValue,
        hasSecurityAccess,
    } = props
    const refProps = useRef(props)
    const areAllRowsSelected = useMemo(() => {
        if (selection === SelectionType.Checkbox && data.length) {
            const allRowsId = getAllValuesByKey(data, { keyToIterate: 'children', keyToExtract: 'id' })

            return allRowsId.every(id => selectedRows.includes(id))
        }

        return false
    }, [selectedRows, selection, data])

    refProps.current = props

    return (
        <tablePropsContext.Provider value={refProps}>
            <Table className="table">
                <TableHeader
                    sorting={sorting}
                    selection={selection}
                    row={headerRow}
                    cells={headerCell}
                    areAllRowsSelected={areAllRowsSelected}
                />

                <TableBody
                    hasSecurityAccess={hasSecurityAccess}
                    focusedRowValue={focusedRowValue}
                    treeDataKey={treeDataKey}
                    selectedKey={selectedKey}
                    selectedRows={selectedRows}
                    selection={selection}
                    expandedRows={expandedRows}
                    row={bodyRow}
                    cells={bodyCell}
                    rowRenderFieldKey={rowRenderFieldKey}
                    data={data}
                />
            </Table>
        </tablePropsContext.Provider>
    )
})

TableWidget.defaultProps = {
    data: [],
}
TableWidget.displayName = 'TableWidget'
