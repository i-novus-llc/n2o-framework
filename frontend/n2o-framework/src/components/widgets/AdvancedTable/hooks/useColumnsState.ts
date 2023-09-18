/* eslint-disable @typescript-eslint/no-explicit-any */
import { useCallback, useLayoutEffect, useState } from 'react'
import { useStore } from 'react-redux'

import { HeaderCell } from '../../../Table/types/cell'
import { getAllValuesByKey } from '../../../Table/utils'
import { resolveConditions } from '../../../../sagas/conditions'

type ColumnItem = {
    columnId: string
    label: string
    visible: boolean
    disabled: boolean
    conditions: Record<string, any>
}
export type ColumnState = ColumnItem[]
export type ChangeColumnParam = <
    Key extends keyof ColumnItem,
    Value extends ColumnItem[Key]
>(id: string, paramKey: Key, value: Value) => void

export const useColumnsState = (columns: HeaderCell[]) => {
    const { getState } = useStore()
    const [columnsState, setColumnState] = useState<ColumnState>([])
    const changeColumnParam = useCallback<ChangeColumnParam>((id, paramKey, value) => {
        setColumnState(state => state.map((item) => {
            if (item.columnId === id) {
                return ({ ...item, [paramKey]: value })
            }

            return item
        }))
    }, [])

    useLayoutEffect(() => {
        const state = getState()
        const allHeaderCell = getAllValuesByKey(columns, { keyToIterate: 'children' })
        const columnState = allHeaderCell.reduce<ColumnState>((acc, cellData) => {
            if (cellData.id) {
                let resolvedVisibility = cellData.visible === undefined ? true : cellData.visible

                if (cellData?.conditions?.visible) {
                    resolvedVisibility = resolveConditions(state, cellData.conditions.visible).resolve
                }

                acc.push({
                    columnId: cellData.id,
                    label: cellData.label || '',
                    visible: resolvedVisibility,
                    disabled: Boolean(cellData.disabled),
                    conditions: cellData.conditions || {},
                })
            }

            return acc
        }, [])

        setColumnState(columnState)
    }, [columns, getState])

    return [columnsState, changeColumnParam]
}
