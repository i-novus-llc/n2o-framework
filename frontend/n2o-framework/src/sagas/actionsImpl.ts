import {
    call,
    fork,
    put,
    select,
    takeEvery,
} from 'redux-saga/effects'
import get from 'lodash/get'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import merge from 'deepmerge'

import { START_INVOKE, SUBMIT } from '../constants/actionImpls'
import { getModelSelector } from '../ducks/models/selectors'
import { dataProviderResolver } from '../core/dataProviderResolver'
import { FETCH_INVOKE_DATA } from '../core/api'
import { setModel } from '../ducks/models/store'
import { failInvoke, successInvoke } from '../actions/actionImpl'
import { ModelPrefix } from '../core/datasource/const'
import { endValidation, startValidate, submit } from '../ducks/datasource/store'
import { AsyncEffectWrapper } from '../ducks/api/utils/effectWrapper'
import { type State } from '../ducks/State'
import { type metaPropsType } from '../plugins/CommonMenuTypes'
import { type DataSourceState } from '../ducks/datasource/DataSource'
import { dataSourceByIdSelector } from '../ducks/datasource/selectors'
import { validate as validateSaga } from '../ducks/datasource/sagas/validate'
import { type N2OMeta } from '../ducks/Action'
import { type ValidationResult } from '../core/validation/types'
import { hasError } from '../core/validation/validateModel'
import { logger } from '../utils/logger'

import fetchSaga from './fetch'

// TODO перенести инвок в datasource

/**
 * Отправка запроса
 * @param dataProvider
 * @param model
 * @param apiProvider
 * @returns {IterableIterator<*>}
 */

type FetchInvokeModel = Record<string, unknown> | Array<Record<string, unknown>>

const prepareModel = (
    model: FetchInvokeModel,
    formParams: object,
    submitForm?: boolean,
) => {
    if (submitForm) {
        if (Array.isArray(model)) {
            return model.map(model => ({
                ...model,
                ...formParams,
            }))
        }

        return {
            ...model,
            ...formParams,
        }
    }

    return formParams
}

export function* fetchInvoke(
    dataProvider: { method?: string, submitForm?: boolean, optimistic?: boolean },
    model: FetchInvokeModel,
    apiProvider: unknown,
) {
    const state: State = yield select()

    const submitForm = get(dataProvider, 'submitForm', true)
    const { basePath: path, formParams, headersParams } = yield dataProviderResolver(state, dataProvider)

    const modelRequest = prepareModel(model, formParams, submitForm)

    // @ts-ignore import from js file
    return yield call(
        // @ts-ignore import from js file
        fetchSaga,
        FETCH_INVOKE_DATA,
        {
            basePath: path,
            baseQuery: {},
            baseMethod: dataProvider.method,
            headers: headersParams,
            model: modelRequest,
        },
        apiProvider,
    )
}

export interface HandleInvokePayload {
    datasource?: string
    model: ModelPrefix
    dataProvider: { submitForm?: boolean, optimistic?: boolean }
    pageId: string
    widgetId?: string
    field?: `[${number}]`
}

export interface HandleInvokeMeta extends N2OMeta {
    success?: metaPropsType
    fail?: metaPropsType
}

interface ErrorFields {
    [key: string]: ValidationResult | ValidationResult[]
}

// eslint-disable-next-line complexity
export function* handleInvoke(
    apiProvider: unknown,
    { payload, meta = {} }: { payload: HandleInvokePayload, meta?: HandleInvokeMeta },
) {
    const {
        datasource: id,
        model: prefix,
        dataProvider,
        field,
    } = payload

    const optimistic = get(dataProvider, 'optimistic')
    const index = (field && parseInt(field?.replace('[', ''), 10)) ?? undefined
    const model: FetchInvokeModel = id
        ? yield select(getModelSelector({ prefix, id, index }))
        : {}

    let errorFields: ErrorFields = {}

    try {
        if (!dataProvider) { throw new Error('dataProvider is undefined') }

        const { validate } = meta

        if (id && (validate !== false)) {
            const messages: Record<string, ValidationResult[]> = yield validateSaga(startValidate(
                { prefix, id, index },
                undefined,
                { touched: true },
            ))

            if (hasError(messages)) { throw new Error('invalid model') }
        }

        const response: { meta: metaPropsType, data: { $list: metaPropsType } } = optimistic
            ? yield fork(fetchInvoke, dataProvider, model, apiProvider)
            : yield call(fetchInvoke, dataProvider, model, apiProvider)

        errorFields = get(response, 'meta.messages.fields', {})

        const successMeta = merge(meta.success || {}, response.meta || {})

        if (optimistic === false) {
            const newModel = (prefix === ModelPrefix.selected || prefix === ModelPrefix.source) && (typeof index !== 'number')
                ? response.data?.$list
                : response.data

            if (id && !isEqual(model, newModel)) {
                yield put(setModel({ id, prefix, index }, newModel, true))
            }
        }
        yield put(successInvoke(id, prefix, successMeta))
    } catch (err) {
        logger.error(err)

        const errorMeta = get(err, 'json.meta', {})

        errorFields = get(errorMeta, 'messages.fields', {})

        yield put(failInvoke(id, merge(meta.fail || {}, errorMeta)))

        throw err
    } finally {
        if (id) {
            const messages: Record<string, ValidationResult[]> = {}

            for (const [fieldName, error] of Object.entries(errorFields)) {
                messages[fieldName] = Array.isArray(error) ? error : [error]
            }

            if (!isEmpty(messages)) {
                yield put(endValidation({
                    modelLink: { prefix, id, index },
                    messages,
                    fields: Object.keys(messages),
                }, { touched: true }))
            }
        }
    }
}

export default (apiProvider: unknown) => [
    // @ts-ignore проблема с типизацией saga
    takeEvery(START_INVOKE, AsyncEffectWrapper(handleInvoke), apiProvider),
    // @ts-ignore проблема с типизацией saga
    takeEvery(SUBMIT, function* submitSaga({ meta = {}, payload = {} }) {
        const { datasource } = payload
        const { submit: submitProvider }: DataSourceState = yield select(dataSourceByIdSelector(datasource))

        yield put(submit(datasource, submitProvider, meta))
    }),
]
