import { createSlice } from '@reduxjs/toolkit'
import { isEmpty, omit } from 'lodash'
import merge from 'deepmerge'

import { ModelPrefix, SortDirection } from '../../core/datasource/const'
import { IMeta } from '../../sagas/types'
import { ValidationsKey } from '../../core/validation/IValidation'
import { Meta } from '../Action'
import { removeFieldFromArray } from '../models/store'
import { RemoveFieldFromArrayAction } from '../models/Actions'

import type {
    AddComponentAction,
    ChangePageAction,
    ChangeSizeAction,
    DataRequestAction,
    FailRequestAction,
    FailValidateAction,
    ResetDatasourceAction,
    RegisterAction,
    RemoveAction,
    RemoveComponentAction,
    ResolveRequestAction,
    SetAdditionalInfoAction,
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

                const datasource = { ...merge(DataSource.defaultState, initProps), provider }

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

                // После закрытия оверлея удаление компонента изds может прилететь позже удаления самого ds
                if (datasource) {
                    state[id] = {
                        ...datasource,
                        components: datasource.components.filter(idFromDataSource => idFromDataSource !== componentId),
                    }
                }
            },
        },

        dataRequest: {
            prepare(id: string, options: Partial<{ size: number, page: number }> = {}, meta: Meta = {}) {
                return ({
                    payload: { id, options },
                    meta,
                })
            },
            reducer(state, action: DataRequestAction) {
                const { id } = action.payload

                if (!state[id]) {
                    // eslint-disable-next-line no-console
                    console.warn(`Attempt to request data from a non-existent source: ${id}`)

                    return
                }

                delete state[id].error

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
                state[id].paging = {
                    ...state[id].paging,
                    ...query.paging,
                }
            },
        },

        rejectRequest: {
            prepare(id: string, error?, meta?) {
                return ({
                    payload: { id, error },
                    meta,
                })
            },
            reducer(state, action: FailRequestAction) {
                const { id, error } = action.payload

                if (!state[id]) { return }

                state[id].loading = false
                state[id].error = error
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

        setAdditionalInfo: {
            prepare(id: string, additionalInfo: object) {
                return ({
                    payload: {
                        id,
                        additionalInfo,
                    },
                })
            },

            reducer(state, action: SetAdditionalInfoAction) {
                const { id, additionalInfo } = action.payload

                state[id].additionalInfo = additionalInfo
            },
        },

        changePage: {
            prepare(id: string, page: number, withCount: boolean) {
                return ({
                    payload: { id, page, withCount },
                })
            },
            reducer(state, action: ChangePageAction) {
                const { id, page } = action.payload

                state[id].paging.page = page
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

                state[id].paging.size = size
            },
        },

        startValidate: {
            prepare(
                id: string,
                validationsKey = ValidationsKey.Validations,
                prefix = ModelPrefix.active,
                fields?: [],
                meta = {},
            ) {
                return ({
                    payload: { id, validationsKey, prefix, fields },
                    meta,
                })
            },
            reducer(state, action: StartValidateAction) {
                // empty reducer, action for saga
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

        reset: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(id: string) {
                return ({
                    payload: { id },
                })
            },

            reducer(state, action: ResetDatasourceAction) {
                const { id } = action.payload
                const datasource = state[id]

                // reset pagination to default
                datasource.paging.page = 1
                datasource.paging.count = 0

                // reset all errors
                Object.values(ModelPrefix).forEach((prefix) => {
                    const model = datasource.errors[prefix]

                    if (model) {
                        datasource.errors[prefix] = {}
                    }
                })
            },
        },

        resetValidation: {
            // @ts-ignore поправить типы
            prepare(id, fields, prefix = ModelPrefix.active) {
                return ({
                    payload: { id, fields, prefix },
                })
            },
            reducer(state, action: StartValidateAction) {
                const { id, fields = [], prefix } = action.payload
                const datasource = state[id]

                if (datasource) {
                    datasource.errors[prefix] = isEmpty(fields)
                        ? {}
                        : omit(datasource.errors[prefix], fields)
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
            prepare(datasource: string, options = {}, meta: Meta = {}) {
                return ({
                    payload: { datasource, options },
                    meta,
                })
            },
            reducer(state, action: { payload: { datasource: string, options: unknown } }) {
                // экшн приходит с сервера. в сагах надо сделать просто ремап
            },
        },

        submit: {
            prepare(id: string, provider?, meta = {}) {
                return ({
                    payload: { id, provider },
                    meta,
                })
            },
            reducer(state, action: SubmitAction) {
                // empty reducer, action for saga
            },
        },

        submitSuccess: {
            prepare(meta?: IMeta) {
                return ({
                    payload: {},
                    meta,
                })
            },
            reducer() {
                // empty reducer, action for saga
            },
        },

        submitFail: {
            prepare(error: unknown, meta?: IMeta) {
                return ({
                    payload: error,
                    meta,
                })
            },
            reducer() {
                // empty reducer, action for saga
            },
        },
    },
    extraReducers: {
        [removeFieldFromArray.type](state, action: RemoveFieldFromArrayAction) {
            const { key, field, start, end = 1, prefix } = action.payload
            const datasource = state[key]
            const errors: Partial<typeof datasource.errors[typeof prefix]> = {}

            const mask = new RegExp(`${field}\\[(\\d+)]\\.(.+)`)

            for (const [key, messages] of Object.entries(datasource.errors[prefix] || {})) {
                const match = key.match(mask)

                if (match) {
                    const [, i, name] = match
                    const matchedIndex = Number(i)

                    // index before removed elements
                    if (matchedIndex < start) {
                        errors[key] = messages

                        // eslint-disable-next-line no-continue
                        continue
                    }

                    // removed elements: ignore it
                    if ((matchedIndex >= start) && (matchedIndex < start + end)) {
                        // eslint-disable-next-line no-continue
                        continue
                    }

                    // after removed: shift index
                    const newIndex = matchedIndex - end

                    errors[`${field}[${newIndex}].${name}`] = messages
                } else {
                    // not multi-set fields
                    errors[key] = messages
                }
            }

            datasource.errors[prefix] = errors
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
    setAdditionalInfo,
    startValidate,
    resetValidation,
    reset,
    failValidate,
    changePage,
    changeSize,
    addComponent,
    removeComponent,
    setFieldSubmit,
    DATA_REQUEST,
    submit,
    submitSuccess,
    submitFail,
} = datasource.actions
