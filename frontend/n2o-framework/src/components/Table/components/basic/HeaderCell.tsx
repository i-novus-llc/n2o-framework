import React, { forwardRef, ThHTMLAttributes } from 'react'
import classNames from 'classnames'

export type TableHeaderCellProps = ThHTMLAttributes<HTMLTableCellElement>

export const TableHeaderCell = forwardRef<HTMLTableCellElement, TableHeaderCellProps>(({
    children,
    className,
    ...props
}, ref) => {
    const classes = classNames('cell', className)

    return (
        <th
            ref={ref}
            className={classes}
            {...props}
        >
            {children}
        </th>
    )
})

TableHeaderCell.displayName = 'TableHeaderCell'
