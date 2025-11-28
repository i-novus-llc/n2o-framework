import { useCallback } from 'react'
import { useDispatch } from 'react-redux'

import { changeTableColumnParam } from '../../../../ducks/table/store'
import { HeaderCell } from '../../../../ducks/table/Table'

export type ColumnState = HeaderCell[]
export type ChangeColumnParam = <
    Key extends keyof HeaderCell,
    Value extends HeaderCell[Key],
>(widgetId: string, id: string, paramKey: Key, value: Value, parentId?: string) => void

export const useColumnsState = () => {
    const dispatch = useDispatch()
    const changeColumnParam = useCallback<ChangeColumnParam>((widgetId, columnId, paramKey, value, parentId) => {
        dispatch(changeTableColumnParam(widgetId, columnId, paramKey, value, parentId))
    }, [dispatch])

    return [changeColumnParam]
}
