import { State, Table } from './Table'

export const initialState: State = {}

export const defaultColumnState = {
    isInit: true,
    visible: true,
    visibleState: true,
    disabled: false,
    frozen: false,
    key: '',
    columnId: '',
}

export const defaultTableState: Table = {
    columns: {},
}
