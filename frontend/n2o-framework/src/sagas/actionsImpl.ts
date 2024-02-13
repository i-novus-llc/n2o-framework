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
import { Dispatch } from 'redux'

import { START_INVOKE, SUBMIT } from '../constants/actionImpls'
import {
    widgetsSelector,
} from '../ducks/widgets/selectors'
import { getModelByPrefixAndNameSelector } from '../ducks/models/selectors'
import { validate as validateDatasource } from '../core/validation/validate'
import { dataProviderResolver } from '../core/dataProviderResolver'
import { FETCH_INVOKE_DATA } from '../core/api'
import { setModel } from '../ducks/models/store'
import { disablePage, enablePage } from '../ducks/pages/store'
import { failInvoke, successInvoke } from '../actions/actionImpl'
import { disableWidget, enableWidget } from '../ducks/widgets/store'
import { resolveButton } from '../ducks/toolbar/sagas'
import { changeButtonDisabled } from '../ducks/toolbar/store'
import { ModelPrefix } from '../core/datasource/const'
import { failValidate, submit } from '../ducks/datasource/store'
import { EffectWrapper } from '../ducks/api/utils/effectWrapper'
import { State } from '../ducks/State'
import { State as WidgetsState } from '../ducks/widgets/Widgets'
import { ButtonContainer } from '../ducks/toolbar/Toolbar'
import { metaPropsType } from '../plugins/utils'
import { setDirty } from '../ducks/form/store'
import { isDirtyForm } from '../ducks/form/selectors'

import fetchSaga from './fetch'

// TODO перенести инвок в datassource

interface Validate {
    dispatch: Dispatch
    validate: string[]
}

export function* validate({ dispatch, validate }: Validate) {
    if (!validate?.length) { return true }

    const state: State = yield select()
    let valid = true

    for (const datasourceId of validate) {
        valid = valid && (yield call(
            validateDatasource,
            state,
            datasourceId,
            ModelPrefix.active,
            dispatch,
            true,
        ))
    }

    return valid
}

/**
 * Отправка запроса
 * @param dataProvider
 * @param model
 * @param apiProvider
 * @returns {IterableIterator<*>}
 */

type FetchInvokeModel = { id: string } | Array<{ id: string }>

export function* fetchInvoke(
    dataProvider: { method?: string, submitForm?: boolean, optimistic?: boolean },
    model: FetchInvokeModel,
    apiProvider: unknown,
) {
    const state: State = yield select()

    const submitForm = get(dataProvider, 'submitForm', true)
    // @ts-ignore import from js file
    const { basePath: path, formParams, headersParams } = yield dataProviderResolver(state, dataProvider)

    const createModelRequest = ({ id, ...data }: { id: string }) => {
        const modelRequest = {
            id,
            ...formParams,
        }

        if (submitForm) {
            return {
                ...data,
                ...modelRequest,
            }
        }

        return modelRequest
    }

    const modelRequest = Array.isArray(model) ? model.map(createModelRequest) : createModelRequest(model || {})

    // @ts-ignore import from js file
    return yield call(fetchSaga, FETCH_INVOKE_DATA,
        {
            basePath: path,
            baseQuery: {},
            baseMethod: dataProvider.method,
            headers: headersParams,
            model: modelRequest,
        },
        apiProvider)
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

/**
 * вызов экшена
 */

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
        widgetId,
    } = action.payload

    const state: State = yield select()
    const optimistic = get(dataProvider, 'optimistic', false)
    const buttons = get(state, ['toolbar', pageId])
    const buttonIds = !optimistic && has(state, 'toolbar') ? keys(buttons) : []
    const model: FetchInvokeModel = yield select(getModelByPrefixAndNameSelector(modelPrefix, datasource))
    const allWidgets: WidgetsState = yield select(widgetsSelector)
    const widgets = Object.entries(allWidgets)
        .filter(([, widget]) => widget?.datasource === datasource)
        .map(([key]) => key)

    try {
        if (!dataProvider) {
            throw new Error('dataProvider is undefined')
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

        // Доп проверка на то, что сохранение было произведено в форме и если это так, то мы сбрасываем флаг dirty
        if (widgetId) {
            const currentDirtyState = isDirtyForm(widgetId)(state)

            if (currentDirtyState) {
                yield put(setDirty(widgetId, false))
            }
        }

        const response: {meta: metaPropsType, data: {$list: metaPropsType}} = optimistic
            ? yield fork(fetchInvoke, dataProvider, model, apiProvider)
            : yield call(fetchInvoke, dataProvider, model, apiProvider)

        const meta = merge(action.meta?.success || {}, response.meta || {})
        const { submitForm } = dataProvider

        if (!optimistic && submitForm) {
            const newModel = modelPrefix === ModelPrefix.selected ? response.data?.$list : response.data

            if (!isEqual(model, newModel)) {
                yield put(
                    setModel(modelPrefix, datasource, newModel),
                )
            }
        }
        yield put(successInvoke(datasource, meta))
        yield enable(pageId, widgets, buttons, buttonIds)
    } catch (err) {
        // eslint-disable-next-line no-console
        console.error(err)

        const errorMeta = get(err, 'json.meta', {})

        yield* handleFailInvoke(
            action?.meta?.fail || {},
            datasource,
            errorMeta,
        )

        if (errorMeta.messages) {
            const fields: Record<string, unknown> = {}

            for (const [fieldName, error] of Object.entries(errorMeta.messages.fields)) {
                fields[fieldName] = Array.isArray(error) ? error : [error]
            }

            // @ts-ignore FIXME непонял как поправить
            yield put(failValidate(datasource, fields, modelPrefix, { touched: true }))
        }

        yield enable(pageId, widgets, buttons, buttonIds)

        throw err
    }
}

export default (apiProvider: unknown) => [
    // @ts-ignore проблема с типизацией saga
    takeEvery(START_INVOKE, EffectWrapper(handleInvoke), apiProvider),
    // @ts-ignore проблема с типизацией saga
    takeEvery(SUBMIT, function* submitSaga({ meta = {}, payload = {} }) {
        const { datasource } = payload

        // @ts-ignore проблема с типизацией saga
        yield put(submit(datasource, null, meta))
    }),
]
