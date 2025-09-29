import React, { useCallback, useMemo, VFC, MouseEvent } from 'react'

import { RowResolverProps } from '../types/props'
import { Selection } from '../enum'
import { useTableActions } from '../provider/TableActions'
import { useToolbarOverlay } from '../provider/ToolbarOverlay'
import { useTableRefProps } from '../provider/TableRefProps'

import { CellContainer } from './CellContainer'
import { DataRow } from './DataRow'

export const RowResolver: VFC<RowResolverProps> = ({
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
}) => {
    const { setFocusOnRow, onRowClick, selectSingleRow } = useTableActions()
    const { onShowOverlay, onHideOverlay } = useToolbarOverlay()

    const onClickRowAction = useCallback((data) => { onRowClick(data) }, [onRowClick])

    const onSelection = useCallback((data) => {
        setFocusOnRow(data.id, data)

        if (selection === Selection.Radio) {
            selectSingleRow(data?.id)
        }
    }, [selectSingleRow, selection, setFocusOnRow])

    const tableProps = useTableRefProps()
    const CellComponentContainer = tableProps.current.components?.CellContainer || CellContainer

    const { style, ...otherElementAttributes } = elementAttributes || {}
    const hasSelection = selection !== Selection.None
    const hasRowAction = typeof click !== 'undefined'

    const mergedStyle = useMemo(() => ({
        '--deep-level': treeDeepLevel,
        ...style,
    }), [treeDeepLevel, style])

    const onMouseEnter = useCallback((event: MouseEvent) => {
        if (onShowOverlay) { onShowOverlay(event, data) }
    }, [onShowOverlay, data])

    return (
        <RowComponent
            {...otherProps}
            {...otherElementAttributes}
            rowIndex={rowIndex}
            selection={selection}
            data={data}
            onClick={hasRowAction ? onClickRowAction : undefined}
            onSelection={(hasSelection || hasRowAction) ? onSelection : undefined}
            onMouseEnter={onShowOverlay ? onMouseEnter : undefined}
            onMouseLeave={onHideOverlay}
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
                <CellComponentContainer
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
