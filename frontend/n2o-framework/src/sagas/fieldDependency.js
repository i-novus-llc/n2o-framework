import {
    call,
    put,
    select,
    fork,
    takeEvery,
    delay,
} from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import get from 'lodash/get'

import evalExpression from '../utils/evalExpression'
import { makeFormByName, makeFormsByModel } from '../ducks/form/selectors'
import {
    setFieldDisabled,
    setFieldVisible,
    setFieldRequired,
    setFieldLoading,
    registerFieldExtra,
} from '../ducks/form/store'
import { FETCH_VALUE } from '../core/api'
import { dataProviderResolver } from '../core/dataProviderResolver'
import { evalResultCheck } from '../utils/evalResultCheck'
import { startValidate } from '../ducks/datasource/store'
import {
    appendFieldToArray,
    copyFieldArray,
    removeFieldFromArray,
    setModel,
    updateModel,
} from '../ducks/models/store'
import { getModelByPrefixAndNameSelector } from '../ducks/models/selectors'
import { ValidationsKey } from '../core/validation/IValidation'
import { addAlert } from '../ducks/alerts/store'
import { GLOBAL_KEY } from '../ducks/alerts/constants'
import { ModelPrefix } from '../core/datasource/const'

import fetchSaga from './fetch'

const FetchValueCache = new Map()

export function* fetchValue(values, { formName, datasource, modelPrefix }, field, { dataProvider, valueFieldId }) {
    const fetchValueKey = `${formName}.${field}`

    try {
        yield delay(300)
        yield put(setFieldLoading(formName, field, true))
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

        const currentModel = isMultiModel ? model : get(model, valueFieldId)
        const prevFieldValue = get(values, field)
        const nextFieldValue = valueFieldId ? currentModel : model

        if (!isEqual(prevFieldValue, nextFieldValue)) {
            yield put(updateModel(modelPrefix, datasource, field, nextFieldValue))
        }
    } catch (error) {
        if (values[field] !== null) {
            yield put(updateModel(modelPrefix, datasource, field, null))
        }

        const messages = get(error, ['json', 'error', 'meta', 'alert', 'message'])

        if (messages) {
            const [alert] = messages
            const { placement } = alert || GLOBAL_KEY

            yield put(addAlert(placement, alert))
        }

        // eslint-disable-next-line no-console
        console.error(error)
    } finally {
        yield put(setFieldLoading(formName, field, false))
        FetchValueCache.delete(fetchValueKey)
    }
}

export function* modify(form, values, fieldName, dependency = {}, field) {
    const { formName, datasource, modelPrefix } = form
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

            yield put(setFieldDisabled(formName, fieldName, !nextEnabled))

            break
        }
        case 'visible': {
            const nextVisible = Boolean(evalResult)

            yield put(setFieldVisible(formName, fieldName, nextVisible))

            break
        }
        case 'setValue': {
            if (evalResult === undefined || isEqual(evalResult, get(values, fieldName))) {
                break
            }

            yield put(updateModel(modelPrefix, datasource, fieldName, evalResult))

            break
        }
        case 'reset': {
            if (values[fieldName] !== null && evalResultCheck(evalResult)) {
                yield put(updateModel(modelPrefix, datasource, fieldName, null))
            }

            break
        }
        case 'required': {
            const currentRequired = field.required === true
            const nextRequired = Boolean(evalResult)

            if (currentRequired === nextRequired) {
                break
            }

            yield put(setFieldRequired(formName, fieldName, nextRequired))

            break
        }
        case 'reRender': {
            yield delay(50)
            yield put(startValidate(
                datasource,
                ValidationsKey.Validations,
                modelPrefix,
                [fieldName],
                { touched: true },
            ))

            break
        }
        case 'fetchValue': {
            yield fork(
                fetchValue,
                values,
                form,
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
    if (applyOnInit && ((
        actionField === currentField &&
        actionType === registerFieldExtra.type
    ))) { return true }

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
    form,
    values,
    fieldName,
    actionType,
) {
    for (const [fieldId, field] of Object.entries(form.fields)) {
        if (field.dependency) {
            for (const dep of field.dependency) {
                if (shouldBeResolved({
                    actionField: fieldName,
                    actionType,
                    currentField: fieldId,
                    dependency: dep,
                })) {
                    yield fork(modify, form, values, fieldId, dep, field)
                }
            }
        }
    }
}

export function* resolveOnUpdateModel({ type, meta, payload }) {
    yield delay(16)

    const { key: datasource, field, prefix } = meta

    const formValue = yield select(getModelByPrefixAndNameSelector(prefix, datasource))
    const forms = yield select(makeFormsByModel(datasource, prefix))

    for (const form of forms) {
        const fieldName = type === registerFieldExtra.type ? payload.fieldName : field

        if (isEmpty(form.fields)) {
            return
        }

        yield call(
            checkAndModify,
            form,
            formValue || {},
            fieldName,
            type,
        )
    }
}

export function* resolveOnInit({ type, payload }) {
    yield delay(16)

    const { formName, fieldName } = payload

    const form = yield select(makeFormByName(formName))
    const formValue = yield select(getModelByPrefixAndNameSelector(form.modelPrefix, form.datasource))

    if (isEmpty(form.fields)) {
        return
    }

    yield call(
        checkAndModify,
        form,
        formValue || {},
        fieldName,
        type,
    )
}

function* resolveOnSetModel({ payload, meta }) {
    const { prefix, key: datasource, model } = payload

    if (prefix === ModelPrefix.source || prefix === ModelPrefix.selected || !model) {
        return
    }

    const { prevState } = meta
    const forms = yield select(makeFormsByModel(datasource, prefix))
    const prevModel = getModelByPrefixAndNameSelector(prefix, datasource)(prevState || {})

    for (const form of forms) {
        for (const [fieldId, field] of Object.entries(form.fields)) {
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
                    yield fork(modify, form, model || {}, fieldId, dep, field)
                }
            }
        }
    }
}

export const fieldDependencySagas = [
    takeEvery([
        registerFieldExtra,
    ], resolveOnInit),
    takeEvery([
        updateModel,
        appendFieldToArray,
        removeFieldFromArray,
        copyFieldArray,
    ], resolveOnUpdateModel),
    takeEvery(setModel, resolveOnSetModel),
]
