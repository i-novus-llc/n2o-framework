import React, { FC, TdHTMLAttributes } from 'react'
import classNames from 'classnames'

type TableCellProps = TdHTMLAttributes<HTMLTableCellElement>

export const TableCell: FC<TableCellProps> = ({
    children,
    className,
    ...props
}) => {
    const classes = classNames('cell', className)

    return (
        <td
            className={classes}
            {...props}
        >
            {children}
        </td>
    )
}

TableCell.displayName = 'TableCell'
