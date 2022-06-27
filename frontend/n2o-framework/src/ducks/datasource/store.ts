import { createSlice } from '@reduxjs/toolkit'

import { ModelPrefix, SortDirection } from '../../core/datasource/const'

import type {
    AddComponentAction,
    ChangeCountAction,
    ChangePageAction,
    ChangeSizeAction,
    DataRequestAction,
    FailRequestAction,
    FailValidateAction,
    RegisterAction,
    RemoveAction,
    RemoveComponentAction,
    ResolveRequestAction,
    SetFieldSubmitAction,
    SetModelAction,
    SetSortDirectionAction,
    StartValidateAction,
} from './Actions'
import type { DataSourceState } from './DataSource'
import { DataSource } from './DataSource'
import type { IProvider, QueryResult } from './Provider'
import { ProviderType } from './Provider'

const initialState: Record<string, DataSourceState> = {}

const datasource = createSlice({
    name: 'n2o/datasource',
    initialState,
    reducers: {
        register: {
            prepare(id: string, initProps: DataSourceState) {
                return ({
                    type: '',
                    payload: { id, initProps },
                })
            },
            reducer(state, action: RegisterAction) {
                const { id, initProps } = action.payload
                const { provider } = initProps
                const datasource = {
                    ...DataSource.defaultState,
                    ...initProps,
                }

                if (provider && !provider.type) {
                    provider.type = ProviderType.service
                }

                state[id] = datasource
            },
        },

        remove: {
            prepare(id: string) {
                return ({
                    payload: { id },
                })
            },
            reducer(state, action: RemoveAction) {
                delete state[action.payload.id]
            },
        },

        addComponent: {
            prepare(id: string, componentId: string) {
                return ({
                    payload: { id, componentId },
                })
            },

            reducer(state, action: AddComponentAction) {
                const { id, componentId } = action.payload

                const datasource = state[id] || DataSource.defaultState // fixme добавление виджета не должно быть до его регистрации

                if (datasource.components.includes(componentId)) { return }

                state[id] = {
                    ...datasource,
                    components: [...datasource.components, componentId],
                }
            },
        },

        removeComponent: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(id: string, componentId: string) {
                return ({
                    payload: { id, componentId },
                })
            },

            reducer(state, action: RemoveComponentAction) {
                const { id, componentId } = action.payload

                const datasource = state[id]

                state[id] = {
                    ...datasource,
                    components: datasource.components.filter(idFromDataSource => idFromDataSource !== componentId),
                }
            },
        },

        dataRequest: {
            prepare(id: string, options = {}) {
                return ({
                    payload: { id, options },
                })
            },
            reducer(state, action: DataRequestAction) {
                const { id } = action.payload

                if (!state[id]) {
                    // eslint-disable-next-line no-console
                    console.warn(`Attempt to request data from a non-existent source: ${id}`)

                    return
                }

                state[id].loading = true
            },
        },

        resolveRequest: {
            prepare(id: string, json: QueryResult) {
                return ({
                    payload: { id, query: json },
                    meta: json.meta,
                })
            },
            reducer(state, action: ResolveRequestAction) {
                const { id, query } = action.payload

                state[id].loading = false
                state[id].page = query.page
                state[id].count = query.count
            },
        },

        rejectRequest: {
            prepare(id: string, err?, meta?) {
                return ({
                    payload: { id, err },
                    meta,
                })
            },
            reducer(state, action: FailRequestAction) {
                const { id } = action.payload

                if (!state[id]) { return }

                state[id].loading = false
            },
        },

        setSorting: {
            prepare(id: string, field: string, direction: SortDirection) {
                return ({
                    payload: { id, field, direction },
                })
            },
            reducer(state, action: SetSortDirectionAction) {
                const { id, field, direction } = action.payload

                if (direction === SortDirection.none) {
                    state[id].sorting = {}
                } else {
                    state[id].sorting = { [field]: direction }
                }
            },
        },

        changePage: {
            prepare(id: string, page: number) {
                return ({
                    payload: { id, page },
                })
            },
            reducer(state, action: ChangePageAction) {
                const { id, page } = action.payload

                state[id].page = page
            },
        },

        changeCount: {
            prepare(id: string, count: number) {
                return ({
                    payload: { id, count },
                })
            },
            reducer(state, action: ChangeCountAction) {
                const { id, count } = action.payload

                state[id].count = count
            },
        },
        changeSize: {
            prepare(id: string, size: number) {
                return ({
                    payload: { id, size },
                })
            },
            reducer(state, action: ChangeSizeAction) {
                const { id, size } = action.payload

                state[id].size = size
            },
        },

        startValidate: {
            prepare(id: string, fields?: string[], prefix = ModelPrefix.active, meta = {}) {
                return ({
                    payload: { id, prefix, fields },
                    meta,
                })
            },
            reducer(state, action: StartValidateAction) {
                const { id, fields, prefix } = action.payload
                const { errors, validation } = state[id]
                const fieldList = fields?.length ? fields : Object.keys(validation || {})

                fieldList.forEach((field) => { errors[prefix][field] = undefined })
            },
        },

        failValidate: {
            prepare(id, fields, prefix = ModelPrefix.active, meta = {}) {
                return ({
                    payload: { id, fields, prefix },
                    meta,
                })
            },
            reducer(state, action: FailValidateAction) {
                const { id, fields, prefix } = action.payload
                const datasource = state[id]

                datasource.errors[prefix] = {
                    ...datasource.errors[prefix],
                    ...fields,
                }
            },
        },
        setActiveModel: {
            prepare(id: string, model: object) {
                return ({
                    payload: { id, model, prefix: ModelPrefix.active },
                })
            },
            // eslint-disable-next-line no-unused-vars,  @typescript-eslint/no-unused-vars
            reducer(state, action: SetModelAction) {
                // nothing
            },
        },
        setEditModel: {
            prepare(id: string, model: object) {
                return ({
                    payload: { id, model, prefix: ModelPrefix.edit },
                })
            },
            // eslint-disable-next-line no-unused-vars,  @typescript-eslint/no-unused-vars
            reducer(state, action: SetModelAction) {
                // nothing
            },
        },
        setFilter: {
            prepare(id: string, model: object) {
                return ({
                    payload: { id, model, prefix: ModelPrefix.filter },
                })
            },
            // eslint-disable-next-line no-unused-vars,  @typescript-eslint/no-unused-vars
            reducer(state, action: SetModelAction) {
                // nothing
            },
        },
        setSourceModel: {
            prepare(id: string, model: object[]) {
                return ({
                    payload: { id, model, prefix: ModelPrefix.source },
                })
            },
            // eslint-disable-next-line no-unused-vars,  @typescript-eslint/no-unused-vars
            reducer(state, action: SetModelAction<object[]>) {
                // nothing
            },
        },
        setMultiModel: {
            prepare(id: string, model: object[]) {
                return ({
                    payload: { id, model, prefix: ModelPrefix.selected },
                })
            },
            // eslint-disable-next-line no-unused-vars,  @typescript-eslint/no-unused-vars
            reducer(state, action: SetModelAction<object[]>) {
                // nothing
            },
        },

        setFieldSubmit: {
            prepare(id: string, field: string, provider: IProvider) {
                return ({
                    payload: { id, field, provider },
                })
            },
            reducer(state, action: SetFieldSubmitAction) {
                const { id, field, provider } = action.payload
                const datasource = state[id]

                if (!datasource.fieldsSubmit) {
                    datasource.fieldsSubmit = {}
                }
                if (!provider.type) { provider.type = ProviderType.service }

                datasource.fieldsSubmit[field] = provider
            },
        },
    },
})

export default datasource.reducer

// Actions
export const {
    register,
    remove,
    dataRequest,
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
    changePage,
    changeCount,
    changeSize,
    addComponent,
    removeComponent,
    setFieldSubmit,
} = datasource.actions
