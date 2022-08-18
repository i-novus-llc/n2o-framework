import { createSlice } from '@reduxjs/toolkit'
import { isEmpty, omit } from 'lodash'

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

        addComponent: {
            prepare(dataSourceId, componentId) {
                return ({
                    payload: { dataSourceId, componentId },
                })
            },

            reducer(state, action) {
                const { dataSourceId, componentId } = action.payload

                const datasource = state[dataSourceId] || DataSource.defaultState // fixme добавление виджета не должно быть до его регистрации

                if (datasource.components.includes(componentId)) { return }

                state[dataSourceId] = {
                    ...datasource,
                    components: [...datasource.components, componentId],
                }
            },
        },

        removeComponent: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(dataSourceId, componentId) {
                return ({
                    payload: { dataSourceId, componentId },
                })
            },

            reducer(state, action) {
                const { dataSourceId, componentId } = action.payload

                const datasource = state[dataSourceId]

                state[dataSourceId] = {
                    ...datasource,
                    components: datasource.components.filter(idFromDataSource => idFromDataSource !== componentId),
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
            prepare(id, fields, prefix = MODEL_PREFIX.active, meta = {}) {
                return ({
                    payload: { id, prefix, fields },
                    meta,
                })
            },
            // eslint-disable-next-line no-unused-vars
            reducer(state, action) {
                // empty reducer, action for saga
            },
        },

        failValidate: {
            prepare(id, fields, prefix = MODEL_PREFIX.active, meta) {
                return ({
                    payload: { id, fields, prefix },
                    meta,
                })
            },
            // eslint-disable-next-line no-unused-vars
            reducer(state, action) {
                const { id, fields, prefix } = action.payload
                const datasource = state[id]

                datasource.errors[prefix] = {
                    ...(datasource.errors[prefix] || {}),
                    ...fields,
                }
            },
        },

        resetValidation: {
            prepare(id, fields, prefix = MODEL_PREFIX.active) {
                return ({
                    payload: { id, fields, prefix },
                })
            },
            reducer(state, action) {
                const { id, fields = [], prefix } = action.payload
                const datasource = state[id]

                datasource.errors[prefix] = isEmpty(fields)
                    ? {}
                    : omit(datasource.errors[prefix], fields)
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
        setEditModel: {
            prepare(id, model) {
                return ({
                    payload: { id, model, prefix: MODEL_PREFIX.edit },
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

        setFieldSubmit: {
            prepare(id, field, dataProvider) {
                return ({
                    payload: { id, field, dataProvider },
                })
            },
            reducer(state, action) {
                const { id, field, dataProvider } = action.payload
                const datasource = state[id]

                if (!datasource.fieldsSubmit) {
                    datasource.fieldsSubmit = {}
                }

                datasource.fieldsSubmit[field] = dataProvider
            },
        },
    },
})

export default datasource.reducer

// Actions
export const {
    register,
    remove,
    DATA_REQUEST: dataRequest,
    resolveRequest,
    rejectRequest,
    setActiveModel,
    setEditModel,
    setFilter,
    setSourceModel,
    setMultiModel,
    setSorting,
    startValidate,
    failValidate,
    resetValidation,
    changePage,
    changeCount,
    changeSize,
    addComponent,
    removeComponent,
    setFieldSubmit,
} = datasource.actions
