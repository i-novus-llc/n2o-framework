import { createSlice } from '@reduxjs/toolkit'
import set from 'lodash/set'
import get from 'lodash/get'
import merge from 'lodash/merge'

import {
    RegisterTable,
    ChangeTableColumnParam,
    RegisterTableColumn,
    ChangeTableParam,
    SwitchTableColumnParam,
} from './Actions'
import { initialState, defaultTableState, defaultColumnState } from './constants'

const tableSlice = createSlice({
    name: 'n2o/table',
    initialState,
    reducers: {
        registerTable: {
            prepare(widgetId, initProps) {
                return ({
                    payload: { widgetId, initProps },
                })
            },

            reducer(state, action: RegisterTable) {
                const { widgetId, initProps } = action.payload
                const currentState = state[widgetId] || {}

                state[widgetId] = {
                    ...defaultTableState,
                    ...merge(currentState, initProps),
                }
            },
        },

        changeTableParam: {
            prepare(widgetId, paramKey, value) {
                return ({
                    payload: { widgetId, paramKey, value },
                })
            },

            reducer(state, action: ChangeTableParam) {
                const { widgetId, paramKey, value } = action.payload

                set(state, `${widgetId}.${paramKey}`, value)
            },
        },

        switchTableParam: {
            prepare(widgetId, paramKey) {
                return ({
                    payload: { widgetId, paramKey },
                })
            },

            reducer(state, action: SwitchTableColumnParam) {
                const { widgetId, paramKey } = action.payload

                const value = get(state, `${widgetId}.${paramKey}`)

                set(state, `${widgetId}.${paramKey}`, !value)
            },
        },

        registerTableColumn: {
            prepare(
                widgetId,
                columnId,
                label,
                visible,
                disabled,
                conditions,
            ) {
                return ({
                    payload: {
                        widgetId,
                        columnId,
                        label,
                        visible,
                        disabled,
                        conditions,
                    },
                })
            },

            reducer(state, action: RegisterTableColumn) {
                const { widgetId, columnId } = action.payload

                if (!state[widgetId]) {
                    state[widgetId] = defaultTableState
                }

                set(state, `${widgetId}.columns.${columnId}`, { ...defaultColumnState, ...action.payload })
            },
        },

        changeTableColumnParam: {
            prepare(widgetId, columnId, paramKey, value) {
                return ({
                    payload: { widgetId, columnId, paramKey, value },
                })
            },

            reducer(state, action: ChangeTableColumnParam) {
                const { widgetId, columnId, paramKey, value } = action.payload

                set(state, `${widgetId}.columns.${columnId}.${paramKey}`, value)
            },
        },
    },
})

export default tableSlice.reducer

export const {
    registerTable,
    changeTableParam,
    switchTableParam,
    registerTableColumn,
    changeTableColumnParam,
} = tableSlice.actions
