import React, { ComponentType, forwardRef, useMemo } from 'react'
import classNames from 'classnames'

import { Selection } from '../enum'
import { type ChildrenTableHeaderProps, type TableHeaderProps } from '../types/props'
import { parseHeaderRows } from '../utils/parseHeaderRows'

import { CheckboxHeaderCell } from './selection/checkbox-header'
import { TableHeaderCell } from './header-cell'

export const TableHeader = forwardRef<HTMLTableSectionElement, TableHeaderProps>(({
    selection,
    areAllRowsSelected,
    cells,
    sorting,
    validateFilterField,
    filterErrors,
    scrollbar,
    selectionFixed,
}, ref) => {
    const rows = useMemo(() => parseHeaderRows(cells), [cells])
    const selectClassName = classNames('cell-selection', { 'sticky-cell sticky-left': selectionFixed })

    return (
        <thead ref={ref}>
            {rows.map((columns, index) => (
                <tr>
                    {(selection === Selection.Checkbox) && (index === 0) && (
                        // eslint-disable-next-line jsx-a11y/control-has-associated-label
                        <th key={selection} className={selectClassName} rowSpan={rows.length}>
                            <CheckboxHeaderCell areAllRowsSelected={areAllRowsSelected} />
                        </th>
                    )}
                    {(selection === Selection.Radio) && (index === 0) && (
                        // eslint-disable-next-line jsx-a11y/control-has-associated-label
                        <th key={selection} className={selectClassName} rowSpan={rows.length} />
                    )}
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
                </tr>
            ))}
            {scrollbar}
        </thead>
    )
})

TableHeader.displayName = 'TableHeader'
