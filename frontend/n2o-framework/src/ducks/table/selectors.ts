import { createSelector } from '@reduxjs/toolkit'
import get from 'lodash/get'

import { State } from '../State'
import { EMPTY_OBJECT } from '../../utils/emptyTypes'

import { State as TableState, Table } from './Table'

export const tablesSelector = (state: State): TableState => state.table || EMPTY_OBJECT

export const makeTableByIdSelector = (widgetId: string) => createSelector(
    tablesSelector,
    tables => tables[widgetId] || EMPTY_OBJECT,
)

export const makeTableByDatasourceSelector = (datasourceId: string) => createSelector(
    tablesSelector,
    tables => Object.values(tables).find(({ datasource }) => (datasource === datasourceId)) || EMPTY_OBJECT,
)

export const getTableCells = (widgetId: string) => createSelector(
    makeTableByIdSelector(widgetId),
    (tableState: Table) => {
        if (!tableState) { return { header: [], body: [] } }

        return {
            header: tableState.header.cells,
            body: tableState.body.cells,
        }
    },
)

export const getTableParam = (widgetId: string, paramKey: string) => createSelector(
    makeTableByIdSelector(widgetId),
    tableState => get(tableState, paramKey, null),
)
