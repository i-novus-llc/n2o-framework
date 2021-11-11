import { createSlice } from '@reduxjs/toolkit'

import { MODEL_PREFIX, SORT_DIRECTION } from '../../core/datasource/const'

import { DataSource } from './DataSource'

const datasource = createSlice({
    name: 'n2o/datasource',
    initialState: {},
    reducers: {
        register: {
            prepare(id, initProps) {
                return ({
                    payload: { id, initProps },
                })
            },
            reducer(state, action) {
                const { id, initProps } = action.payload

                state[id] = {
                    ...DataSource.defaultState,
                    ...initProps,
                }
            },
        },
        remove: {
            prepare(id) {
                return ({
                    payload: { id },
                })
            },
            reducer(state, action) {
                delete state[action.payload.id]
            },
        },

        addWidget: {
            prepare(id, widgetId) {
                return ({
                    payload: { widgetId, id },
                })
            },
            reducer(state, action) {
                const { widgetId, id } = action.payload
                const datasource = state[id]

                if (datasource.widgets.includes(widgetId)) {
                    return
                }

                state[id] = {
                    ...datasource,
                    widgets: [...datasource.widgets, widgetId],
                }
            },
        },
        removeWidget: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(id, widgetId) {
                return ({
                    payload: { widgetId, id },
                })
            },
            reducer(state, action) {
                const { widgetId, id } = action.payload
                const datasource = state[id]

                state[id] = {
                    ...datasource,
                    widgets: datasource.widgets.filter(widget => widget !== widgetId),
                }
            },
        },

        dataRequest: {
            prepare(id, options = {}) {
                return ({
                    payload: { id, options },
                })
            },
            reducer(state, action) {
                const { id } = action.payload

                state[id].loading = true
            },
        },
        resolveRequest: {
            prepare(id, json) {
                return ({
                    payload: { id, query: json },
                    meta: json.meta,
                })
            },
            reducer(state, action) {
                const { id } = action.payload

                state[id].loading = false
            },
        },
        rejectRequest: {
            prepare(id, err, meta) {
                return ({
                    payload: { id, err },
                    meta,
                })
            },
            // eslint-disable-next-line sonarjs/no-identical-functions
            reducer(state, action) {
                const { id } = action.payload

                state[id].loading = false
            },
        },

        setSorting: {
            prepare(id, field, direction) {
                return ({
                    payload: { id, field, direction },
                })
            },
            reducer(state, action) {
                const { id, field, direction } = action.payload

                if (direction === SORT_DIRECTION.NONE) {
                    state[id].sorting = {}
                } else {
                    state[id].sorting = { [field]: direction }
                }
            },
        },
        changePage: {
            prepare(id, page) {
                return ({
                    payload: { id, page },
                })
            },
            reducer(state, action) {
                const { id, page } = action.payload

                state[id].page = page
            },
        },
        changeCount: {
            prepare(id, count) {
                return ({
                    payload: { id, count },
                })
            },
            reducer(state, action) {
                const { id, count } = action.payload

                state[id].count = count
            },
        },

        startValidate(id, prefix = MODEL_PREFIX.active) {
            return ({
                payload: { id, prefix },
            })
        },
        failValidate(id, fields /* , prefix = MODEL_PREFIX.active*/) {
            return ({
                payload: { id, fields },
            })
        },
        setActiveModel(id, model) {
            return ({
                payload: { id, model, prefix: MODEL_PREFIX.active },
            })
        },
        setFilter(id, model) {
            return ({
                payload: { id, model, prefix: MODEL_PREFIX.filter },
            })
        },
        setSourceModel(id, model) {
            return ({
                payload: { id, model, prefix: MODEL_PREFIX.source },
            })
        },
        setMultiModel(id, model) {
            return ({
                payload: { id, model, prefix: MODEL_PREFIX.selected },
            })
        },
    },
})

export default datasource.reducer

// Actions
export const {
    register,
    remove,
    addWidget,
    removeWidget,
    dataRequest,
    resolveRequest,
    rejectRequest,
    setActiveModel,
    setFilter,
    setSourceModel,
    setMultiModel,
    setSorting,
    startValidate,
    failValidate,
    changePage,
    changeCount,
} = datasource.actions
