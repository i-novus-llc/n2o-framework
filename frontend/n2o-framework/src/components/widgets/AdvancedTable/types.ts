import { type Cell, type HeaderCell } from '../../Table/types/cell'
import { Selection } from '../../Table'
import { ToolbarProps } from '../../buttons/Toolbar'
import type { Props as WidgetFiltersProps } from '../WidgetFilters'
import { PLACES } from '../StandardWidget'
import { type Props as StandardWidgetProps } from '../StandardWidget'
import { Validation, TableWidgetContainerProps } from '../../Table/types/props'
import { type Props as N2OPaginationProps } from '../Table/N2OPagination'
import { onFilterType } from '../../Table/hooks/useChangeFilter'

import { type ChangeColumnParam, type ColumnState } from './hooks/useColumnsState'

export type BodyCells = { cells: Cell[] }
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
    filter: { filterPlace: PLACES, filterFieldsets: WidgetFiltersProps['fieldsets'] },
    id: string,
    table: TableProps,
    datasourceModelLength: number,
    datasource: string,
    page: number,
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
    columnsState: ColumnState
    tableConfig: TableWidgetContainerProps['tableConfig']
    switchTableParam: ChangeColumnParam
    resolvedFilter: WithTableType['filter']
    resolvedCells: TableWidgetContainerProps['cells']
    paginationVisible: N2OPaginationProps['visible']
    dataMapper(data: unknown): unknown
    components: TableWidgetContainerProps['components']
    setFilter: onFilterType,
}
