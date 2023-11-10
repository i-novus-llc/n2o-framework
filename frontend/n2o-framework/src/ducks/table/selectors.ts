import { createSelector } from '@reduxjs/toolkit'

import { State } from '../State'
import { EMPTY_OBJECT } from '../../utils/emptyTypes'

import { State as TableState } from './Table'

export const tablesSelector = (state: State): TableState => state.table || EMPTY_OBJECT

export const makeTableByIdSelector = (widgetId: string) => createSelector(
    tablesSelector,
    tables => tables[widgetId] || EMPTY_OBJECT,
)

export const getTableColumns = (widgetId: string) => createSelector(
    makeTableByIdSelector(widgetId),
    tableState => tableState?.columns || {},
)
