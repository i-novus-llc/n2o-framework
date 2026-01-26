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
    const cellsMap = new Map<string, T>()

    cells.forEach(cell => cellsMap.set(cell.id, cell))

    const result: T[] = []
    const processedIds = new Set<string>()

    // @INFO merge с учетом порядка в cached
    for (const cachedColumn of cached) {
        const sourceColumn = cellsMap.get(cachedColumn.id)

        if (sourceColumn) {
            result.push(mergeColumns(sourceColumn, cachedColumn))
            processedIds.add(cachedColumn.id)
        }
    }

    // @INFO добавляем не закешированные колонки
    for (const sourceColumn of cells) {
        if (!processedIds.has(sourceColumn.id)) {
            result.push(sourceColumn)
        }
    }

    return result
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
