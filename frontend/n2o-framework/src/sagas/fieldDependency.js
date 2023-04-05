import {
    call,
    put,
    select,
    fork,
    takeEvery,
    takeLatest,
    delay,
} from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import some from 'lodash/some'
import includes from 'lodash/includes'
import get from 'lodash/get'

import evalExpression from '../utils/evalExpression'
import { makeFormByName } from '../ducks/form/selectors'
import {
    enableField,
    disableField,
    showField,
    hideField,
    setRequired,
    unsetRequired,
    setLoading,
    registerFieldExtra,
    initializeDependencies,
} from '../ducks/form/store'
import { FETCH_VALUE } from '../core/api'
import { dataProviderResolver } from '../core/dataProviderResolver'
import { evalResultCheck } from '../utils/evalResultCheck'
import { resolveRequest, startValidate } from '../ducks/datasource/store'
import {
    appendFieldToArray,
    combineModels,
    copyFieldArray,
    removeFieldFromArray,
    updateModel,
} from '../ducks/models/store'
import { makeDatasourceIdSelector, makeWidgetByIdSelector } from '../ducks/widgets/selectors'
import { ModelPrefix } from '../core/datasource/const'
import { getModelByPrefixAndNameSelector } from '../ducks/models/selectors'
import { ValidationsKey } from '../core/validation/IValidation'
import { addAlert } from '../ducks/alerts/store'
import { GLOBAL_KEY } from '../ducks/alerts/constants'

import fetchSaga from './fetch'

const FetchValueCache = new Map()

export function* fetchValue(values, form, field, { dataProvider, valueFieldId }) {
    const fetchValueKey = `${form}.${field}`

    try {
        yield delay(300)
        yield put(setLoading(form, field, true))
        const state = yield select()
        const { url, headersParams, baseQuery } = dataProviderResolver(
            state,
            dataProvider,
        )

        if (isEqual(baseQuery, FetchValueCache.get(fetchValueKey))) {
            return
        }

        FetchValueCache.set(fetchValueKey, baseQuery)

        const response = yield call(fetchSaga, FETCH_VALUE, {
            url,
            headers: headersParams,
        })

        const isMultiModel = get(response, 'list', []).length > 1

        const model = isMultiModel
            ? get(response, 'list', null)
            : get(response, 'list[0]', null)

        const currentModel = isMultiModel ? model : model[valueFieldId]
        const prevFieldValue = get(values, field)
        const nextFieldValue = valueFieldId ? currentModel : model

        if (model && !isEqual(prevFieldValue, nextFieldValue)) {
            yield put(updateModel(ModelPrefix.active, form, field, nextFieldValue))
        }
    } catch (e) {
        if (values[field] !== null) {
            yield put(updateModel(ModelPrefix.active, form, field, null))
        }

        const error = e.json || {}
        const { messages } = error?.meta?.alert

        if (messages) {
            const [alert] = messages
            const { placement } = alert || GLOBAL_KEY

            yield put(addAlert(placement, alert))
        }

        // eslint-disable-next-line no-console
        console.error(e)
    } finally {
        yield put(setLoading(form, field, false))
        FetchValueCache.delete(fetchValueKey)
    }
}

// eslint-disable-next-line complexity
export function* modify(prefix, values, datasourceKey, fieldName, dependency = {}, field) {
    const { type, expression } = dependency
    const { parentIndex } = field
    const context = {
        ...values,
        index: typeof parentIndex === 'number' ? parentIndex : values.index,
    }

    const evalResult = expression
        ? evalExpression(expression, context)
        : undefined

    switch (type) {
        case 'enabled': {
            const nextEnabled = Boolean(evalResult)

            if (nextEnabled) {
                yield put(enableField(datasourceKey, fieldName))
            } else {
                yield put(disableField(datasourceKey, fieldName))
            }

            break
        }
        case 'visible': {
            const nextVisible = Boolean(evalResult)

            if (nextVisible) {
                yield put(showField(datasourceKey, fieldName))
            } else {
                yield put(hideField(datasourceKey, fieldName))
            }

            break
        }
        case 'setValue': {
            if (evalResult === undefined || isEqual(evalResult, get(values, fieldName))) {
                break
            }

            yield put(updateModel(prefix, datasourceKey, fieldName, evalResult))

            break
        }
        case 'reset': {
            if (values[fieldName] !== null && evalResultCheck(evalResult)) {
                yield put(updateModel(prefix, datasourceKey, fieldName, null))
            }

            break
        }
        case 'required': {
            const currentRequired = field.required === true
            const nextRequired = Boolean(evalResult)

            if (currentRequired === nextRequired) {
                break
            }

            if (nextRequired) {
                yield put(setRequired(datasourceKey, fieldName))
            } else {
                yield put(unsetRequired(datasourceKey, fieldName))
            }

            break
        }
        case 'reRender': {
            const state = yield select()
            const datasource = makeDatasourceIdSelector(datasourceKey)(state)
            const form = makeWidgetByIdSelector(datasourceKey)(state)
            const prefix = get(form, [
                'form',
                'modelPrefix',
            ])

            yield delay(50)
            yield put(startValidate(datasource, ValidationsKey.Validations, prefix, [fieldName], { touched: true }))

            break
        }
        case 'fetchValue': {
            yield fork(
                fetchValue,
                values,
                datasourceKey,
                fieldName,
                dependency,
            )

            break
        }
        default:
            break
    }
}

export function* checkAndModify(
    prefix,
    values,
    fields,
    formName,
    fieldName,
    actionType,
) {
    for (const fieldId of Object.keys(fields)) {
        const field = fields[fieldId]

        if (field.dependency) {
            for (const dep of field.dependency) {
                const needRunOnInit = dep.applyOnInit && (
                    actionType === initializeDependencies.type ||
                    (fieldName === fieldId &&
                        actionType === registerFieldExtra.type)
                )
                const someDepNeedRun = some(
                    dep.on,
                    field => (
                        field === fieldName ||
                        (includes(field, '.') && includes(field, fieldName))
                    ),
                )

                const isFormActionType = [
                    updateModel.type,
                    appendFieldToArray.type,
                    removeFieldFromArray.type,
                    copyFieldArray,
                ].some(type => type === actionType)

                if (
                    needRunOnInit ||
                    (someDepNeedRun && isFormActionType)
                ) {
                    yield fork(modify, prefix, values, formName, fieldId, dep, field)
                }
            }
        }
    }
}

export function* resolveDependency({ type, meta, payload }) {
    yield delay(16)

    try {
        const { key, field: fieldName, prefix = ModelPrefix.active } = meta
        const formValue = yield select(getModelByPrefixAndNameSelector(prefix, key))
        const form = yield select(makeFormByName(key))

        if (isEmpty(form) || isEmpty(form.registeredFields)) {
            return
        }

        yield call(
            checkAndModify,
            prefix,
            formValue || {},
            form.registeredFields,
            key,
            type === registerFieldExtra.type ? payload.name : fieldName,
            type,
        )
    } catch (e) {
        // todo: падает тут из-за отсутствия формы
        // eslint-disable-next-line no-console
        console.error(e)
    }
}

export function* resolveDependencyOnInit({ payload }) {
    const { id } = payload

    yield call(resolveDependency, {
        type: initializeDependencies.type,
        meta: {
            key: id,
        },
    })
}

export function* resolveDependencyDefaultModels({ payload }) {
    const { combine = {} } = payload

    yield delay(500)

    for (const [, models] of Object.entries(combine)) {
        for (const [datasource] of Object.entries(models)) {
            yield call(resolveDependency, {
                type: initializeDependencies.type,
                meta: {
                    key: datasource,
                },
            })
        }
    }
}

export const fieldDependencySagas = [
    takeEvery([
        registerFieldExtra,
        updateModel,
        initializeDependencies,
        appendFieldToArray,
        removeFieldFromArray,
        copyFieldArray,
    ], resolveDependency),
    takeEvery(resolveRequest, resolveDependencyOnInit),
    takeLatest(combineModels, resolveDependencyDefaultModels),
]
