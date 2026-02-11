import { CSSProperties } from 'react'

import { type BodyCell, type FilterField, type HeaderCell } from '../../../ducks/table/Table'
import { Selection } from '../../../ducks/table/Table'
import { ToolbarProps } from '../../buttons/Toolbar'
import type { Props as WidgetFiltersProps } from '../WidgetFilters'
import { PLACES } from '../WidgetLayout/WidgetLayout'
import { type Props as StandardWidgetProps } from '../StandardWidget'
import { Validation, TableWidgetContainerProps } from '../../Table/types/props'
import { type Props as N2OPaginationProps } from '../Table/N2OPagination'
import { onFilterType } from '../../Table/hooks/useChangeFilter'
import { SortDirection } from '../../../core/datasource/const'
import { type Paging } from '../../../ducks/datasource/Provider'

import { type ChangeColumnParam, type ColumnState } from './hooks/useColumnsState'

export type BodyCells = { cells: BodyCell[] }
export type HeaderCells = { cells: HeaderCell[] }

export interface TableCells {
    body: BodyCells
    header: HeaderCells
}

export interface TableProps extends TableCells {
    autoFocus: boolean
    autoSelect: boolean
    rowSelection: Selection
    textWrap: boolean
}

export interface WithTableType {
    filter: { filterPlace: PLACES, filterFieldsets: WidgetFiltersProps['fieldsets'] }
    id: string
    table: TableProps
    datasourceModelLength: number
    datasource: string
    page: number
    size?: number
    saveSettings?: boolean
    sorting?: Record<string, SortDirection>
    setPage: N2OPaginationProps['setPage']
}

type Enhancer = WithTableType & Pick<
    StandardWidgetProps, 'fetchData' | 'datasource' | 'disabled' | 'loading' | 'children' | 'style' | 'table'>

export interface AdvancedTableWidgetProps extends Enhancer {
    toolbar: Record<string, ToolbarProps>
    className: string
    setPage: N2OPaginationProps['setPage']
    paging: { showCount: boolean, place: string } & N2OPaginationProps
    size: N2OPaginationProps['size']
    count: N2OPaginationProps['count']
    validations: Record<string, Validation[]>
    sorting: TableWidgetContainerProps['sorting']
    hasNext: boolean
    isInit: boolean
    setResolve(model: Record<string, unknown>): void
    changeColumnParam: ChangeColumnParam
    resetSettings(): void
    columnsState: ColumnState
    tableConfig: TableWidgetContainerProps['tableConfig']
    resolvedFilter: WithTableType['filter']
    resolvedCells: TableWidgetContainerProps['cells']
    paginationVisible: N2OPaginationProps['visible']
    dataMapper(data: unknown): unknown
    components: TableWidgetContainerProps['components']
    setFilter: onFilterType,
    textWrap: boolean
    width?: CSSProperties['width']
    layout: {
        scrollbarPosition?: 'top' | 'bottom'
        stickyHeader?: boolean
        stickyFooter?: boolean
    }
}

export type TableStateCache = {
    header: CellStateCache[]
    body: CellStateCache[]
    textWrap: boolean
    datasourceFeatures: Partial<{ // TODO rename: datasource
        paging: Partial<Pick<Paging, 'size' | 'page'>>
        sorting: Record<string, SortDirection>
    }>
}

export interface CellStateCache {
    id: string
    visibleState?: boolean
    children?: CellStateCache[]
    format?: string
    filterField: FilterField | null
}
