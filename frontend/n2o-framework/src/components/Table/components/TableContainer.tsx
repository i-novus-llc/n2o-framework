import React, { useMemo, useRef, VFC } from 'react'
import classNames from 'classnames'
import { useSticky } from '@i-novus/n2o-components/lib/layouts/Scroll/useSticky'

import { TableWidgetContainerProps } from '../types/props'
import { TableActionsProvider } from '../provider/TableActions'
import { Selection } from '../enum'
import { getAllValuesByKey } from '../utils'
import { TableRefProps } from '../provider/TableRefProps'
import { EMPTY_ARRAY, EMPTY_OBJECT, NOOP_FUNCTION } from '../../../utils/emptyTypes'

import { TableHeader } from './TableHeader'
import { TableBody } from './TableBody'
import { ScrollbarRow } from './ScrollbarRow'
import { useFixedCells } from './useFixedCells'

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
    scrollPosition = 'bottom',
    stickyHeader = false,
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
    const headerRef = useRef(null)
    const hasSelection = rowSelection === Selection.Radio || rowSelection === Selection.Checkbox
    const { cells: fixedCells, colgroup, hasFixedLeft } = useFixedCells({ cells, hasSelection })
    const colSpan = fixedCells.body.length + (hasSelection ? 1 : 0)

    useSticky(refContainerElem, headerRef)

    return (
        <TableRefProps
            value={{
                ...props,
                tableConfig,
                sorting,
                cells: fixedCells,
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
                    className={
                        classNames('table-container', {
                            'sticky-header': stickyHeader,
                        })
                    }
                    style={{ width, height }}
                >
                    <table
                        className={classNames(className, { 'hidden-scrollbar': scrollPosition === 'top' })}
                        id={id}
                        cellPadding="0"
                        cellSpacing="0"
                    >
                        {colgroup}
                        <TableHeader
                            ref={stickyHeader ? headerRef : undefined}
                            sorting={sorting}
                            selection={rowSelection}
                            selectionFixed={hasFixedLeft}
                            row={header.row}
                            cells={fixedCells.header}
                            areAllRowsSelected={areAllRowsSelected}
                            validateFilterField={validateFilterField}
                            filterErrors={filterErrors}
                            scrollbar={scrollPosition === 'top'
                                ? <ScrollbarRow targetRef={refContainerElem} cellType='th' colSpan={colSpan} />
                                : null
                            }
                        />

                        {errorComponent ? (
                            <td colSpan={fixedCells.body.length}>{errorComponent}</td>
                        ) : (
                            <TableBody
                                focusedRowValue={focusedRowValue}
                                treeDataKey="children"
                                selectedKey="id"
                                selectedRows={selectedRows}
                                selection={rowSelection}
                                selectionFixed={hasFixedLeft}
                                expandedRows={expandedRows}
                                row={body.row}
                                cells={fixedCells.body}
                                rowRenderFieldKey="id"
                                data={data}
                                isTextWrap={isTextWrap}
                            />
                        )}

                        {(!errorComponent && EmptyContent && data.length === 0) && (
                            <td className="empty_content" colSpan={fixedCells.body.length}>{EmptyContent}</td>
                        )}
                    </table>
                </div>
            </TableActionsProvider>
        </TableRefProps>
    )
}

TableContainer.displayName = 'TableContainer'
