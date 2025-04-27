import React from 'react'
import classNames from 'classnames'

export interface DragHandleProps {
    label?: string
    className?: string
    empty?: boolean
}

export function DragHandle({ label, className, empty = false, ...rest }: DragHandleProps) {
    return (
        <div className={classNames('drag-handle', className)} {...rest}>
            {!empty && <i className="fa fa-ellipsis-v" />}
            {!empty && label && <span className="d-none">{` ${label}`}</span>}
        </div>
    )
}
