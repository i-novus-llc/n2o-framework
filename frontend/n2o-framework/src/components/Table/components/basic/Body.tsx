import React, { FC, TableHTMLAttributes } from 'react'
import classNames from 'classnames'

type TableBodyProps = TableHTMLAttributes<HTMLTableSectionElement>

export const TableBody: FC<TableBodyProps> = ({
    children,
    className,
    ...props
}) => {
    const classes = classNames('body', className)

    return (
        <tbody
            className={classes}
            {...props}
        >
            {children}
        </tbody>
    )
}

TableBody.displayName = 'TableBody'
