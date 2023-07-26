import { useLayoutEffect } from 'react'
import { useDispatch } from 'react-redux'

// @ts-ignore - нет типизации
import { registerColumn } from '../../../../ducks/columns/store'
import { HeaderCell } from '../../../Table/types/cell'
import { getAllValuesByKey } from '../../../Table/utils'

export const useRegisterColumns = (id: string, columns: HeaderCell[]) => {
    const dispatch = useDispatch()

    useLayoutEffect(() => {
        const allHeaderCell = getAllValuesByKey(columns, { keyToIterate: 'children' })

        allHeaderCell.forEach((cellData) => {
            if (cellData.id) {
                dispatch(registerColumn(
                    id,
                    cellData.id,
                    cellData.label || '',
                    cellData.visible === undefined ? true : cellData.visible,
                    Boolean(cellData.disabled),
                    cellData.conditions || {},
                ))
            }
        })
    }, [columns, dispatch, id])
}
