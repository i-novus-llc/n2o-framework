import React, { useCallback, useMemo, VFC } from 'react'

import { RowResolverProps } from '../types/props'

import Table from './basic'
import { CellContainer } from './CellContainer'

export const RowResolver: VFC<RowResolverProps> = (props) => {
    const {
        component: CustomRowComponent,
        elementAttributes,
        onClick,
        data,
        treeDeepLevel,
        isSelectedRow,
        isFocused,
        cells,
        rowValue,
        isTreeExpanded,
        hasExpandedButton,
        ...otherProps
    } = props

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
        'data-has-click': hasClick,
        'data-deep-level': treeDeepLevel,
    }), [hasClick, isFocused, treeDeepLevel])

    return CustomRowComponent ? (
        <CustomRowComponent
            {...otherProps}
            data={data}
            elementAttributes={otherElementAttributes}
            onClick={onRowClick}
            style={mergedStyle}
            isSelectedRow={isSelectedRow}
            isFocused={isFocused}
            hasClick={hasClick}
            treeDeepLevel={treeDeepLevel}
            dataParams={dataParams}
        >
            {cellsComponents}
        </CustomRowComponent>
    ) : (
        <Table.Row
            {...otherElementAttributes}
            onClick={onRowClick}
            style={mergedStyle}
            data-selected={isSelectedRow}
            {...dataParams}
        >
            {cellsComponents}
        </Table.Row>
    )
}
