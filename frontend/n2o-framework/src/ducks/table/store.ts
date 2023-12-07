import { createSlice } from '@reduxjs/toolkit'
import set from 'lodash/set'
import get from 'lodash/get'
import merge from 'lodash/merge'

import {
    ChangeTableColumnParam,
    ChangeTableParam,
    RegisterTable,
    RegisterTableColumn,
    SwitchTableColumnParam,
} from './Actions'
import { getDefaultColumnState, getDefaultTableState, initialState } from './constants'

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

                state[widgetId] = { ...getDefaultTableState(), ...merge(currentState, initProps) }
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
                    state[widgetId] = getDefaultTableState()
                }

                const { columns } = state[widgetId]

                columns[columnId] = { ...getDefaultColumnState(), ...action.payload }
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

                if (!state[widgetId]) {
                    // eslint-disable-next-line no-console
                    console.warn(`Виджет ${widgetId} не существует`)

                    return
                }

                const column = state[widgetId].columns[columnId]

                set(column, paramKey, value)
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
