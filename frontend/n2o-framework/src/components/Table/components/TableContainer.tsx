import React, { useMemo, VFC } from 'react'

import { TableWidgetContainerProps } from '../types/props'
import { TableActionsProvider } from '../provider/TableActions'
import { Selection } from '../enum'
import { getAllValuesByKey } from '../utils'
import { TableRefProps } from '../provider/TableRefProps'
import { EMPTY_ARRAY, EMPTY_OBJECT, NOOP_FUNCTION } from '../../../utils/emptyTypes'

import { TableHeader } from './TableHeader'
import { TableBody } from './TableBody'
import Table from './basic'

export const TableContainer: VFC<TableWidgetContainerProps<HTMLDivElement>> = ({
    tableConfig,
    sorting,
    cells,
    isTextWrap,
    errorComponent,
    EmptyContent,
    refContainerElem,
    validateFilterField,
    id,
    className,
    actionListener = NOOP_FUNCTION,
    focusedRowValue = null,
    data = EMPTY_ARRAY,
    expandedRows = EMPTY_ARRAY,
    selectedRows = EMPTY_ARRAY,
    filterErrors = EMPTY_OBJECT,
    ...props
}) => {
    const { width, height, rowSelection, body, header } = tableConfig
    const areAllRowsSelected = useMemo(() => {
        if (rowSelection === Selection.Checkbox && data.length) {
            const allRowsId = getAllValuesByKey(data, { keyToIterate: 'children', keyToExtract: 'id' })

            return allRowsId.every(id => selectedRows.includes(id))
        }

        return false
    }, [rowSelection, data, selectedRows])

    return (
        <TableRefProps
            value={{
                ...props,
                tableConfig,
                sorting,
                cells,
                isTextWrap,
                errorComponent,
                EmptyContent,
                refContainerElem,
                validateFilterField,
                id,
                className,
                actionListener,
                focusedRowValue,
                data,
                expandedRows,
                selectedRows,
                filterErrors,
            }}
        >
            <TableActionsProvider actionListener={actionListener}>
                <div
                    ref={refContainerElem}
                    data-text-wrap={isTextWrap}
                    className="table-container"
                    style={{ width, height }}
                >
                    <Table className={className} id={id}>
                        <TableHeader
                            sorting={sorting}
                            selection={rowSelection}
                            row={header.row}
                            cells={cells.header}
                            areAllRowsSelected={areAllRowsSelected}
                            validateFilterField={validateFilterField}
                            filterErrors={filterErrors}
                        />

                        {errorComponent ? (
                            <Table.Cell colSpan={cells.body.length}>{errorComponent}</Table.Cell>
                        ) : (
                            <TableBody
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

                        {(!errorComponent && EmptyContent && data.length === 0) && (
                            <Table.Cell className="empty_content" colSpan={cells.body.length}>{EmptyContent}</Table.Cell>
                        )}
                    </Table>
                </div>
            </TableActionsProvider>
        </TableRefProps>
    )
}

TableContainer.displayName = 'TableContainer'
