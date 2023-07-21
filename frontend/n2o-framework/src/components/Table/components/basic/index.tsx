import React, { PropsWithChildren, TableHTMLAttributes } from 'react'
import classNames from 'classnames'

import { TableCell } from './Cell'
import { TableRow } from './Row'
import { TableBody } from './Body'
import { TableHeader } from './Header'
import { TableFooter } from './Footer'
import { TableHeaderCell } from './HeaderCell'

type TableProps = TableHTMLAttributes<HTMLTableElement>

export const Table = ({
    children,
    className,
    ...props
}: PropsWithChildren<TableProps>) => {
    const classes = classNames('n2o-advanced-table', className)

    return (
        <table
            className={classes}
            {...props}
        >
            {children}
        </table>
    )
}

Table.Cell = TableCell
Table.Row = TableRow
Table.Body = TableBody
Table.Header = TableHeader
Table.HeaderCell = TableHeaderCell
Table.Footer = TableFooter

export default Table
