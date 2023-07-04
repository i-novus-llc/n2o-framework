import React, { useCallback, useRef, VFC } from 'react'

import { CheckboxCellProps } from '../../models/props'
import { excludeItems, getAllValuesByKey } from '../../utils'
// @ts-ignore - отсутствует типизация
import Checkbox from '../../../controls/Checkbox/CheckboxN2O'
import { useTableActions } from '../../provider/TableActions'
import { useTableRefProps } from '../../provider/TableRefProps'

export const CheckboxCell: VFC<CheckboxCellProps> = ({ rowValue, isSelectedRow, model }) => {
    const refProps = useTableRefProps()
    const { selectRows, deselectRows } = useTableActions()
    const modelRef = useRef(model)

    modelRef.current = model

    const onSelect = useCallback((event) => {
        const model = modelRef.current
        let rowValueList = [rowValue]

        if (model.children) {
            const allRowsId = getAllValuesByKey([model], { keyToExtract: 'id', keyToIterate: 'children' })

            if (event.nativeEvent.target.checked) {
                rowValueList = excludeItems(allRowsId, refProps.current.selectedRows)
            } else {
                rowValueList = allRowsId
            }
        }

        if (event.nativeEvent.target.checked) {
            selectRows(rowValueList)
        } else {
            deselectRows(rowValueList)
        }
    }, [rowValue, refProps, selectRows, deselectRows])

    return (
        <div className="checkbox-cell">
            <Checkbox
                inline
                checked={isSelectedRow}
                onChange={onSelect}
            />
        </div>
    )
}
