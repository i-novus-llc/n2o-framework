import { createSlice, current } from '@reduxjs/toolkit'
import set from 'lodash/set'
import get from 'lodash/get'
import merge from 'lodash/merge'

import { reorderElement } from '../utils'

import {
    ChangeTableColumnParam,
    ChangeTableParam,
    RegisterTable,
    ReorderTableColumn,
    SwitchTableColumnParam, UpdateTableParams,
} from './Actions'
import { getDefaultTableState } from './constants'
import { type State } from './Table'

export const initialState: State = {}

export const tableSlice = createSlice({
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

        changeTableColumnParam: {
            prepare(widgetId, id, paramKey, value, parentId) {
                return ({
                    payload: { widgetId, id, paramKey, value, parentId },
                })
            },

            reducer(state, action: ChangeTableColumnParam) {
                const { widgetId, id, paramKey, value, parentId } = action.payload

                if (!state[widgetId]) {
                    // eslint-disable-next-line no-console
                    console.warn(`Виджет ${widgetId} не существует`)

                    return
                }

                const { header } = state[widgetId]
                const { cells } = header || {}

                if (!cells) {
                    // eslint-disable-next-line no-console
                    console.warn(`Виджет ${widgetId} не существует`)

                    return
                }

                if (parentId) {
                    const parentIndex = cells.findIndex(({ id }) => id === parentId)
                    const parentHeader = cells[parentIndex]
                    const { children = [] } = parentHeader || {}

                    const targetChildren = children.find(({ id: childrenId }) => childrenId === id)

                    if (targetChildren) {
                        set(targetChildren, paramKey, value)
                    }

                    return
                }

                const targetIndex = cells.findIndex(({ id: currentId }) => currentId === id)
                const targetHeader = cells[targetIndex]

                set(targetHeader, paramKey, value)
            },
        },

        reorderColumn: {
            prepare(widgetId, headerId, reorderColumnId, targetColumnId) {
                return { payload: { widgetId, headerId, reorderColumnId, targetColumnId } }
            },

            reducer(state, action: ReorderTableColumn) {
                const { reorderColumnId, targetColumnId } = action.payload

                if (reorderColumnId === targetColumnId) { return }

                const { widgetId, headerId } = action.payload

                const { header, body } = state[widgetId]

                if (!header || !body) { return }

                const targetHeaderCell = header?.cells.find(({ id }) => id === headerId)

                if (!targetHeaderCell?.children) { return }

                targetHeaderCell.children = reorderElement(targetHeaderCell.children, reorderColumnId, targetColumnId)
                body.cells = reorderElement(body.cells, reorderColumnId, targetColumnId)
            },
        },

        updateTableParams: {
            prepare(widgetId, params) {
                return { payload: { widgetId, params } }
            },

            reducer(state, action: UpdateTableParams) {
                const { widgetId, params } = action.payload

                state[widgetId] = { ...getDefaultTableState(), ...merge(state[widgetId], params) }
            },
        },
    },
})

export default tableSlice.reducer

export const {
    registerTable,
    changeTableParam,
    switchTableParam,
    changeTableColumnParam,
    reorderColumn,
    updateTableParams,
} = tableSlice.actions
