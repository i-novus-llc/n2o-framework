import {
    call,
    fork,
    put,
    select,
    takeEvery,
} from 'redux-saga/effects'
import get from 'lodash/get'
import has from 'lodash/has'
import keys from 'lodash/keys'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import every from 'lodash/every'
import merge from 'deepmerge'

import { START_INVOKE, SUBMIT } from '../constants/actionImpls'
import {
    widgetsSelector,
} from '../ducks/widgets/selectors'
import { getModelByPrefixAndNameSelector } from '../ducks/models/selectors'
import { dataProviderResolver } from '../core/dataProviderResolver'
import { FETCH_INVOKE_DATA } from '../core/api'
import { setModel } from '../ducks/models/store'
import { disablePage, enablePage } from '../ducks/pages/store'
import { failInvoke, successInvoke } from '../actions/actionImpl'
import { disableWidget, enableWidget } from '../ducks/widgets/store'
import { resolveButton } from '../ducks/toolbar/sagas'
import { changeButtonDisabled } from '../ducks/toolbar/store'
import { ModelPrefix } from '../core/datasource/const'
import { failValidate, startValidate, submit } from '../ducks/datasource/store'
import { EffectWrapper } from '../ducks/api/utils/effectWrapper'
import { State } from '../ducks/State'
import { State as WidgetsState } from '../ducks/widgets/Widgets'
import { ButtonContainer } from '../ducks/toolbar/Toolbar'
import { metaPropsType } from '../plugins/utils'
import { DataSourceState } from '../ducks/datasource/DataSource'
import { dataSourceByIdSelector } from '../ducks/datasource/selectors'
import { validate } from '../ducks/datasource/sagas/validate'
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

type FetchInvokeModel = { id: string } | Array<{ id: string }>

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

export function* handleFailInvoke(
    metaInvokeFail: Record<string, unknown>,
    widgetId: string,
    metaResponse: Record<string, unknown>,
) {
    const meta = merge(metaInvokeFail, metaResponse)

    yield put(failInvoke(widgetId, meta))
}

/**
 * @param {string} pageId
 * @param {string[]} widgets
 * @param {object[]} buttons
 * @param {string[]} buttonIds
 */
function* enable(pageId: string, widgets: string[], buttons: ButtonContainer, buttonIds: string[]) {
    if (pageId) {
        yield put(enablePage(pageId))

        if (buttons) {
            for (const buttonId of buttonIds) {
                const button = buttons[buttonId]
                const needUnDisableButton = every(button.conditions, (v, k) => k !== 'enabled')

                if (!isEmpty(button.conditions)) {
                    yield call(resolveButton, buttons[buttonId])
                }

                if (needUnDisableButton) {
                    yield put(changeButtonDisabled(pageId, buttonId, false))
                }
            }
        }
    }
    if (widgets.length) {
        for (const id of widgets) {
            yield put(enableWidget(id))
        }
    }
}

export interface HandleInvokePayload {
    datasource: string
    model: ModelPrefix
    dataProvider: { submitForm?: boolean, optimistic?: boolean }
    pageId: string
    widgetId?: string
}

export interface HandleInvokeMeta {
    success: metaPropsType
    fail: metaPropsType
}

interface ErrorFields {
    [key: string]: Record<string, unknown>
}

// eslint-disable-next-line complexity
export function* handleInvoke(
    apiProvider: unknown,
    action: { payload: HandleInvokePayload, meta?: HandleInvokeMeta },
) {
    const {
        datasource,
        model: modelPrefix,
        dataProvider,
        pageId,
    } = action.payload

    const state: State = yield select()
    const optimistic = get(dataProvider, 'optimistic')
    const buttons = get(state, ['toolbar', pageId])
    const buttonIds = !optimistic && has(state, 'toolbar') ? keys(buttons) : []
    const model: FetchInvokeModel = yield select(getModelByPrefixAndNameSelector(modelPrefix, datasource))
    const allWidgets: WidgetsState = yield select(widgetsSelector)
    const widgets = Object.entries(allWidgets)
        .filter(([, widget]) => widget?.datasource === datasource)
        .map(([key]) => key)

    let errorFields: ErrorFields = {}

    try {
        if (!dataProvider) {
            throw new Error('dataProvider is undefined')
        }
        if (modelPrefix === ModelPrefix.active) {
            const isValid: boolean = yield validate(startValidate(
                datasource,
                // @ts-ignore проблема с типизацией saga
                ValidationsKey.Validations,
                modelPrefix,
                undefined,
                { touched: true },
            ))

            if (!isValid) { throw new Error('invalid model') }
        }
        if (pageId && !optimistic) {
            yield put(disablePage(pageId))
            for (const buttonId of buttonIds) {
                yield put(changeButtonDisabled(pageId, buttonId, true))
            }
        }
        if (widgets.length && !optimistic) {
            for (const id of widgets) {
                yield put(disableWidget(id))
            }
        }

        const response: { meta: metaPropsType, data: { $list: metaPropsType } } = optimistic
            ? yield fork(fetchInvoke, dataProvider, model, apiProvider)
            : yield call(fetchInvoke, dataProvider, model, apiProvider)

        errorFields = get(response, 'meta.messages.fields', {})

        const meta = merge(action.meta?.success || {}, response.meta || {})

        if (optimistic === false) {
            const newModel = modelPrefix === ModelPrefix.selected ? response.data?.$list : response.data

            if (!isEqual(model, newModel)) {
                yield put(
                    setModel(modelPrefix, datasource, newModel),
                )
            }
        }
        yield put(successInvoke(datasource, modelPrefix, meta))
        yield enable(pageId, widgets, buttons, buttonIds)
    } catch (err) {
        // eslint-disable-next-line no-console
        console.error(err)

        const errorMeta = get(err, 'json.meta', {})

        errorFields = get(errorMeta, 'messages.fields', {})

        yield* handleFailInvoke(
            action?.meta?.fail || {},
            datasource,
            errorMeta,
        )

        yield enable(pageId, widgets, buttons, buttonIds)

        throw err
    } finally {
        const fields: Record<string, unknown> = {}

        for (const [fieldName, error] of Object.entries(errorFields)) {
            fields[fieldName] = Array.isArray(error) ? error : [error]
        }

        // @ts-ignore FIXME непонял как поправить
        yield put(failValidate(datasource, fields, modelPrefix, { touched: true }))
    }
}

export default (apiProvider: unknown) => [
    // @ts-ignore проблема с типизацией saga
    takeEvery(START_INVOKE, EffectWrapper(handleInvoke), apiProvider),
    // @ts-ignore проблема с типизацией saga
    takeEvery(SUBMIT, function* submitSaga({ meta = {}, payload = {} }) {
        const { datasource } = payload
        const { submit: submitProvider }: DataSourceState = yield select(dataSourceByIdSelector(datasource))

        // @ts-ignore проблема с типизацией saga
        yield put(submit(datasource, submitProvider, meta))
    }),
]
