import React, { useRef, VFC } from 'react'
import classNames from 'classnames'

import Table from '../basic'
import { TableHeaderCellProps } from '../../types/props'
import { Icon } from '../../../snippets/Icon/Icon'
import { useMouseDownResize } from '../../hooks/useMouseDownResize'

import { HeaderFilter } from './header-filter'

export const TableHeaderCell: VFC<TableHeaderCellProps> = (props) => {
    const {
        component: Component,
        id,
        filterField,
        sortingDirection,
        colSpan,
        rowSpan,
        multiHeader,
        icon,
        resizable,
        elementAttributes = {},
        validateFilterField,
        filterError,
        ...rest
    } = props

    const { className } = elementAttributes
    const cellRef = useRef<HTMLTableCellElement>(null)
    const onMouseDownResizeCell = useMouseDownResize(cellRef)

    return (
        <Table.HeaderCell
            data-resizeble={resizable}
            ref={cellRef}
            colSpan={colSpan}
            rowSpan={rowSpan}
            {...elementAttributes}
            className={classNames(className, { 'n2o-advanced-table-header-text-center': multiHeader })}
        >
            <div className="n2o-advanced-table-header-cell-content">
                <Icon name={icon} visible={typeof icon === 'string'} />
                <Component {...rest} sorting={sortingDirection} />
                {filterField && (
                    <HeaderFilter
                        id={id}
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
