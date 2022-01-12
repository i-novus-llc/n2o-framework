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
                const datasource = state[id] || DataSource.defaultState // fixme добавление виджета не должно быть до его регистрации

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

        DATA_REQUEST: {
            prepare(datasource, options = {}) {
                return ({
                    payload: { datasource, options },
                })
            },
            reducer(state, action) {
                const { datasource } = action.payload

                if (!state[datasource]) {
                    // eslint-disable-next-line no-console
                    console.warn(`Attempt to request data from a non-existent source: ${datasource}`)

                    return
                }

                state[datasource].loading = true
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

                if (!state[id]) { return }

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
        changeSize: {
            prepare(id, size) {
                return ({
                    payload: { id, size },
                })
            },
            reducer(state, action) {
                const { id, size } = action.payload

                state[id].size = size
            },
        },

        startValidate: {
            prepare(id, fields, prefix = MODEL_PREFIX.active) {
                return ({
                    payload: { id, prefix, fields },
                })
            },
            reducer(state, action) {
                const { id, fields } = action.payload
                const datasource = state[id]
                const fieldList = fields?.length ? fields : Object.keys(datasource.validation || {})

                datasource.errors = datasource.errors || {}

                fieldList.forEach((field) => { datasource.errors[field] = undefined })
            },
        },
        failValidate: {
            prepare(id, fields, meta /* , prefix = MODEL_PREFIX.active*/) {
                return ({
                    payload: { id, fields },
                    meta,
                })
            },
            // eslint-disable-next-line no-unused-vars
            reducer(state, action) {
                const { id, fields } = action.payload
                const datasource = state[id]

                datasource.errors = {
                    ...(datasource.errors || {}),
                    ...fields,
                }
            },
        },
        setActiveModel: {
            prepare(id, model) {
                return ({
                    payload: { id, model, prefix: MODEL_PREFIX.active },
                })
            },
            // eslint-disable-next-line no-unused-vars
            reducer(state, action) {
                // nothing
            },
        },
        setFilter: {
            prepare(id, model) {
                return ({
                    payload: { id, model, prefix: MODEL_PREFIX.filter },
                })
            },
            // eslint-disable-next-line no-unused-vars
            reducer(state, action) {
                // nothing
            },
        },
        setSourceModel: {
            prepare(id, model) {
                return ({
                    payload: { id, model, prefix: MODEL_PREFIX.source },
                })
            },
            // eslint-disable-next-line no-unused-vars
            reducer(state, action) {
                // nothing
            },
        },
        setMultiModel: {
            prepare(id, model) {
                return ({
                    payload: { id, model, prefix: MODEL_PREFIX.selected },
                })
            },
            // eslint-disable-next-line no-unused-vars
            reducer(state, action) {
                // nothing
            },
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
    DATA_REQUEST: dataRequest,
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
    changeSize,
} = datasource.actions
