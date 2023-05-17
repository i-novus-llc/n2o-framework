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
import { startValidate } from '../ducks/datasource/store'
import {
    appendFieldToArray,
    combineModels,
    copyFieldArray,
    removeFieldFromArray,
    setModel,
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

export function* fetchValue(prefix, values, form, field, { dataProvider, valueFieldId }) {
    const fetchValueKey = `${form}.${field}`

    try {
        yield delay(300)
        yield put(setLoading(prefix, form, field, true))
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
        yield put(setLoading(prefix, form, field, false))
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
                yield put(enableField(prefix, datasourceKey, fieldName))
            } else {
                yield put(disableField(prefix, datasourceKey, fieldName))
            }

            break
        }
        case 'visible': {
            const nextVisible = Boolean(evalResult)

            if (nextVisible) {
                yield put(showField(prefix, datasourceKey, fieldName))
            } else {
                yield put(hideField(prefix, datasourceKey, fieldName))
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
                yield put(setRequired(prefix, datasourceKey, fieldName))
            } else {
                yield put(unsetRequired(prefix, datasourceKey, fieldName))
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
                prefix,
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

const shouldBeResolved = ({
    dependency,
    actionType,
    actionField,
    currentField,
}) => {
    const { applyOnInit, on } = dependency
    const isChangeAction = [
        updateModel.type,
        appendFieldToArray.type,
        removeFieldFromArray.type,
        copyFieldArray.type,
    ].some(type => type === actionType)

    // apply on init
    if (applyOnInit && (
        actionType === initializeDependencies.type ||
        (
            actionField === currentField &&
            actionType === registerFieldExtra.type
        )
    )) { return true }

    // apply on change
    /*
     * условие 2 нужно чтобы стрелть событиями когда обновлённое поле это объект,
     * а подписка на внутренее значение
     * update({ field: { id: 1 } }) on=['field.id'] => fieldName="field"
     * из-за этого условия стреляют лишние зависимости: on=['field.count']
     * FIXME: Переделать на реальное сравнение изменения вложенных полей
     */
    return isChangeAction && on?.some(dependencyField => (
        dependencyField === actionField || // full equality
        dependencyField.startsWith(`${actionField}.`) || // fieldName: "field", on: "field.id"
        actionField.startsWith(`${dependencyField}.`) || // fieldName: "field.inner", on: "field"
        actionField.startsWith(`${dependencyField}[`) // fieldName: "field[index]", on: "field"
    ))
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
                if (shouldBeResolved({
                    actionField: fieldName,
                    actionType,
                    currentField: fieldId,
                    dependency: dep,
                })) {
                    yield fork(modify, prefix, values, formName, fieldId, dep, field)
                }
            }
        }
    }
}

export function* resolveDependency({ type, meta, payload }) {
    yield delay(16)

    try {
        const { key, field, prefix } = meta

        if (!prefix) {
            return
        }

        const formValue = yield select(getModelByPrefixAndNameSelector(prefix, key))
        const form = yield select(makeFormByName(key))
        const registeredFields = get(form, [prefix, 'registeredFields'], {})
        const fieldName = type === registerFieldExtra.type ? payload.name : field

        if (isEmpty(registeredFields)) {
            return
        }

        yield call(
            checkAndModify,
            prefix,
            formValue || {},
            registeredFields,
            key,
            fieldName,
            type,
        )
    } catch (e) {
        // todo: падает тут из-за отсутствия формы
        // eslint-disable-next-line no-console
        console.error(e)
    }
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

function* resolveDependencyAfterSetModel({ payload, meta }) {
    const { prefix, key, model } = payload

    if (prefix === ModelPrefix.source || prefix === ModelPrefix.selected || !model) {
        return
    }
    const { prevState } = meta
    const form = yield select(makeFormByName(key))
    const registeredFields = get(form, [prefix, 'registeredFields'], {})
    const prevModel = getModelByPrefixAndNameSelector(prefix, key)(prevState || {})

    // стандартный обхода ключей объекта циклом for ... in
    // eslint-disable-next-line guard-for-in,no-restricted-syntax
    for (const fieldId in registeredFields) {
        const field = registeredFields[fieldId]
        const { dependency = [] } = field

        if (!dependency.length) {
            // eslint-disable-next-line no-continue
            continue
        }

        // Обход каждой зависимости
        for (const dep of dependency) {
            const { on = [], applyOnInit } = dep
            const isSomeDepsNeedRun = on.length || applyOnInit

            if (!isSomeDepsNeedRun) {
                // eslint-disable-next-line no-continue
                continue
            }

            const isSomeFieldChanged = on.some((fieldPath) => {
                if (!prevModel) {
                    return true
                }

                const currentValue = get(model, fieldPath)
                const prevValue = get(prevModel, fieldPath)

                return !isEqual(currentValue, prevValue)
            })

            if (isSomeFieldChanged) {
                yield fork(modify, prefix, model || {}, key, fieldId, dep, field)
            }
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
    takeEvery(setModel, resolveDependencyAfterSetModel),
    takeLatest(combineModels, resolveDependencyDefaultModels),
]
