import React, { MouseEvent } from 'react'
import { DropdownItem as Component } from 'reactstrap'
import classNames from 'classnames'

import { HeaderCell } from '../../../ducks/table/Table'
import { NOOP_FUNCTION } from '../../../utils/emptyTypes'

import { getLabel } from './helpers'

export interface DropdownItemProps {
    item: HeaderCell
    onClick(columnId: HeaderCell['columnId'], visibleState: HeaderCell['visibleState'], parentId?: HeaderCell['parentId']): void
    lockedColumns: string[]
    onMouseDown?(event: MouseEvent): void
    elementAttributes?: Record<string, unknown>
}

export const DropdownItem = ({ item, onClick, lockedColumns, onMouseDown = NOOP_FUNCTION, elementAttributes }: DropdownItemProps) => {
    const { columnId, label, parentId, icon, visibleState } = item

    const handleClick = (e: MouseEvent) => {
        e.stopPropagation()
        e.preventDefault()
        onClick(columnId, visibleState, parentId)
    }

    const currentLabel = getLabel(columnId, label, icon)

    return (
        <Component
            key={columnId}
            toggle={false}
            onClick={handleClick}
            disabled={lockedColumns.length ? lockedColumns.includes(columnId) : false}
            onMouseDown={onMouseDown}
            {...elementAttributes}
        >
            <span className="n2o-dropdown-check-container">
                {visibleState && <i className="fa fa-check" aria-hidden="true" />}
            </span>
            {icon && <i className={classNames(icon, { 'mr-1': currentLabel })} />}
            {currentLabel && <span>{currentLabel}</span>}
        </Component>
    )
}
