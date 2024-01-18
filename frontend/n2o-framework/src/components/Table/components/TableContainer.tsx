import React, { useMemo, VFC } from 'react'

import { TableWidgetContainerProps } from '../types/props'
import { TableActionsProvider } from '../provider/TableActions'
import { Selection } from '../enum'
import { getAllValuesByKey } from '../utils'
import { TableRefProps } from '../provider/TableRefProps'

import { TableHeader } from './TableHeader'
import { TableBody } from './TableBody'
import Table from './basic'

export const TableContainer: VFC<TableWidgetContainerProps<HTMLDivElement>> = (props) => {
    const {
        tableConfig,
        data,
        sorting,
        cells,
        hasSecurityAccess,
        isTextWrap,
        focusedRowValue,
        expandedRows,
        selectedRows,
        actionListener,
        errorComponent,
        EmptyContent,
        refContainerElem,
    } = props
    const { width, height, rowSelection, body, header } = tableConfig
    const areAllRowsSelected = useMemo(() => {
        if (rowSelection === Selection.Checkbox && data.length) {
            const allRowsId = getAllValuesByKey(data, { keyToIterate: 'children', keyToExtract: 'id' })

            return allRowsId.every(id => selectedRows.includes(id))
        }

        return false
    }, [rowSelection, data, selectedRows])

    return (
        <TableRefProps value={props}>
            <TableActionsProvider actionListener={actionListener}>
                <div
                    ref={refContainerElem}
                    data-text-wrap={isTextWrap}
                    className="table-container"
                    style={{ width, height }}
                >
                    <Table className="table">
                        <TableHeader
                            sorting={sorting}
                            selection={rowSelection}
                            row={header.row}
                            cells={cells.header}
                            areAllRowsSelected={areAllRowsSelected}
                        />

                        {errorComponent ? (
                            <Table.Cell colSpan={cells.body.length}>{errorComponent}</Table.Cell>
                        ) : (
                            <TableBody
                                hasSecurityAccess={hasSecurityAccess}
                                focusedRowValue={focusedRowValue}
                                treeDataKey="children"
                                selectedKey="id"
                                selectedRows={selectedRows}
                                selection={rowSelection}
                                expandedRows={expandedRows}
                                row={body.row}
                                cells={cells.body}
                                rowRenderFieldKey="id"
                                data={data}
                            />
                        )}

                        {!errorComponent && EmptyContent && data.length === 0 ? (
                            <Table.Cell colSpan={cells.body.length}>{EmptyContent}</Table.Cell>
                        ) : null}
                    </Table>
                </div>
            </TableActionsProvider>
        </TableRefProps>
    )
}

TableContainer.defaultProps = {
    data: [],
    hasSecurityAccess: true,
    focusedRowValue: null,
    expandedRows: [],
    selectedRows: [],
    actionListener: () => {},
    filterErrors: {},
}

TableContainer.displayName = 'TableContainer'
