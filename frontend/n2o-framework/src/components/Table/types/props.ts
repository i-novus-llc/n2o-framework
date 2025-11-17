/* eslint-disable @typescript-eslint/no-explicit-any */
import { FC, RefObject, TdHTMLAttributes, CSSProperties, ReactNode } from 'react'

import { Selection, TableActions } from '../enum'
import { SortDirection } from '../../../core/datasource/const'
import { Severity } from '../../../core/validation/types'

import { Data, DataItem, ExpandedRows, SelectedRows } from './general'
import { Row } from './row'
import { Cell, HeaderCell } from './cell'

export interface Validation {
    enablingConditions: string[]
    expression: string
    on: string[]
    severity: Severity
    text: string
    type: string
    validationKey: string
}

interface FieldError {
    message: { severity: Severity, text: string }
    validationClass: 'is-valid' | 'has-warning' | 'is-invalid'
}

export type TableWidgetContainerProps<T extends HTMLElement = HTMLElement> = {
    filterValue?: Record<string, any>
    sorting: Record<string, SortDirection>
    data: Data
    isTextWrap: boolean
    focusedRowValue: string | null
    expandedRows: ExpandedRows
    selectedRows: SelectedRows
    actionListener(action: TableActions, payload: any): void
    errorComponent?: ReactNode
    EmptyContent?: ReactNode
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
    validateFilterField(id: string, model: Record<string, unknown>, reset?: boolean): boolean
    filterErrors?: Record<string, FieldError>
    components?: {
        /*
            Компонент который будет являться контейнером для всей ячейки. Внутри необходимо реализовать рендер компонента ячейки.
            Может быть полезен если на прикладе нужно реализовать какой то кастомный рендер всей ячейки, а не только конкретного ее типа
        */
        CellContainer: FC<CellContainerProps>
    }
    childrenToggleState?: ReactNode
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
}
& Pick<TableWidgetContainerProps, 'data' | 'sorting' | 'selectedRows' | 'focusedRowValue' | 'expandedRows'>

export type TableBodyProps = {
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
    isTextWrap?: boolean
}

export type TableHeaderProps = {
    selection: Selection
    areAllRowsSelected?: boolean
    cells: TableProps['headerCell']
    row: TableProps['headerRow']
    validateFilterField: TableWidgetContainerProps['validateFilterField']
    filterErrors?: TableWidgetContainerProps['filterErrors']
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
    validateFilterField: TableWidgetContainerProps['validateFilterField']
    filterError?: FieldError
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
    validateFilterField: TableWidgetContainerProps['validateFilterField']
    filterError?: FieldError
} & Required<Pick<HeaderCell, 'filterField'>>

type SwitchCellProps = {
    switchFieldId?: any
    switchList?: object
    switchDefault?: { elementAttributes?: Record<string, any> }
}

export type CellContainerProps = {
    isSelectedRow: boolean
    model: DataItem
    rowValue: string
    cellIndex: number
    hasExpandedButton: boolean
    isTreeExpanded: boolean
    rowIndex: RowContainerProps['rowIndex']
    alignment?: TdHTMLAttributes<HTMLTableCellElement>['align']
    style?: CSSProperties
    isTextWrap?: boolean
} & Omit<Cell, 'elementAttributes'> & SwitchCellProps
