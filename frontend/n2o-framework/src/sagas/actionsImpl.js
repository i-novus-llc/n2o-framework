import {
    call,
    fork,
    put,
    select,
    takeEvery,
    throttle,
} from 'redux-saga/effects'
import isFunction from 'lodash/isFunction'
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
import { validate as validateDatasource } from '../core/validation/validate'
import { actionResolver } from '../core/factory/actionResolver'
import { dataProviderResolver } from '../core/dataProviderResolver'
import { FETCH_INVOKE_DATA } from '../core/api'
import { setModel } from '../ducks/models/store'
import { disablePage, enablePage } from '../ducks/pages/store'
import { failInvoke, successInvoke } from '../actions/actionImpl'
import { disableWidget, enableWidget } from '../ducks/widgets/store'
import { resolveButton } from '../ducks/toolbar/sagas'
import { changeButtonDisabled, callActionImpl } from '../ducks/toolbar/store'
import { ModelPrefix } from '../core/datasource/const'
import { failValidate, submit } from '../ducks/datasource/store'
import { EffectWrapper } from '../ducks/api/utils/effectWrapper'
import { isDirtyForm } from '../ducks/form/selectors'
import { setDirty } from '../ducks/form/store'

import fetchSaga from './fetch'

// TODO перенести инвок в datassource

export function* validate({ dispatch, validate }) {
    if (!validate?.length) { return true }

    const state = yield select()
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
 * вызов экшена
 */
export function* handleAction(factories, action) {
    const { options, actionSrc } = action.payload

    try {
        let actionFunc

        if (isFunction(actionSrc)) {
            actionFunc = actionSrc
        } else {
            actionFunc = actionResolver(actionSrc, factories)
        }
        const state = yield select()
        const valid = yield validate(options)

        if (!valid) {
            // eslint-disable-next-line no-console
            console.log(`DataSources "${options.valid}" is not valid.`)
        } else if (actionFunc) {
            yield call(actionFunc, {
                ...options,
                state,
            })
        }
    } catch (err) {
        // eslint-disable-next-line no-console
        console.error(err)
    }
}

/**
 * Отправка запроса
 * @param dataProvider
 * @param model
 * @param apiProvider
 * @returns {IterableIterator<*>}
 */
export function* fetchInvoke(dataProvider, model, apiProvider) {
    const state = yield select()

    const submitForm = get(dataProvider, 'submitForm', true)
    const {
        basePath: path,
        formParams,
        headersParams,
    } = yield dataProviderResolver(state, dataProvider)

    const createModelRequest = ({ id, ...data }) => {
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

    return yield call(
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

export function* handleFailInvoke(metaInvokeFail, widgetId, metaResponse) {
    const meta = merge(metaInvokeFail, metaResponse)

    yield put(failInvoke(widgetId, meta))
}

/**
 * @param {string} pageId
 * @param {string[]} widgets
 * @param {object[]} buttons
 * @param {string[]} buttonIds
 */
function* enable(pageId, widgets, buttons, buttonIds) {
    if (pageId) {
        yield put(enablePage(pageId))

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
    if (widgets.length) {
        for (const id of widgets) {
            yield put(enableWidget(id))
        }
    }
}

/**
 * вызов экшена
 */
// eslint-disable-next-line complexity
export function* handleInvoke(apiProvider, action) {
    const {
        datasource,
        model: modelPrefix,
        dataProvider,
        pageId,
        widgetId,
    } = action.payload

    const state = yield select()
    const optimistic = get(dataProvider, 'optimistic', false)
    const buttons = get(state, ['toolbar', pageId], [])
    const buttonIds = !optimistic && has(state, 'toolbar') ? keys(buttons) : []
    const model = yield select(getModelByPrefixAndNameSelector(modelPrefix, datasource))
    const widgets = Object.entries(yield select(widgetsSelector))
        .filter(([, widget]) => (widget.datasource === datasource))
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

        const response = optimistic
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

        const errorMeta = err?.json?.meta || {}

        yield* handleFailInvoke(
            action.meta.fail || {},
            datasource,
            errorMeta,
        )

        if (errorMeta.messages) {
            const fields = {}

            for (const [fieldName, error] of Object.entries(errorMeta.messages.fields)) {
                fields[fieldName] = Array.isArray(error) ? error : [error]
            }

            yield put(failValidate(datasource, fields, modelPrefix, { touched: true }))
        }

        yield enable(pageId, widgets, buttons, buttonIds)

        throw err
    }
}

export default (apiProvider, factories) => [
    throttle(500, callActionImpl.type, handleAction, factories),
    takeEvery(START_INVOKE, EffectWrapper(handleInvoke), apiProvider),
    takeEvery(SUBMIT, function* submitSaga({ meta = {}, payload = {} }) {
        const { datasource } = payload

        yield put(submit(datasource, null, meta))
    }),
]
