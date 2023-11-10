import { useCallback, useLayoutEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'

import { HeaderCell } from '../../../Table/types/cell'
import { getAllValuesByKey } from '../../../Table/utils'
import { resolveConditions, Condition } from '../../../../sagas/conditions'
import { registerTableColumn, changeTableColumnParam } from '../../../../ducks/table/store'
import { getTableColumns } from '../../../../ducks/table/selectors'
import { State } from '../../../../ducks/State'

type ColumnItem = {
    columnId: string
    label: string
    visible: boolean
    disabled: boolean
    conditions: { visible?: Condition[] }
}
export type ColumnState = ColumnItem[]
export type ChangeColumnParam = <
    Key extends keyof ColumnItem,
    Value extends ColumnItem[Key]
>(widgetId: string, id: string, paramKey: Key, value: Value) => void

export const useColumnsState = (columns: HeaderCell[], widgetId: string, state: State) => {
    const dispatch = useDispatch()

    const changeColumnParam = useCallback<ChangeColumnParam>((widgetId, columnId, paramKey, value) => {
        dispatch(changeTableColumnParam(widgetId, columnId, paramKey, value))
    }, [])

    useLayoutEffect(() => {
        const allHeaderCell = getAllValuesByKey(columns, { keyToIterate: 'children' })

        allHeaderCell.forEach((cellData) => {
            if (cellData.id) {
                let resolvedVisibility = cellData.visible === undefined ? true : cellData.visible

                if (cellData?.conditions?.visible) {
                    resolvedVisibility = resolveConditions(state, cellData.conditions.visible).resolve
                }

                dispatch(registerTableColumn(
                    widgetId,
                    cellData.id,
                    cellData.label || '',
                    resolvedVisibility,
                    Boolean(cellData.disabled),
                    cellData.conditions || {},
                ))
            }
        })
    }, [columns])

    const reduxColumns = useSelector(getTableColumns(widgetId))
    const columnsState = Object.values(reduxColumns)

    return [columnsState, changeColumnParam]
}
