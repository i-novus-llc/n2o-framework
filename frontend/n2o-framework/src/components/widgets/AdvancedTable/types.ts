import { CSSProperties } from 'react'

import { type BodyCell, type FilterField, type HeaderCell } from '../../../ducks/table/Table'
import { Selection } from '../../Table'
import { ToolbarProps } from '../../buttons/Toolbar'
import type { Props as WidgetFiltersProps } from '../WidgetFilters'
import { PLACES } from '../StandardWidget'
import { type Props as StandardWidgetProps } from '../StandardWidget'
import { Validation, TableWidgetContainerProps } from '../../Table/types/props'
import { type Props as N2OPaginationProps } from '../Table/N2OPagination'
import { onFilterType } from '../../Table/hooks/useChangeFilter'
import { SortDirection } from '../../../core/datasource/const'
import { type Paging } from '../../../ducks/datasource/Provider'

import { type ChangeColumnParam, type SwitchTableParam, type ColumnState } from './hooks/useColumnsState'

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
    textWrap: true
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
    switchTableParam: SwitchTableParam
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

export interface AdvancedTableWidgetLocalStorageProps {
    widgetId: AdvancedTableWidgetProps['id']
    textWrap: AdvancedTableWidgetProps['textWrap']
    columnsState: AdvancedTableWidgetProps['columnsState']
    sorting: AdvancedTableWidgetProps['sorting']
    page: AdvancedTableWidgetProps['page']
    size: AdvancedTableWidgetProps['size']
    componentName: string
}

export enum SAVED_SETTINGS {
    HEADER = 'header',
    BODY = 'body',
    TEXT_WRAP = 'textWrap',
    DATA_SOURCE_SETTINGS = 'datasourceFeatures',
}

export enum DATA_SOURCE_SAVED_SETTINGS {
    PAGING = 'paging',
    SORTING = 'sorting',
}

export interface DatasourceSavedSettings {
    [DATA_SOURCE_SAVED_SETTINGS.PAGING]: Paging
    [DATA_SOURCE_SAVED_SETTINGS.SORTING]: Record<string, SortDirection>
}

export interface SavedColumn {
    id: string
    visibleState?: boolean
    children?: SavedColumn[]
    format: string | null
    filterField: FilterField | null
}
