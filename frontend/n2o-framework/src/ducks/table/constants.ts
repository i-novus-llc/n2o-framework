import { State, Table } from './Table'

export const VISIBLE_STATE = 'visibleState'
export const IS_DEFAULT_COLUMNS = 'isDefaultColumns'

export const initialState: State = {}

export const defaultColumnState = {
    isInit: true,
    visible: true,
    [VISIBLE_STATE]: true,
    disabled: false,
    frozen: false,
    key: '',
    columnId: '',
}

export const defaultTableState: Table = {
    columns: {},
    [IS_DEFAULT_COLUMNS]: false,
}
