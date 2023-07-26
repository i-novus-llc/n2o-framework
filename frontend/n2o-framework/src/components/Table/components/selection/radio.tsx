import React, { useCallback, VFC } from 'react'

import { InputRadio } from '@i-novus/n2o-components/lib/inputs/RadioGroup/default/Input'

import { RadioCellProps } from '../../types/props'
import { useTableActions } from '../../provider/TableActions'

export const RadioCell: VFC<RadioCellProps> = ({ rowValue, isSelectedRow }) => {
    const { selectSingleRow } = useTableActions()

    const onSelect = useCallback(() => {
        selectSingleRow(rowValue)
    }, [rowValue, selectSingleRow])

    return (
        <InputRadio
            className="n2o-advanced-table-row-radio"
            checked={isSelectedRow}
            value={rowValue}
            onChange={onSelect}
        />
    )
}
