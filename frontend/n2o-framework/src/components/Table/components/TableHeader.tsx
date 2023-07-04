import React, { memo, useMemo } from 'react'

import { SelectionType } from '../enum'
import { TableHeaderProps } from '../types/props'
import { parseHeaderRows } from '../utils/parseHeaderRows'

import { CheckboxHeaderCell } from './selection/checkbox-header'
import { TableHeaderCell } from './header-cell'
import Table from './basic'

export const TableHeader = memo<TableHeaderProps>(({
    selection,
    areAllRowsSelected,
    cells,
    sorting,
}) => {
    const rows = useMemo(() => parseHeaderRows(cells), [cells])

    return (
        <Table.Header>
            {rows.map(columns => (
                <Table.Row>
                    {selection === SelectionType.Checkbox ? (
                        <Table.HeaderCell key={selection}>
                            <CheckboxHeaderCell areAllRowsSelected={areAllRowsSelected} />
                        </Table.HeaderCell>
                    ) : null}
                    {selection === SelectionType.Radio ? (
                        <Table.HeaderCell key={selection} />
                    ) : null}
                    {columns.map(cell => (
                        <TableHeaderCell
                            key={cell.id}
                            sortingDirection={sorting?.[cell.id]}
                            {...cell}
                        />
                    ))}
                </Table.Row>
            ))}
        </Table.Header>
    )
})
