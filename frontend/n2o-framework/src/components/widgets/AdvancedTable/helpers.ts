import { type BodyCell, type HeaderCell } from '../../../ducks/table/Table'

import { type CellStateCache } from './types'

// объединение колонок
function mergeColumns<
    T extends HeaderCell | BodyCell,
    U extends CellStateCache = CellStateCache,
>(cell: T, cachedCell: U): T {
    const merged: T = { ...cell, ...cachedCell }

    if (cachedCell.children) {
        merged.children = mergeWithCache(cell.children || [], cachedCell.children)
    }

    return merged
}

// создание колонок
export function mergeWithCache<
    T extends HeaderCell | BodyCell,
    U extends CellStateCache = CellStateCache,
>(cells: T[] = [], cached: U[] = []): T[] {
    return cells.map((sourceColumn) => {
        const cachedColumn = cached.find(t => t.id === sourceColumn.id)

        if (!cachedColumn) { return sourceColumn }

        return mergeColumns(sourceColumn, cachedColumn)
    })
}

// облегченные колонки для хранения
export function prepareCellsToStore(cells: HeaderCell[] | BodyCell[]): CellStateCache[] {
    return cells.map(({ id, children, visibleState = true, format = null, filterField = null }) => {
        const prepared: CellStateCache = { id, visibleState, format, filterField }

        if (children) {
            prepared.children = prepareCellsToStore(children)
        }

        return prepared
    })
}
