import { useLayoutEffect } from 'react'
import { useDispatch } from 'react-redux'

// @ts-ignore - нет типизации
import { registerColumn } from '../../../../ducks/columns/store'
import { HeaderCell } from '../../../Table/models/cell'

export const useRegisterColumns = (id: string, columns: HeaderCell[]) => {
    const dispatch = useDispatch()

    /* TODO:
            В данный момент воспроизведено поведение из состояния до рефакторинга.
            Регистрируются только те ячейки, которые не обернуты в multi-column
            Необходимо исправить это поведение и починить параметр visible у мультиколонок
        */
    useLayoutEffect(() => {
        columns.forEach((cellData) => {
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
