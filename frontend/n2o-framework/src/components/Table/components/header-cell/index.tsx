import React, { useRef, VFC } from 'react'
import classNames from 'classnames'

import Table from '../basic'
import { TableHeaderCellProps } from '../../models/props'
// @ts-ignore - отсутствуют типы
import { Icon } from '../../../snippets/Icon/Icon'
import { useMouseDownResize } from '../../hooks/useMouseDownResize'

import { HeaderFilter } from './header-filter'

export const TableHeaderCell: VFC<TableHeaderCellProps> = (props) => {
    const {
        component: Component,
        id,
        filterControl,
        sortingDirection,
        colSpan,
        rowSpan,
        multiHeader,
        elementAttributes,
        icon,
        resizable,
        ...otherCellProps
    } = props

    // Заготовка для ресайза колонок
    const cellRef = useRef<HTMLTableCellElement>(null)
    const onMouseDownResizeCell = useMouseDownResize(cellRef)

    return (
        <Table.HeaderCell
            className={classNames({
                'n2o-advanced-table-header-text-center': multiHeader,
            })}
            data-resizeble={resizable}
            ref={cellRef}
            colSpan={colSpan}
            rowSpan={rowSpan}
        >
            <div className="n2o-advanced-table-header-cell-content">
                {icon && <Icon name={icon} />}
                {filterControl ? (
                    <HeaderFilter
                        id={id}
                        filterControl={filterControl}
                    />

                ) : null}
                <Component {...otherCellProps} {...elementAttributes} sorting={sortingDirection} />
            </div>
            {resizable ? <div className="resizeTrigger" onMouseDown={onMouseDownResizeCell} /> : null}
        </Table.HeaderCell>
    )
}

TableHeaderCell.defaultProps = {
    elementAttributes: {},
}
TableHeaderCell.displayName = 'TableHeaderCell'
