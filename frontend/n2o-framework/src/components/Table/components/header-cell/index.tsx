import React, { useRef, VFC } from 'react'
import classNames from 'classnames'
import { Icon } from '@i-novus/n2o-components/lib/display/Icon'

import Table from '../basic'
import { TableHeaderCellProps } from '../../types/props'
import { useMouseDownResize } from '../../hooks/useMouseDownResize'
import { DragHandle } from '../../../buttons/ToggleColumn/DragAndDropColumn/DragHandle'

import { HeaderFilter } from './header-filter'

export const TableHeaderCell: VFC<TableHeaderCellProps> = ({
    component: Component,
    filterField,
    sortingDirection,
    colSpan,
    rowSpan,
    multiHeader,
    icon,
    resizable,
    validateFilterField,
    filterError,
    elementAttributes = {},
    dragAttributes = null,
    ...rest
}) => {
    const {
        className, componentClassName, alignment,
        width, style, ...otherElementAttributes
    } = elementAttributes
    const cellRef = useRef<HTMLTableCellElement>(null)
    const onMouseDownResizeCell = useMouseDownResize(cellRef)
    let extStyle = style

    if (width) {
        extStyle = {
            minWidth: width,
            maxWidth: width,
            ...style,
        }
    }

    return (
        <Table.HeaderCell
            data-resizeble={resizable}
            ref={cellRef}
            colSpan={colSpan}
            rowSpan={rowSpan}
            align={alignment}
            style={extStyle}
            {...otherElementAttributes}
            className={classNames(className, { 'n2o-advanced-table-header-text-center': multiHeader, 'drag-header': dragAttributes })}
        >
            <div className="n2o-advanced-table-header-cell-content">
                {dragAttributes && <DragHandle {...dragAttributes} {...rest} />}
                <Icon name={icon} visible={typeof icon === 'string'} />
                <section className="n2o-advanced-table-header-cell__header">
                    <Component {...rest} className={componentClassName} sorting={sortingDirection} />
                    {filterField && (
                        <HeaderFilter
                            id={filterField.id}
                            filterField={filterField}
                            validateFilterField={validateFilterField}
                            filterError={filterError}
                        />
                    )}
                </section>
            </div>
            {resizable && <div className="resizeTrigger" onMouseDown={onMouseDownResizeCell} />}
        </Table.HeaderCell>
    )
}

TableHeaderCell.displayName = 'TableHeaderCell'
