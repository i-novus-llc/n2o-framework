import React, { ComponentType, memo, useMemo } from 'react'

import { Selection } from '../enum'
import { type ChildrenTableHeaderProps, type TableHeaderProps } from '../types/props'
import { parseHeaderRows } from '../utils/parseHeaderRows'

import { CheckboxHeaderCell } from './selection/checkbox-header'
import { TableHeaderCell } from './header-cell'
import Table from './basic'

export const TableHeader = memo<TableHeaderProps>(({
    selection,
    areAllRowsSelected,
    cells,
    sorting,
    validateFilterField,
    filterErrors,
}) => {
    const rows = useMemo(() => parseHeaderRows(cells), [cells])

    return (
        <Table.Header>
            {rows.map(columns => (
                <Table.Row>
                    {selection === Selection.Checkbox && (
                        <Table.HeaderCell key={selection} className="cell-selection">
                            <CheckboxHeaderCell areAllRowsSelected={areAllRowsSelected} />
                        </Table.HeaderCell>
                    )}
                    {selection === Selection.Radio && (
                        <Table.HeaderCell key={selection} className="cell-selection" />
                    )}
                    <>
                        {columns.map((cell) => {
                            const { moveMode } = cell

                            if (moveMode) {
                                const Component = cell.component as ComponentType<ChildrenTableHeaderProps>

                                return (
                                    <Component
                                        {...cell}
                                        sorting={sorting}
                                        validateFilterField={validateFilterField}
                                        filterErrors={filterErrors}
                                    />
                                )
                            }

                            return (
                                <TableHeaderCell
                                    key={cell.id}
                                    sortingDirection={cell.sortingParam ? sorting[cell.sortingParam] : undefined}
                                    validateFilterField={validateFilterField}
                                    filterError={filterErrors?.[cell.id]}
                                    {...cell}
                                />
                            )
                        })}
                    </>
                </Table.Row>
            ))}
        </Table.Header>
    )
})
