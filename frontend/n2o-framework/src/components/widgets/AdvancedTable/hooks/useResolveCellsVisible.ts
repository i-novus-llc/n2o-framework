import { useMemo } from 'react'

import { getAllValuesByKey } from '../../../Table/utils'
import { type HeaderCell, type Cell } from '../../../Table/types/cell'

import { ColumnState } from './useColumnsState'

const filterVisibleNestedFields = (data: HeaderCell[], columnsState: ColumnState): HeaderCell[] => {
    const filterByVisible = (id: string) => {
        const cellState = columnsState.find(column => column.columnId === id)

        if (cellState) {
            if (!cellState.visible) {
                return false
            }

            return cellState.visibleState
        }

        return true
    }

    const resolveVisibility = (data: HeaderCell[]): HeaderCell[] => (
        data.reduce<HeaderCell[]>((filteredArray, item) => {
            const { children } = item

            if (item.visible === false) {
                return filteredArray
            }

            if (children?.length) {
                const filteredChildren = resolveVisibility(children)

                if (filteredChildren.length) {
                    filteredArray.push({ ...item, children: filteredChildren })
                }
            } else if (item.id !== undefined && filterByVisible(item.id)) {
                filteredArray.push(item)
            }

            return filteredArray
        }, [])
    )

    return resolveVisibility(data)
}

export const useResolveCellsVisible = <TCell extends Cell, THeaderCell extends HeaderCell>(
    cells: { body: TCell[], header: THeaderCell[] },
    columnsState: ColumnState,
) => (
        useMemo(() => {
            const header = filterVisibleNestedFields(cells.header, columnsState)
            const visibleHeaderCells = new Set(getAllValuesByKey(header, { keyToIterate: 'children', keyToExtract: 'id' }))
            const body = cells.body.filter(cellData => visibleHeaderCells.has(cellData.id))

            return { body, header }
        }, [cells, columnsState])
    )
