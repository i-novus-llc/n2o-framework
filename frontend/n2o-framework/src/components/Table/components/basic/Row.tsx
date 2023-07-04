import React, { FC, TdHTMLAttributes } from 'react'
import classNames from 'classnames'

export type TableRowProps = TdHTMLAttributes<HTMLTableRowElement>

export const TableRow: FC<TableRowProps> = ({
    children,
    className,
    ...props
}) => {
    const classes = classNames('table-row', className)

    return (
        <tr
            className={classes}
            {...props}
        >
            {children}
        </tr>
    )
}

TableRow.displayName = 'TableRow'
