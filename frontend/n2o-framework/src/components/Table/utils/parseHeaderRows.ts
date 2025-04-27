import cloneDeep from 'lodash/cloneDeep'

import { HeaderCell } from '../../../ducks/table/Table'

/**
 * Форматировние списка ячеек заголовков, что бы правильно проставить параметр colSpan и rowSpan
 */
export const parseHeaderRows = (rootColumns: readonly HeaderCell[]): HeaderCell[][] => {
    const rows: HeaderCell[][] = []

    function fillRowCells(
        columns: readonly HeaderCell[],
        colIndex: number,
        rowIndex = 0,
    ): number[] {
        rows[rowIndex] = rows[rowIndex] || []

        let currentColIndex = colIndex

        return columns.filter(Boolean).map(({ children, moveMode, ...column }) => {
            const cell = cloneDeep(column)

            let colSpan = 1

            const subColumns = children

            if (!moveMode && subColumns && subColumns.length > 0) {
                colSpan = fillRowCells(subColumns, currentColIndex, rowIndex + 1).reduce(
                    (total, count) => total + count,
                    0,
                )
                cell.hasSubColumns = true
            }

            if (column.colSpan !== undefined) {
                colSpan = column.colSpan
            }

            if (column.rowSpan !== undefined) {
                cell.rowSpan = column.rowSpan
            }

            cell.colSpan = colSpan
            rows[rowIndex].push(moveMode ? { ...cell, children, moveMode } : cell)

            currentColIndex += colSpan

            return colSpan
        })
    }

    fillRowCells(rootColumns, 0)

    const rowCount = rows.length

    for (let rowIndex = 0; rowIndex < rowCount; rowIndex += 1) {
        rows[rowIndex].forEach((cell) => {
            if (!('rowSpan' in cell) && !cell.hasSubColumns) {
                cell.rowSpan = rowCount - rowIndex
            }

            delete cell.hasSubColumns
        })
    }

    return rows
}
