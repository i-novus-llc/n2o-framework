import React, { useCallback, useMemo, VFC } from 'react'

import { RowResolverProps } from '../types/props'
import { SelectionType } from '../enum'
import { useTableActions } from '../provider/TableActions'

import Table from './basic'
import { CellContainer } from './CellContainer'

export const RowResolver: VFC<RowResolverProps> = (props) => {
    const {
        component: CustomRowComponent,
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
        hasSecurityAccess,
        click,
        ...otherProps
    } = props
    const { setFocusOnRow, onDispatchRowAction } = useTableActions()

    const { style, ...otherElementAttributes } = elementAttributes || {}
    const hasSelection = selection !== SelectionType.None
    const hasRowAction = click && hasSecurityAccess

    const onClickRowAction = useCallback((data) => {
        if (click) {
            onDispatchRowAction(click, data)
        }
    }, [click, onDispatchRowAction])
    const onSelection = useCallback((data) => {
        setFocusOnRow(data.id, data)
    }, [setFocusOnRow])

    const onRowClick = useCallback((data) => {
        if (hasSelection) {
            onSelection(data)
        }

        if (hasRowAction) {
            onClickRowAction(data)
        }
    }, [hasSelection, hasRowAction, onClickRowAction, onSelection])

    const mergedStyle = useMemo(() => ({
        '--deep-level': treeDeepLevel,
        ...style,
    }), [treeDeepLevel, style])

    const cellsComponents = useMemo(() => (
        cells.map(({
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
        ))
    ), [cells, data, hasExpandedButton, isSelectedRow, isTreeExpanded, rowValue])
    const dataParams = useMemo(() => ({
        'data-focused': isFocused,
        'data-has-click': hasSelection || hasRowAction,
        'data-deep-level': treeDeepLevel,
    }), [hasRowAction, hasSelection, isFocused, treeDeepLevel])

    return CustomRowComponent ? (
        <CustomRowComponent
            {...otherProps}
            selection={selection}
            data={data}
            elementAttributes={otherElementAttributes}
            onClick={hasRowAction ? onClickRowAction : undefined}
            onSelection={hasSelection ? onSelection : undefined}
            style={mergedStyle}
            isSelectedRow={isSelectedRow}
            isFocused={isFocused}
            treeDeepLevel={treeDeepLevel}
            dataParams={dataParams}
        >
            {cellsComponents}
        </CustomRowComponent>
    ) : (
        <Table.Row
            {...otherElementAttributes}
            onClick={() => onRowClick(data)}
            style={mergedStyle}
            data-selected={isSelectedRow}
            {...dataParams}
        >
            {cellsComponents}
        </Table.Row>
    )
}
