import React, { Fragment, VFC } from 'react'

import { RowsProps } from '../types/props'

import { RowContainer } from './RowContainer'

export const Rows: VFC<RowsProps> = (props) => {
    const {
        rowRenderFieldKey,
        data,
        treeDeepLevel = 0,
        treeDataKey,
        selectedRows,
        expandedRows,
        selectedKey,
        focusedRowValue,
        ...otherProps
    } = props

    return (
        <>
            {data.map((dataItem, index) => {
                const treeData = dataItem?.[treeDataKey] || []
                const isSelectedRow = selectedRows.some(selectedRowItem => selectedRowItem === dataItem[selectedKey])
                const isTreeExpanded = expandedRows.some(expandedTreeValue => expandedTreeValue === dataItem.id)
                const rowValue = dataItem.id
                const isFocused = focusedRowValue === rowValue
                const hasTreeData = Boolean(treeData.length)

                return (
                    <Fragment key={rowRenderFieldKey ? dataItem[rowRenderFieldKey] : index}>
                        <RowContainer
                            rowIndex={index}
                            data={dataItem}
                            isFocused={isFocused}
                            rowValue={rowValue}
                            isSelectedRow={isSelectedRow}
                            treeDeepLevel={treeDeepLevel}
                            isTreeExpanded={isTreeExpanded}
                            hasExpandedButton={hasTreeData}
                            {...otherProps}
                        />
                        {(hasTreeData && isTreeExpanded) ? (
                            <Rows
                                {...props}
                                data={treeData}
                                treeDeepLevel={treeDeepLevel + 1}
                            />
                        ) : null}
                    </Fragment>
                )
            })}
        </>
    )
}

Rows.displayName = 'Rows'
