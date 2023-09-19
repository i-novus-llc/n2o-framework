import classNames from 'classnames'
import React, { VFC, SyntheticEvent } from 'react'

import { ExpandButtonProps } from '../types/props'
import { useTableActions } from '..'

export const ExpandButton: VFC<ExpandButtonProps> = ({ rowValue, isTreeExpanded }) => {
    const { toggleExpandRow } = useTableActions()

    const onClick = (event: SyntheticEvent) => {
        event.stopPropagation()
        toggleExpandRow(rowValue, !isTreeExpanded)
    }

    return (
        <span
            onClick={onClick}
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
