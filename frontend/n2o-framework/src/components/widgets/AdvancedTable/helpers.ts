import { type BodyCell, type HeaderCell } from '../../../ducks/table/Table'

import { type SavedColumn } from './types'

interface BaseColumn {
    id: string;
    children?: BaseColumn[];
}

// объединение колонок
function mergeColumns<T extends BaseColumn>(source: T, target: T): T {
    const merged: T = { ...source, ...target }

    if (target.children) {
        const sourceChildren = source.children || []

        merged.children = mergeChildren(target.children, sourceChildren)
    } else {
        merged.children = undefined
    }

    return merged
}

// объединение дочерних элементов
function mergeChildren<T extends BaseColumn>(targetChildren: T[], sourceChildren: T[]): T[] {
    return targetChildren.map((targetChild) => {
        const sourceChild = sourceChildren.find(s => s.id === targetChild.id)

        if (!sourceChild) {
            return copyColumn(targetChild)
        }

        return mergeColumns(sourceChild, targetChild)
    })
}

// копирование колонки
function copyColumn<T extends BaseColumn>(column: T): T {
    const copied: T = { ...column }

    if (column.children) {
        copied.children = column.children.map(child => copyColumn(child))
    } else {
        copied.children = undefined
    }

    return copied
}

// создание колонок
export function createColumns<P extends HeaderCell | BodyCell>(source: P[], target: P[]): P[] {
    return target.map((targetColumn) => {
        const sourceColumn = source.find(s => s.id === targetColumn.id)

        if (!sourceColumn) {
            return copyColumn(targetColumn)
        }

        return mergeColumns(sourceColumn, targetColumn)
    })
}

// облегченные колонки для хранения
export function prepareCellsToStore(cells: HeaderCell[] | BodyCell[]): SavedColumn[] {
    return cells.map(({ id, children, visibleState = true, format = null, filterField = null }) => {
        let preparedChildren: SavedColumn['children']

        if (children) {
            preparedChildren = prepareCellsToStore(children)
        }

        const prepared: SavedColumn = { id, visibleState, format, filterField }

        if (preparedChildren) {
            prepared.children = preparedChildren
        }

        return prepared
    })
}
