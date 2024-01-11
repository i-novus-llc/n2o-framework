/* eslint-disable @typescript-eslint/no-explicit-any */
import { FC, RefObject, VFC } from 'react'

import { Selection, TableActions } from '../enum'
import { SortDirection } from '../../../core/datasource/const'

import { Data, DataItem, ExpandedRows, SelectedRows } from './general'
import { Row } from './row'
import { Cell, HeaderCell } from './cell'

export type TableWidgetContainerProps<T extends HTMLElement = HTMLElement> = {
    hasSecurityAccess: boolean
    filterValue?: Record<string, any>
    sorting: Record<string, SortDirection>
    data: Data
    isTextWrap: boolean
    focusedRowValue: string | null
    expandedRows: ExpandedRows
    selectedRows: SelectedRows
    actionListener(action: TableActions, payload: any): void
    errorComponent?: VFC
    EmptyContent?: VFC
    refContainerElem?: RefObject<T>
    cells: {
        body: Cell[]
        header: HeaderCell[]
    }
    toolbar?: unknown
    tableConfig: {
        width?: string
        height?: string
        rowSelection: Selection
        header: {
            row?: Row
        }
        body: {
            row?: Row
        }
    }
}

export type TableProps = {
    bodyRow?: Row
    headerRow?: Row
    rowRenderFieldKey: string
    selectedKey: string
    treeDataKey: string
    selection: Selection
    tableId: string
    headerCell: HeaderCell[]
    bodyCell: Cell[]
    hasSecurityAccess: boolean
}
& Pick<TableWidgetContainerProps, 'data' | 'sorting' | 'selectedRows' | 'focusedRowValue' | 'expandedRows'>

export type TableBodyProps ={
    row?: Row
    cells: TableProps['bodyCell']
    focusedRowValue: TableProps['focusedRowValue']
    treeDataKey: TableProps['treeDataKey']
    selectedKey: TableProps['selectedKey']
    selectedRows: TableProps['selectedRows']
    selection: TableProps['selection']
    expandedRows: TableProps['expandedRows']
    rowRenderFieldKey: TableProps['rowRenderFieldKey']
    data: TableProps['data']
    hasSecurityAccess: TableProps['hasSecurityAccess']
}

export type TableHeaderProps = {
    selection: Selection
    areAllRowsSelected?: boolean
    cells: TableProps['headerCell']
    row: TableProps['headerRow']
} & Pick<TableProps, 'sorting'>

export type CheckboxHeaderCellProps = {
    areAllRowsSelected?: boolean
}

export type TableHeaderCellProps = {
    component: FC<any>
    id: string
    icon?: string
    colSpan?: number
    rowSpan?: number
    multiHeader?: boolean
    sortingDirection?: string
    resizable?: boolean
} & Pick<HeaderCell, 'elementAttributes' | 'filterField'>

export type SelectionCellProps = {
    rowValue: string
    isSelectedRow: boolean
    selection: Selection
    model: DataItem
    isTreeExpanded: boolean
    hasExpandedButton: boolean
}

export type CheckboxCellProps = Omit<SelectionCellProps, 'selection'>

export type RadioCellProps = Omit<SelectionCellProps, 'selection'>

export type CustomCellComponentProps = {
    model: DataItem
    isSelectedRow: boolean
    isTreeExpanded: boolean
    hasExpandedButton: boolean
    rowValue: any
    selection?: Selection
}

export type ExpandButtonProps = {
    rowValue: string
    isTreeExpanded: boolean
}

export type RowsProps = Omit<TableBodyProps, 'row'> & {
    treeDeepLevel?: number
} & Row

export type RowContainerProps = {
    rowIndex: number
    data: DataItem
    isSelectedRow: boolean
    isTreeExpanded: boolean
    hasExpandedButton: boolean
    isFocused: boolean
    rowValue: string
} & Omit<RowsProps, 'rowRenderFieldKey' | 'treeDataKey' | 'expandedRows' | 'selectedRows' | 'data' | 'selectedKey' | 'focusedRowValue'>

export type RowResolverProps = Omit<RowContainerProps, | 'hasSelect'>

export type DataRowProps = {
    selection: RowContainerProps['selection']
    data: RowContainerProps['data']
    onClick?(data: RowContainerProps['data']): void
    onSelection?(data: RowContainerProps['data']): void
    style: Record<string, any>
    [x: string]: any
}

export type HeaderFilterProps = {
    id: string
} & Required<Pick<HeaderCell, 'filterField'>>

export type CellContainerProps = {
    isSelectedRow: boolean
    model: DataItem
    rowValue: string
    cellIndex: number
    hasExpandedButton: boolean
    isTreeExpanded: boolean
    rowIndex: RowContainerProps['rowIndex']
} & Omit<Cell, 'elementAttributes'>
