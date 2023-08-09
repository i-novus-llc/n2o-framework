import { useMemo } from 'react'

import { getAllValuesByKey } from '../../../Table/utils'

type Cell = {
    [x: string]: unknown
    id: string
    visible?: boolean
}

type HeaderCell = {
    [x: string]: unknown
    id?: string
    visible?: boolean
    children?: HeaderCell[]
}

type ColumnState = Record<string, {
    [x: string]: unknown
    visible?: boolean
}>

const filterVisibleNestedFields = (data: HeaderCell[], columnsState: ColumnState): HeaderCell[] => {
    const filterByVisible = (id: string) => {
        const cellState = columnsState[id]

        if (cellState) {
            return cellState?.visible
        }

        return true
    }

    const resolveVisibility = (data: HeaderCell[]): HeaderCell[] => (
        data.reduce<HeaderCell[]>((filteredArray, item) => {
            const { children } = item
            const hasChildren = children?.length

            if (item.visible === false) {
                return filteredArray
            }

            if (hasChildren) {
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

// eslint-disable-next-line max-len
export const useResolveCellsVisible = <TCell extends Cell, THeaderCell extends HeaderCell>(cells: { body: TCell[], header: THeaderCell[] }, columnsState: ColumnState) => (
    useMemo(() => {
        const header = filterVisibleNestedFields(cells.header, columnsState)
        const visibleHeaderCells = new Set(getAllValuesByKey(header, { keyToIterate: 'children', keyToExtract: 'id' }))
        const body = cells.body.filter(cellData => visibleHeaderCells.has(cellData.id))

        return { body, header }
    }, [cells, columnsState])
)
