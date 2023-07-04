import React, { useCallback, VFC } from 'react'

import { RadioCellProps } from '../../types/props'
// @ts-ignore - отсутствует типизация
import { InputRadio } from '../../../controls/Radio/Input'
import { useTableActions } from '../../provider/TableActions'

export const RadioCell: VFC<RadioCellProps> = ({ rowValue, isSelectedRow }) => {
    const { selectSingleRow } = useTableActions()

    const onSelect = useCallback(() => {
        selectSingleRow(rowValue)
    }, [rowValue, selectSingleRow])

    return (
        <InputRadio
            className="n2o-advanced-table-row-radio"
            inline
            checked={isSelectedRow}
            value={rowValue}
            onChange={onSelect}
        />
    )
}
