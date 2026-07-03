import { createSlice } from '@reduxjs/toolkit'
import isEmpty from 'lodash/isEmpty'
import omit from 'lodash/omit'
import set from 'lodash/set'
import get from 'lodash/get'

import { ModelPrefix, SortDirection } from '../../core/datasource/const'
import { Meta } from '../../sagas/types'
import { INDEX_MASK, INDEX_REGEXP } from '../../core/validation/const'
import { Validation } from '../../core/validation/types'
import { Meta as N2OMeta } from '../Action'
import { appendToArray, removeFromArray } from '../models/store'
import { AppendToArrayAction, RemoveFromArrayAction } from '../models/Actions'
import { getOnAppend, getOnRemove, mapMultiFields } from '../../core/models/mapMultiFields'
import { logger } from '../../utils/logger'
import { DataProviderError } from '../../core/dataProviderResolver'
import { ModelLink } from '../../core/models/types'

import type {
    ChangePageAction,
    ChangeSizeAction,
    DataRequestAction,
    ValidateEndAction,
    ValidateEndPayload,
    FailRequestAction,
    ResetDatasourceAction,
    RegisterAction,
    RemoveAction,
    ResolveRequestAction,
    SetFieldSubmitAction,
    SetSortDirectionAction,
    StartValidateAction,
    SubmitAction,
    UpdatePagingAction,
} from './Actions'
import type { DataSourceState, DataSourceConfig } from './DataSource'
import { DataSource } from './DataSource'
import {
    Provider,
    QueryResult,
    ProviderType,
    SubmitProvider,
    Paging,
} from './Provider'

export const initialState: Record<string, DataSourceState> = {}

const prepareValidations = (
    record: DataSourceConfig['validations'][ModelPrefix],
): DataSourceState['validations'][ModelPrefix] => {
    if (!record) { return record }

    return Object.fromEntries(Object.entries(record).map(([key, validations]) => [key, validations.map(v => ({
        ...v,
        on: v.on?.map((key) => {
            if (key instanceof RegExp) { return key }

            const mask = key
                .replaceAll(INDEX_REGEXP, INDEX_MASK)
                .replaceAll(/\./ig, '\\.')

            return new RegExp(`^${mask}(\\[.+|\\..+)?$`)
        }) ?? [],
    }))]))
}

const mapProps = <T extends Partial<DataSourceConfig>>(initProps: T): T & Pick<DataSourceState, 'validations'> => {
    return {
        ...initProps,
        validations: Object.fromEntries(
            Object.entries(initProps.validations || {})
                .map(([modelPrefix, validations]) => [modelPrefix, prepareValidations(validations)]),
        ),
    }
}

export const datasource = createSlice({
    name: 'n2o/datasource',
    initialState,
    reducers: {
        register: {
            prepare(id: string, initProps: DataSourceConfig) {
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

                const defaultState = state[id] || DataSource.defaultState
                const mappedProps = mapProps(initProps)

                // TODO временно из за фичи восстановления пагинации по хешу api/mapParams
                // Не приходит page в paging, из за отсутствия page где то дальше ломается
                const paging = { ...defaultState.paging, ...mappedProps.paging }

                const props = { ...defaultState, ...mappedProps, paging }

                const datasource = { ...props, provider }

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

        dataRequest: {
            prepare(id: string, options: Partial<{ size: number, page: number }> = {}, meta: N2OMeta = {}) {
                return ({
                    payload: { id, options },
                    meta,
                })
            },
            reducer(state, action: DataRequestAction) {
                const { id } = action.payload

                if (!state[id]) {
                    logger.warn(`Attempt to request data from a non-existent source: ${id}`)

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
                state[id].additionalInfo = query.additionalInfo
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

                if (error && error instanceof DataProviderError) {
                    state[id].paging = {
                        ...state[id].paging,
                        page: 1,
                        count: 0,
                    }
                }
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

                if (!state[id]) {
                    state[id] = DataSource.defaultState
                }

                if (direction === SortDirection.none) {
                    state[id].sorting = {}
                } else {
                    state[id].sorting = { [field]: direction }
                }
            },
        },

        updatePaging: {
            prepare(id: string, paging: Partial<Paging>) {
                return {
                    payload: { id, paging },
                }
            },
            reducer(state, { payload }: UpdatePagingAction) {
                const { id, paging } = payload

                if (!state[id]) {
                    state[id] = DataSource.defaultState
                }

                state[id].paging = {
                    ...state[id].paging,
                    ...paging,
                }
            },
        },

        changePage: {
            prepare(id: string, page: number, options?: object) {
                return ({
                    payload: { id, page, options },
                })
            },
            reducer(state, action: ChangePageAction) {
                const { id, page } = action.payload

                state[id].paging.page = page
                delete state[id].errors[ModelPrefix.source]
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
                modelLink: ModelLink,
                fields?: Record<string, Validation[]>,
                meta = {},
            ) {
                return ({
                    payload: { modelLink, fields },
                    meta,
                })
            },
            reducer(state, action: StartValidateAction) {
                // empty reducer, action for saga
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
                datasource.errors = {}
            },
        },

        endValidation: {
            prepare(payload: ValidateEndPayload, meta = {}) { return ({ payload, meta }) },
            reducer(state, action: ValidateEndAction) {
                const { modelLink, fields, messages } = action.payload
                const { id, prefix, index } = modelLink
                const datasource = state[id]

                if (!datasource) { return }
                if (prefix === ModelPrefix.selected) { return }

                // Хак, чтобы не дублировать валидацию для форм с двумя моделями
                // eslint-disable-next-line no-nested-ternary
                const modelPrefix = prefix === ModelPrefix.edit
                    ? datasource.validations[ModelPrefix.filter]
                        ? ModelPrefix.filter
                        : ModelPrefix.active
                    : prefix

                const path = modelPrefix === ModelPrefix.source
                    ? `${modelPrefix}[${index}]`
                    : modelPrefix

                if (!fields || isEmpty(fields)) {
                    set(datasource.errors, path, messages)

                    return
                }

                // TODO merge arrays
                set(datasource.errors, path, {
                    ...omit(get(datasource.errors, path), fields),
                    ...messages,
                })
            },
        },

        setFieldSubmit: {
            prepare(id: string, field: string, provider: Provider) {
                return ({
                    payload: { id, field, provider },
                })
            },
            reducer(state, action: SetFieldSubmitAction) {
                const { id, field, provider: propsProvider } = action.payload
                const datasource = state[id]

                datasource.fieldsSubmit[field] = {
                    ...propsProvider,
                    // @ts-ignore FIXME разобраться с типизацией
                    //  (непонятно почему может быть разный type, если ожидается строго service)
                    type: propsProvider.type || ProviderType.service,
                }
            },
        },

        DATA_REQUEST: {
            prepare(datasource: string, options = {}, meta: N2OMeta = {}) {
                return ({
                    payload: { datasource, options },
                    meta,
                })
            },
            reducer(state, action: { payload: { datasource: string, options: unknown } }) {
                // Экшен приходит с сервера. В сагах надо сделать просто ремап
            },
        },

        submit: {
            prepare(id: string, provider?: SubmitProvider, meta = {}) {
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
            prepare(id: string, provider: SubmitProvider, meta?: Meta) {
                return ({
                    payload: { id, provider },
                    meta,
                })
            },
            reducer() {
                // empty reducer, action for saga
            },
        },

        submitFail: {
            prepare(error: unknown, meta?: Meta) {
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
        [removeFromArray.type](state, action: RemoveFromArrayAction) {
            const { modelLink, fieldName, start, count = 1 } = action.payload
            const { id, prefix, index } = modelLink
            const datasource = state[id]

            if (!datasource) { return }
            if (prefix === ModelPrefix.selected) { return }

            const isMulti = prefix === ModelPrefix.source

            // Если удалили строку в datasource, просто удаляем соотвественно из массива ошибок
            if (isMulti && typeof index !== 'number') {
                const errors = datasource.errors[prefix] || []

                errors.splice(start, count)

                return
            }

            // Если удалили строку из модели внутри datasource, то fieldName в нём обязателен
            if (!fieldName) { return }

            const path = isMulti
                ? `${prefix}[${index}]`
                : prefix

            set(datasource.errors, path, mapMultiFields(
                get(datasource.errors, path, {}),
                fieldName,
                getOnRemove(fieldName, start, count),
            ))
        },
        [appendToArray.type](state, action: AppendToArrayAction) {
            const { modelLink, fieldName, position } = action.payload
            const { id, prefix, index } = modelLink
            const datasource = state[id]

            if (!datasource) { return }
            if (prefix === ModelPrefix.selected) { return }
            if (typeof position === 'undefined') { return }

            const isMulti = prefix === ModelPrefix.source

            if (isMulti && typeof index !== 'number') {
                const errors = datasource.errors[prefix] || []

                errors.splice(position, 0, {})

                return
            }

            if (!fieldName) { return }

            const path = isMulti
                ? `${prefix}[${index}]`
                : prefix

            set(datasource.errors, path, mapMultiFields(
                get(datasource.errors, path, {}),
                fieldName,
                getOnAppend(fieldName, position),
            ))
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
    reset,
    endValidation,
    changePage,
    changeSize,
    setFieldSubmit,
    DATA_REQUEST,
    submit,
    submitSuccess,
    submitFail,
    updatePaging,
} = datasource.actions
