import { useCallback } from 'react'
import { useDispatch } from 'react-redux'

import { changeTableColumnParam, switchTableParam as switchTableParamAction } from '../../../../ducks/table/store'
import { HeaderCell, Table } from '../../../../ducks/table/Table'

export type ColumnState = HeaderCell[]
export type ChangeColumnParam = <
    Key extends keyof HeaderCell,
    Value extends HeaderCell[Key],
>(widgetId: string, id: string, paramKey: Key, value: Value, parentId?: string) => void

export type SwitchTableParam = <
    Key extends keyof Table,
>(widgetId: string, paramKey: Key) => void

export const useColumnsState = () => {
    const dispatch = useDispatch()
    const changeColumnParam = useCallback<ChangeColumnParam>((widgetId, columnId, paramKey, value, parentId) => {
        dispatch(changeTableColumnParam(widgetId, columnId, paramKey, value, parentId))
    }, [dispatch])

    const switchTableParam = useCallback<SwitchTableParam>((widgetId, paramKey) => {
        dispatch(switchTableParamAction(widgetId, paramKey))
    }, [dispatch])

    return [changeColumnParam, switchTableParam]
}
