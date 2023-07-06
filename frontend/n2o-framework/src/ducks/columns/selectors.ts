import { createSelector } from '@reduxjs/toolkit'

import { State } from '../State'
import { EMPTY_OBJECT } from '../../utils/emptyTypes'

/**
 * Получение стора columns
 */
export const columnsSelector = (state: State) => state.columns || EMPTY_OBJECT

/**
 * Получение элемента columns store
 */
export const getContainerColumns = (key: string) => createSelector(
    columnsSelector,
    columns => columns[key] || EMPTY_OBJECT,
)

/**
 * Получение колонки из columns store
 */
export const makeColumnByKeyAndIdSelector = (key: string, id: string) => createSelector(
    getContainerColumns(key),
    columns => columns[id] || EMPTY_OBJECT,
)

/**
 * Получение значение isInit колонки
 */
export const isInitSelector = (key: string, id: string) => createSelector(
    makeColumnByKeyAndIdSelector(key, id),
    column => column.isInit,
)

/**
 * Получение значение visible колонки
 */
export const isVisibleSelector = (key: string, id: string) => createSelector(
    makeColumnByKeyAndIdSelector(key, id),
    column => column.visible,
)

/**
 * Получение значение disabled колонки
 */
export const isDisabledSelector = (key: string, id: string) => createSelector(
    makeColumnByKeyAndIdSelector(key, id),
    column => column.disabled,
)
