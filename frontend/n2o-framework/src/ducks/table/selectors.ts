import { createSelector } from '@reduxjs/toolkit'
import get from 'lodash/get'

import { State } from '../State'
import { EMPTY_OBJECT } from '../../utils/emptyTypes'

import { State as TableState } from './Table'

export const tablesSelector = (state: State): TableState => state.table || EMPTY_OBJECT

export const makeTableByIdSelector = (widgetId: string) => createSelector(
    tablesSelector,
    tables => tables[widgetId] || EMPTY_OBJECT,
)

export const getTableHeaderCells = (widgetId: string) => createSelector(
    makeTableByIdSelector(widgetId),
    tableState => tableState?.header?.cells || [],
)

export const getTableParam = (widgetId: string, paramKey: string) => createSelector(
    makeTableByIdSelector(widgetId),
    tableState => get(tableState, paramKey, null),
)
