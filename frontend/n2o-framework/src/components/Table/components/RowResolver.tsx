import React, { useCallback, useMemo, VFC } from 'react'

import { RowResolverProps } from '../types/props'
import { Selection } from '../enum'
import { useTableActions } from '../provider/TableActions'

import { CellContainer } from './CellContainer'
import { DataRow } from './DataRow'

export const RowResolver: VFC<RowResolverProps> = (props) => {
    const {
        component: RowComponent = DataRow,
        elementAttributes,
        data,
        treeDeepLevel,
        isSelectedRow,
        isFocused,
        cells,
        rowValue,
        isTreeExpanded,
        hasExpandedButton,
        selection,
        click,
        rowIndex,
        ...otherProps
    } = props
    const { setFocusOnRow, onRowClick } = useTableActions()

    const onClickRowAction = useCallback((data) => { onRowClick(data) }, [onRowClick])
    const onSelection = useCallback((data) => { setFocusOnRow(data.id, data) }, [setFocusOnRow])

    const { style, ...otherElementAttributes } = elementAttributes || {}
    const hasSelection = selection !== Selection.None
    const hasRowAction = typeof click !== 'undefined'

    const mergedStyle = useMemo(() => ({
        '--deep-level': treeDeepLevel,
        ...style,
    }), [treeDeepLevel, style])

    return (
        <RowComponent
            {...otherProps}
            {...otherElementAttributes}
            rowIndex={rowIndex}
            selection={selection}
            data={data}
            onClick={hasRowAction ? onClickRowAction : undefined}
            onSelection={hasSelection ? onSelection : undefined}
            style={mergedStyle}
            data-focused={isFocused}
            data-has-click={hasSelection || hasRowAction}
            data-deep-level={treeDeepLevel}
            data-selected={isSelectedRow}
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
                    rowIndex={rowIndex}
                    {...elementAttributes}
                    {...cellProps}
                />
            ))}
        </RowComponent>
    )
}
