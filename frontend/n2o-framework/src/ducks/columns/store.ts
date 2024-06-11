import { createSlice } from '@reduxjs/toolkit'

import { RESET_STATE } from '../widgets/constants'

import { State } from './Columns'
import ColumnsResolver from './ColumnsResolver'
import {
    ChangeColumnDisabled,
    ChangeColumnVisibility,
    ChangeFrozenColumn,
    RegisterColumn,
    ToggleColumnVisibility,
} from './Actions'

export const initialState: State = {}

export const columnsSlice = createSlice({
    name: 'n2o/columns',
    initialState,
    reducers: {
        REGISTER_COLUMN: {
            prepare(
                key,
                columnId,
                label,
                visible,
                disabled,
                conditions,
            ) {
                return ({
                    payload: {
                        key,
                        columnId,
                        label,
                        visible,
                        disabled,
                        conditions,
                    },
                })
            },

            reducer(state, action: RegisterColumn) {
                const { key, columnId } = action.payload

                if (!state[key]) {
                    state[key] = {}
                }

                state[key][columnId] = { ...ColumnsResolver.defaultState, ...action.payload }
            },
        },

        CHANGE_COLUMN_VISIBILITY: {
            prepare(key, columnId, visible) {
                return ({
                    payload: {
                        visible,
                        key,
                        columnId,
                    },
                })
            },

            reducer(state, action: ChangeColumnVisibility) {
                const { key, columnId, visible } = action.payload

                if (!state[key]) {
                    state[key] = {}
                }

                if (!state[key][columnId]) {
                    state[key][columnId] = ColumnsResolver.defaultState
                }

                state[key][columnId].visible = visible
            },
        },

        CHANGE_COLUMN_DISABLED: {
            prepare(widgetId, columnId, disabled) {
                return ({
                    payload: {
                        disabled,
                        key: widgetId,
                        columnId,
                    },
                })
            },

            reducer(state, action: ChangeColumnDisabled) {
                const { key, columnId, disabled } = action.payload

                if (!state[key]) {
                    state[key] = {}
                }

                if (!state[key][columnId]) {
                    state[key][columnId] = ColumnsResolver.defaultState
                }

                state[key][columnId].disabled = disabled
            },
        },

        TOGGLE_COLUMN_VISIBILITY: {
            prepare(widgetId, columnId) {
                return ({
                    payload: {
                        key: widgetId,
                        columnId,
                    },
                })
            },

            reducer(state, action: ToggleColumnVisibility) {
                const { key, columnId } = action.payload

                if (!state[key]) {
                    state[key] = {}
                }

                if (!state[key][columnId]) {
                    state[key][columnId] = ColumnsResolver.defaultState
                }

                state[key][columnId].visible = !state[key][columnId].visible
            },
        },

        CHANGE_FROZEN_COLUMN: {
            /* FIXME дублирование с TOGGLE_COLUMN_VISIBILITY */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(widgetId, columnId) {
                return ({
                    payload: {
                        key: widgetId,
                        columnId,
                    },
                })
            },

            reducer(state, action: ChangeFrozenColumn) {
                const { key, columnId } = action.payload

                if (!state[key]) {
                    state[key] = {}
                }

                if (!state[key][columnId]) {
                    state[key][columnId] = ColumnsResolver.defaultState
                }

                state[key][columnId].frozen = !state[key][columnId].frozen
            },
        },
    },
    extraReducers: {
        [RESET_STATE](state, action) {
            const { widgetId }: { widgetId: string } = action.payload

            if (state[widgetId]) {
                Object.keys(state[widgetId]).forEach((key) => {
                    state[widgetId][key].isInit = false
                })
            }
        },
    },
})

export default columnsSlice.reducer

// Actions
export const {
    CHANGE_COLUMN_DISABLED: changeColumnDisabled,
    CHANGE_COLUMN_VISIBILITY: changeColumnVisibility,
    CHANGE_FROZEN_COLUMN: changeFrozenColumn,
    REGISTER_COLUMN: registerColumn,
    TOGGLE_COLUMN_VISIBILITY: toggleColumnVisibility,
} = columnsSlice.actions

export const setColumnVisible = (widgetId: string, columnId: string) => changeColumnVisibility(widgetId, columnId, true)
export const setColumnHidden = (widgetId: string, columnId: string) => changeColumnVisibility(widgetId, columnId, false)
