import {
    call,
    fork,
    put,
    select,
    throttle,
} from 'redux-saga/effects'
import isFunction from 'lodash/isFunction'
import get from 'lodash/get'
import has from 'lodash/has'
import keys from 'lodash/keys'
import isEqual from 'lodash/isEqual'
import merge from 'deepmerge'

import { START_INVOKE } from '../constants/actionImpls'
import {
} from '../ducks/widgets/selectors'
import { makeGetModelByPrefixSelector } from '../ducks/models/selectors'
import { validate as validateDatasource } from '../core/datasource/validate'
import { actionResolver } from '../core/factory/actionResolver'
import { dataProviderResolver } from '../core/dataProviderResolver'
import { FETCH_INVOKE_DATA } from '../core/api'
import { setModel } from '../ducks/models/store'
import { disablePage, enablePage } from '../ducks/pages/store'
import { failInvoke, successInvoke } from '../actions/actionImpl'
import { disableWidgetOnFetch, enableWidget } from '../ducks/widgets/store'
import { changeButtonDisabled, callActionImpl } from '../ducks/toolbar/store'

import fetchSaga from './fetch'

export function* validate({ dispatch, validate }) {
    if (!validate?.length) { return true }

    const state = yield select()
    let valid = true

    for (const datasourceId of validate) {
        const datasource = yield select(
            makeDatasourceIdSelector(datasourceId),
        )

        valid = valid && (yield call(
            validateDatasource,
            state,
            datasource,
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

    const modelRequest = Array.isArray(model) ? model.map(createModelRequest) : createModelRequest(model)

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
 * вызов экшена
 */
// eslint-disable-next-line complexity
export function* handleInvoke(apiProvider, action) {
    const {
        datasource,
        model: modelPrefix,
        dataProvider,
        widgetId,
        pageId,
    } = action.payload

    const state = yield select()
    const optimistic = get(dataProvider, 'optimistic', false)
    const buttonIds = !optimistic && has(state, 'toolbar') ? keys(state.toolbar[pageId]) : []
    const model = yield select(makeGetModelByPrefixSelector(modelPrefix, datasource))

    try {
        if (!dataProvider) {
            throw new Error('dataProvider is undefined')
        }
        if (pageId && !optimistic) {
            yield put(disablePage(pageId))
        }
        if (widgetId && !optimistic) {
            yield put(disableWidgetOnFetch(widgetId))

            for (let index = 0; index <= buttonIds.length - 1; index += 1) {
                yield put(changeButtonDisabled(pageId, buttonIds[index], true))
            }
        }
        const response = optimistic
            ? yield fork(fetchInvoke, dataProvider, model, apiProvider)
            : yield call(fetchInvoke, dataProvider, model, apiProvider)

        const meta = merge(action.meta.success || {}, response.meta || {})
        const { submitForm } = dataProvider
        const needRedirectOrCloseModal = meta.redirect || meta.modalsToClose

        if (!needRedirectOrCloseModal && !isEqual(model, response.data) && submitForm) {
            yield put(
                setModel(modelPrefix, datasource, optimistic ? model : response.data),
            )
        }
        yield put(successInvoke(widgetId, meta))
    } catch (err) {
        // eslint-disable-next-line no-console
        console.error(err)
        yield* handleFailInvoke(
            action.meta.fail || {},
            widgetId,
            err.json && err.json.meta ? err.json.meta : {},
        )
    } finally {
        if (pageId) {
            yield put(enablePage(pageId))
        }
        if (widgetId) {
            yield put(enableWidget(widgetId))

            for (let index = 0; index <= buttonIds.length - 1; index += 1) {
                yield put(changeButtonDisabled(pageId, buttonIds[index], false))
            }
        }
    }
}

export default (apiProvider, factories) => [
    throttle(500, callActionImpl.type, handleAction, factories),
    throttle(500, START_INVOKE, handleInvoke, apiProvider),
]
