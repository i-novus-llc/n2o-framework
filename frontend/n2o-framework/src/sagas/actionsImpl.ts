import {
    call,
    fork,
    put,
    select,
    takeEvery,
} from 'redux-saga/effects'
import get from 'lodash/get'
import isEqual from 'lodash/isEqual'
import merge from 'deepmerge'

import { START_INVOKE, SUBMIT } from '../constants/actionImpls'
import { getModelByPrefixAndNameSelector } from '../ducks/models/selectors'
import { dataProviderResolver } from '../core/dataProviderResolver'
import { FETCH_INVOKE_DATA } from '../core/api'
import { setModel } from '../ducks/models/store'
import { failInvoke, successInvoke } from '../actions/actionImpl'
import { ModelPrefix } from '../core/datasource/const'
import { failValidate, startValidate, submit } from '../ducks/datasource/store'
import { EffectWrapper } from '../ducks/api/utils/effectWrapper'
import { State } from '../ducks/State'
import { metaPropsType } from '../plugins/utils'
import { DataSourceState } from '../ducks/datasource/DataSource'
import { dataSourceByIdSelector } from '../ducks/datasource/selectors'
import { validate as validateSaga } from '../ducks/datasource/sagas/validate'
import { N2OMeta } from '../ducks/Action'
import { ValidationsKey } from '../core/validation/types'

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
}

export interface HandleInvokeMeta extends N2OMeta {
    success?: metaPropsType
    fail?: metaPropsType
}

interface ErrorFields {
    [key: string]: Record<string, unknown>
}

// eslint-disable-next-line complexity
export function* handleInvoke(
    apiProvider: unknown,
    { payload, meta = {} }: { payload: HandleInvokePayload, meta?: HandleInvokeMeta },
) {
    const {
        datasource,
        model: modelPrefix,
        dataProvider,
    } = payload

    const optimistic = get(dataProvider, 'optimistic')
    const model: FetchInvokeModel = datasource
        ? yield select(getModelByPrefixAndNameSelector(modelPrefix, datasource))
        : {}

    let errorFields: ErrorFields = {}

    try {
        if (!dataProvider) { throw new Error('dataProvider is undefined') }

        const { validate } = meta

        if (datasource && (validate !== false) && (modelPrefix === ModelPrefix.active)) {
            const isValid: boolean = yield validateSaga(startValidate(
                datasource,
                ValidationsKey.Validations,
                modelPrefix,
                undefined,
                { touched: true },
            ))

            if (!isValid) { throw new Error('invalid model') }
        }

        const response: { meta: metaPropsType, data: { $list: metaPropsType } } = optimistic
            ? yield fork(fetchInvoke, dataProvider, model, apiProvider)
            : yield call(fetchInvoke, dataProvider, model, apiProvider)

        errorFields = get(response, 'meta.messages.fields', {})

        const successMeta = merge(meta.success || {}, response.meta || {})

        if (optimistic === false) {
            const newModel = modelPrefix === ModelPrefix.selected ? response.data?.$list : response.data

            if (datasource && !isEqual(model, newModel)) {
                yield put(
                    setModel(modelPrefix, datasource, newModel),
                )
            }
        }
        yield put(successInvoke(datasource, modelPrefix, successMeta))
    } catch (err) {
        // eslint-disable-next-line no-console
        console.error(err)

        const errorMeta = get(err, 'json.meta', {})

        errorFields = get(errorMeta, 'messages.fields', {})

        yield put(failInvoke(datasource, merge(meta.fail || {}, errorMeta)))

        throw err
    } finally {
        if (datasource) {
            const fields: Record<string, unknown> = {}

            for (const [fieldName, error] of Object.entries(errorFields)) {
                fields[fieldName] = Array.isArray(error) ? error : [error]
            }

            yield put(failValidate(datasource, fields, modelPrefix, { touched: true }))
        }
    }
}

export default (apiProvider: unknown) => [
    // @ts-ignore проблема с типизацией saga
    takeEvery(START_INVOKE, EffectWrapper(handleInvoke), apiProvider),
    // @ts-ignore проблема с типизацией saga
    takeEvery(SUBMIT, function* submitSaga({ meta = {}, payload = {} }) {
        const { datasource } = payload
        const { submit: submitProvider }: DataSourceState = yield select(dataSourceByIdSelector(datasource))

        yield put(submit(datasource, submitProvider, meta))
    }),
]
