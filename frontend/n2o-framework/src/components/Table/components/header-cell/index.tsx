import React, { useRef, VFC } from 'react'
import classNames from 'classnames'

import Table from '../basic'
import { TableHeaderCellProps } from '../../types/props'
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

    const { className = '', ...otherElementAttributes } = elementAttributes
    const cellRef = useRef<HTMLTableCellElement>(null)
    const onMouseDownResizeCell = useMouseDownResize(cellRef)

    return (
        <Table.HeaderCell
            className={classNames({
                'n2o-advanced-table-header-text-center': multiHeader,
                [className]: Boolean(className),
            })}
            data-resizeble={resizable}
            ref={cellRef}
            colSpan={colSpan}
            rowSpan={rowSpan}
        >
            <div className="n2o-advanced-table-header-cell-content">
                {icon && <Icon name={icon} />}
                <Component {...otherCellProps} {...otherElementAttributes} sorting={sortingDirection} />
                {filterControl ? (
                    <HeaderFilter
                        id={id}
                        filterControl={filterControl}
                    />

                ) : null}
            </div>
            {resizable ? <div className="resizeTrigger" onMouseDown={onMouseDownResizeCell} /> : null}
        </Table.HeaderCell>
    )
}

TableHeaderCell.defaultProps = {
    elementAttributes: {},
}
TableHeaderCell.displayName = 'TableHeaderCell'
