import React, { FC, TableHTMLAttributes } from 'react'
import classNames from 'classnames'

type TableFooterProps = TableHTMLAttributes<HTMLTableSectionElement>

export const TableFooter: FC<TableFooterProps> = ({
    children,
    className,
    ...props
}) => {
    const classes = classNames('footer', className)

    return (
        <tfoot
            className={classes}
            {...props}
        >
            {children}
        </tfoot>
    )
}

TableFooter.displayName = 'TableFooter'
