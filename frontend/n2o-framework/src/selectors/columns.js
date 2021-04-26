import { createSelector } from 'reselect'

const columnsSelector = state => state.columns || {}

const getContainerColumns = key => createSelector(
    columnsSelector,
    columns => columns[key] || {},
)

const makeColumnByKeyAndIdSelector = (key, id) => createSelector(
    getContainerColumns(key),
    columns => columns[id] || {},
)

const isInitSelector = (key, id) => createSelector(
    makeColumnByKeyAndIdSelector(key, id),
    column => column.isInit,
)

const isVisibleSelector = (key, id) => createSelector(
    makeColumnByKeyAndIdSelector(key, id),
    column => column.visible,
)

const isDisabledSelector = (key, id) => createSelector(
    makeColumnByKeyAndIdSelector(key, id),
    column => column.disabled,
)

export {
    getContainerColumns,
    isVisibleSelector,
    isInitSelector,
    isDisabledSelector,
}
