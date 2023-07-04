import classNames from 'classnames'
import React, { VFC } from 'react'

import { ExpandButtonProps } from '../models/props'
import { useTableActions } from '../provider/TableActions'

export const ExpandButton: VFC<ExpandButtonProps> = ({ rowValue, isTreeExpanded }) => {
    const { toggleExpandRow } = useTableActions()

    return (
        <span
            onClick={() => toggleExpandRow(rowValue, !isTreeExpanded)}
            className="n2o-advanced-table-expand"
        >
            <i
                className={classNames('fa', 'n2o-advanced-table-expand-icon', {
                    'n2o-advanced-table-expand-icon-expanded': isTreeExpanded,
                    'fa-angle-right': !isTreeExpanded,
                    'fa-angle-down': isTreeExpanded,
                })}
            />
        </span>
    )
}
