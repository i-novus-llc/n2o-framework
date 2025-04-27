import { Table } from './Table'

export const VISIBLE_STATE = 'visibleState'
export const IS_DEFAULT_COLUMNS = 'isDefaultColumns'

export const getDefaultColumnState = () => ({
    isInit: true,
    visible: true,
    [VISIBLE_STATE]: true,
    disabled: false,
    frozen: false,
    key: '',
    columnId: '',
})

export const getDefaultTableState = (): Table => ({
    header: { cells: [] },
    [IS_DEFAULT_COLUMNS]: false,
})
