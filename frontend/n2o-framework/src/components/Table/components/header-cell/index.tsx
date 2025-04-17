import React, { useRef, VFC } from 'react'
import classNames from 'classnames'
import { Icon } from '@i-novus/n2o-components/lib/display/Icon'

import Table from '../basic'
import { TableHeaderCellProps } from '../../types/props'
import { useMouseDownResize } from '../../hooks/useMouseDownResize'
import { EMPTY_OBJECT } from '../../../../utils/emptyTypes'

import { HeaderFilter } from './header-filter'

export const TableHeaderCell: VFC<TableHeaderCellProps> = ({
    component: Component,
    id,
    filterField,
    sortingDirection,
    colSpan,
    rowSpan,
    multiHeader,
    icon,
    resizable,
    elementAttributes = EMPTY_OBJECT as TableHeaderCellProps['elementAttributes'],
    validateFilterField,
    filterError,
    ...rest
}) => {
    const { className, alignment, ...otherElementAttributes } = elementAttributes
    const cellRef = useRef<HTMLTableCellElement>(null)
    const onMouseDownResizeCell = useMouseDownResize(cellRef)

    return (
        <Table.HeaderCell
            data-resizeble={resizable}
            ref={cellRef}
            colSpan={colSpan}
            rowSpan={rowSpan}
            align={alignment}
            {...otherElementAttributes}
            className={classNames(className, { 'n2o-advanced-table-header-text-center': multiHeader })}
        >
            <div className="n2o-advanced-table-header-cell-content">
                <Icon name={icon} visible={typeof icon === 'string'} />
                <Component {...rest} sorting={sortingDirection} />
                {filterField && (
                    <HeaderFilter
                        id={filterField.id}
                        filterField={filterField}
                        validateFilterField={validateFilterField}
                        filterError={filterError}
                    />
                )}
            </div>
            {resizable && <div className="resizeTrigger" onMouseDown={onMouseDownResizeCell} />}
        </Table.HeaderCell>
    )
}

TableHeaderCell.displayName = 'TableHeaderCell'
