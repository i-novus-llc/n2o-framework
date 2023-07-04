import React, { memo, useCallback, useMemo } from 'react'

import { DataRowProps } from '../models/props'

import Table from './basic'
import { CellContainer } from './CellContainer'

export const DataRow = memo<DataRowProps>(({
    onClick,
    data,
    rowValue,
    isSelectedRow,
    cells,
    isTreeExpanded,
    hasExpandedButton,
    treeDeepLevel,
    elementAttributes,
    isFocused,
}) => {
    const { style, ...otherElementAttributes } = elementAttributes || {}
    const hasClick = !!onClick
    const onRowClick = useCallback(() => {
        if (onClick) {
            onClick(data)
        }
    }, [data, onClick])

    const mergedStyle = useMemo(() => ({
        '--deep-level': treeDeepLevel,
        ...style,
    }), [treeDeepLevel, style])

    return (
        <Table.Row
            {...otherElementAttributes}
            onClick={onRowClick}
            style={mergedStyle}
            data-selected={isSelectedRow}
            data-focused={isFocused}
            data-has-click={hasClick}
        >
            {cells.map(({
                elementAttributes,
                ...cellProps
            }, index) => (
                <CellContainer
                    key={cellProps.id}
                    cellIndex={index}
                    hasExpandedButton={hasExpandedButton}
                    isSelectedRow={isSelectedRow}
                    model={data}
                    rowValue={rowValue}
                    isTreeExpanded={isTreeExpanded}
                    {...elementAttributes}
                    {...cellProps}
                />
            ))}
        </Table.Row>
    )
})

DataRow.displayName = 'DataRow'
