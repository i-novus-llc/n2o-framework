import { createSlice } from '@reduxjs/toolkit'

import { ModelPrefix, SortDirection } from '../../core/datasource/const'

import type {
    AddComponentAction,
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
    SetSortDirectionAction,
    StartValidateAction,
    SubmitAction,
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
                const { provider: propsProvider } = initProps
                let provider

                if (propsProvider) {
                    provider = {
                        ...propsProvider,
                        type: propsProvider.type || ProviderType.service,
                    }
                }

                const datasource = {
                    ...DataSource.defaultState,
                    ...initProps,
                    provider,
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
                const { errors, validations } = state[id]
                const fieldList = fields?.length ? fields : Object.keys(validations || {})

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

        setFieldSubmit: {
            prepare(id: string, field: string, provider: IProvider) {
                return ({
                    payload: { id, field, provider },
                })
            },
            reducer(state, action: SetFieldSubmitAction) {
                const { id, field, provider: propsProvider } = action.payload
                const datasource = state[id]

                datasource.fieldsSubmit[field] = {
                    ...propsProvider,
                    type: propsProvider.type || ProviderType.service,
                }
            },
        },
        // eslint-disable-next-line @typescript-eslint/naming-convention
        DATA_REQUEST: {
            prepare(datasource: string, options = {}) {
                return ({
                    payload: { datasource, options },
                })
            },
            reducer(state, action: { payload: { datasource: string, options: unknown } }) {
                // экшн приходит с сервера. в сагах надо сделать просто ремап
            },
        },

        submit: {
            prepare(id: string, provider, meta = {}) {
                return ({
                    payload: { id, provider },
                    meta,
                })
            },
            reducer(state, action: SubmitAction) {
                // empty reducer, action for saga
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
    setSorting,
    startValidate,
    failValidate,
    changePage,
    changeSize,
    addComponent,
    removeComponent,
    setFieldSubmit,
    DATA_REQUEST,
    submit,
} = datasource.actions
