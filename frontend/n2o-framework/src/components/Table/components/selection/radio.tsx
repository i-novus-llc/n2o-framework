import React, { useCallback, VFC } from 'react'
import { InputRadio } from '@i-novus/n2o-components/lib/inputs/Radio'

import { RadioCellProps } from '../../types/props'
import { useTableActions } from '../../provider/TableActions'

export const RadioCell: VFC<RadioCellProps> = ({ rowValue, isSelectedRow, model }) => {
    const { selectSingleRow, setFocusOnRow } = useTableActions()

    const onSelect = useCallback(() => {
        selectSingleRow(rowValue)
        setFocusOnRow(rowValue, model)
    }, [model, rowValue, selectSingleRow, setFocusOnRow])

    return (
        <div onClick={event => event.stopPropagation()}>
            <InputRadio
                className="n2o-advanced-table-row-radio"
                checked={isSelectedRow}
                value={rowValue}
                onChange={onSelect}
            />
        </div>
    )
}
