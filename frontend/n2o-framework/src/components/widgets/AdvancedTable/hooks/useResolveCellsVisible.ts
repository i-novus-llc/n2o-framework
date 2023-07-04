import { useMemo } from 'react'

type TCell = {
    [x: string]: unknown
    id: string
    visible?: boolean
}

type TColumnState = Record<string, {
    [x: string]: unknown
    visible?: boolean
}>

// eslint-disable-next-line max-len
export const useResolveCellsVisible = <Cell extends TCell, HeaderCell extends TCell>(cells: { body: Cell[], header: HeaderCell[] }, columnsState: TColumnState) => (
    useMemo(() => {
        const filterByVisible = (id: string) => {
            const cellState = columnsState[id]

            if (cellState) {
                return cellState?.visible
            }

            return true
        }
        const header = cells.header.filter(cellData => filterByVisible(cellData.id))
        const body = cells.body.filter(cellData => filterByVisible(cellData.id))

        return { body, header }
    }, [cells, columnsState])
)
