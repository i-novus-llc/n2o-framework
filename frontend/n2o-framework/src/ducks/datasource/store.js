import { createSlice, current } from '@reduxjs/toolkit'
import get from 'lodash/get'

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

                /* FIXME for debugging, remove and return
                 *   state[id] = {
                    ...DataSource.defaultState,
                    ...initProps,}
                 */
                const components = get(current(state), `${id}.components`, [])

                if (components.length) {
                    state[id] = {
                        ...state[id],
                        ...initProps,
                        components,
                    }
                } else {
                    state[id] = {
                        ...DataSource.defaultState,
                        ...initProps,
                    }
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
            prepare(id, prefix = MODEL_PREFIX.active) {
                return ({
                    payload: { id, prefix },
                })
            },
            // eslint-disable-next-line no-unused-vars
            reducer(state, action) {
                // nothing
            },
        },
        failValidate: {
            prepare(id, fields /* , prefix = MODEL_PREFIX.active*/) {
                return ({
                    payload: { id, fields },
                })
            },
            // eslint-disable-next-line no-unused-vars
            reducer(state, action) {
                // nothing
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
    addComponent,
    removeComponent,
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
    changeSize,
} = datasource.actions
