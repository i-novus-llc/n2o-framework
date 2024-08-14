import { useCallback, useLayoutEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'

import { HeaderCell } from '../../../Table/types/cell'
import { getAllValuesByKey } from '../../../Table/utils'
import { resolveConditions } from '../../../../sagas/conditions'
import { registerTableColumn, changeTableColumnParam, switchTableParam } from '../../../../ducks/table/store'
import { Condition } from '../../../../ducks/toolbar/Toolbar'
import { getTableColumns } from '../../../../ducks/table/selectors'
import { State } from '../../../../ducks/State'

type ColumnItem = {
    columnId: string
    label: string
    visible: boolean
    visibleState?: boolean
    disabled: boolean
    conditions: { visible?: Condition[] }
}
export type ColumnState = ColumnItem[]
export type ChangeColumnParam = <
    Key extends keyof ColumnItem,
    Value extends ColumnItem[Key],
>(widgetId: string, id: string, paramKey: Key, value: Value) => void

export const useColumnsState = (columns: HeaderCell[], widgetId: string, state: State) => {
    const dispatch = useDispatch()
    const changeColumnParam = useCallback<ChangeColumnParam>((widgetId, columnId, paramKey, value) => {
        dispatch(changeTableColumnParam(widgetId, columnId, paramKey, value))
    }, [])

    const switchTableParameter = useCallback((widgetId, paramKey) => {
        dispatch(switchTableParam(widgetId, paramKey))
    }, [])

    useLayoutEffect(() => {
        const allHeaderCell = getAllValuesByKey(columns, { keyToIterate: 'children' })

        allHeaderCell.forEach((cellData) => {
            const { id: columnId } = cellData

            if (columnId) {
                let resolvedVisibility = cellData.visible === undefined ? true : cellData.visible

                if (cellData?.conditions?.visible) {
                    resolvedVisibility = resolveConditions(state, cellData.conditions.visible).resolve
                }

                const { disabled, icon, label = '', conditions = {} } = cellData

                dispatch(registerTableColumn(
                    {
                        widgetId,
                        columnId,
                        label,
                        visible: resolvedVisibility,
                        disabled: Boolean(disabled),
                        conditions,
                        icon,
                    },
                ))
            }
        })
    }, [columns.length])

    const reduxColumns = useSelector(getTableColumns(widgetId))
    const columnsState = Object.values(reduxColumns)

    return [columnsState, changeColumnParam, switchTableParameter]
}
