import { createSelector } from '@reduxjs/toolkit'

/**
 * Получение стора columns
 * @param {Object.<string, any>} state
 * @return {Columns.store}
 */
export const columnsSelector = state => state.columns || {}

/**
 * Получение элемента columns store
 * @param {string} key
 * @return {Object.<string, any>}
 */
export const getContainerColumns = key => createSelector(
    columnsSelector,
    columns => columns[key] || {},
)

/**
 * Получение колонки из columns store
 * @param {string} key
 * @param {string} id
 * @return {Object.<string, any>}
 */
export const makeColumnByKeyAndIdSelector = (key, id) => createSelector(
    getContainerColumns(key),
    columns => columns[id] || {},
)

/**
 * Получение значение isInit колонки
 * @param {string} key
 * @param {string} id
 * @return {boolean}
 */
export const isInitSelector = (key, id) => createSelector(
    makeColumnByKeyAndIdSelector(key, id),
    column => column.isInit,
)

/**
 * Получение значение visible колонки
 * @param {string} key
 * @param {string} id
 * @return {boolean}
 */
export const isVisibleSelector = (key, id) => createSelector(
    makeColumnByKeyAndIdSelector(key, id),
    column => column.visible,
)

/**
 * Получение значение disabled колонки
 * @param {string} key
 * @param {string} id
 * @return {boolean}
 */
export const isDisabledSelector = (key, id) => createSelector(
    makeColumnByKeyAndIdSelector(key, id),
    column => column.disabled,
)
