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
    dangerouslySetFieldValue,
} from '../ducks/form/store'
// @ts-ignore ignore import error from js file
import { FETCH_VALUE } from '../core/api'
// @ts-ignore ignore import error from js file
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
import { ValidationsKey } from '../core/validation/types'
import { addAlert } from '../ducks/alerts/store'
import { GLOBAL_KEY } from '../ducks/alerts/constants'
import { ModelPrefix } from '../core/datasource/const'
import { FETCH_TRIGGER } from '../core/dependencies/constants'
import { State } from '../ducks/State'

import fetchSaga from './fetch'

const FetchValueCache = new Map()

interface ModifyDependency {
    type?: string
    expression?: string
    applyOnInit?: boolean
    on?: string[]
    dataProvider?: unknown
    valueFieldId?: string
}

interface ModifyField {
    dependency: ModifyDependency[]
}

interface ModifyForm {
    formName: string
    datasource: string
    modelPrefix: ModelPrefix
    fields: ModifyField[]
}

interface ModifyField {
    parentIndex: string | number
    required?: boolean
}

export function* fetchValue(
    values: Record<string, unknown>,
    { formName, datasource, modelPrefix }: ModifyForm,
    field: string,
    { dataProvider, valueFieldId = '' }: ModifyDependency,
) {
    const fetchValueKey = `${formName}.${field}`

    try {
        yield delay(300)
        yield put(setFieldLoading(formName, field, true))
        const state: State = yield select()
        const { url, headersParams, baseQuery } = dataProviderResolver(
            state,
            dataProvider,
        )

        if (isEqual(baseQuery, FetchValueCache.get(fetchValueKey))) {
            return
        }

        FetchValueCache.set(fetchValueKey, baseQuery)

        const response: { list: Array<Record<string, unknown>> } =
            yield call(fetchSaga, FETCH_VALUE, { url, headers: headersParams })

        const isMultiModel = get(response, 'list', []).length > 1

        const model = isMultiModel
            ? get(response, 'list', null)
            : get(response, 'list[0]', null)

        const currentModel = isMultiModel ? model : get(model, valueFieldId, {})
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

export function* modify(
    form: ModifyForm,
    values: Record<string, unknown>,
    fieldName: string,
    field: ModifyField,
    dependency: ModifyDependency = {},
) {
    const { formName, datasource, modelPrefix } = form
    const { type, expression } = dependency
    const { parentIndex } = field
    const context = {
        ...values,
        index: typeof parentIndex === 'number' ? parentIndex : values.index,
    }

    const evalResult: boolean | void = expression ? evalExpression(expression, context) : undefined

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
                // @ts-ignore FIXME непонял как поправить
                datasource, ValidationsKey.Validations, modelPrefix, [fieldName], { touched: true },
            ))

            break
        }
        case 'fetchValue': {
            yield fork(fetchValue, values, form, fieldName, dependency)

            break
        }
        /* FIXME Временное решение, fetch с помощью dataProvider
            нужно перенести _fetchData из withFetchData в middleware
             fetchTrigger используется в withObserveDependency
        **/
        case 'fetch': {
            yield put(dangerouslySetFieldValue(
                formName,
                fieldName,
                FETCH_TRIGGER,
                Math.random(),
            ))

            break
        }
        default:
            break
    }
}

interface ShouldBeResolved {
    dependency: ModifyDependency
    actionType: string
    actionField: string
    currentField: string
}

const shouldBeResolved = ({ dependency, actionType, actionField, currentField }: ShouldBeResolved) => {
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
    form: ModifyForm,
    values: Record<string, unknown>,
    fieldName: string,
    actionType: string,
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
                    yield fork(modify, form, values, fieldId, field, dep)
                }
            }
        }
    }
}

interface ResolveOnUpdateModel {
    type: string
    meta: {
        key: string
        field: string
        prefix: ModelPrefix
    }
    payload: {
        fieldName: string
    }
}

export function* resolveOnUpdateModel({ type, meta, payload }: ResolveOnUpdateModel) {
    yield delay(16)

    const { key: datasource, field, prefix } = meta

    const formValue: Record<string, unknown> = yield select(getModelByPrefixAndNameSelector(prefix, datasource))
    const forms: ModifyForm[] = yield select(makeFormsByModel(datasource, prefix))

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

interface ResolveOnItitPayload {
    formName: string
    fieldName: string
}

export function* resolveOnInit({ type, payload }: { type: string, payload: ResolveOnItitPayload }) {
    yield delay(16)

    const { formName, fieldName } = payload

    const form: ModifyForm = yield select(makeFormByName(formName))
    const formValue: Record<string, unknown> =
        yield select(getModelByPrefixAndNameSelector(form.modelPrefix, form.datasource))

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

interface ResolveOnSetModelPayload {
    prefix: ModelPrefix
    key: string
    model: Record<string, unknown>
    isDefault: boolean
}

function* resolveOnSetModel({ payload, meta }: { payload: ResolveOnSetModelPayload, meta: { prevState: State } }) {
    const { prefix, key: datasource, model, isDefault } = payload

    if (prefix === ModelPrefix.source || prefix === ModelPrefix.selected || !model) {
        return
    }

    const { prevState } = meta
    const forms: ModifyForm[] = yield select(makeFormsByModel(datasource, prefix))
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

                if (isDefault) {
                    if (applyOnInit) {
                        yield fork(modify, form, model || {}, fieldId, field, dep)
                    }

                    // eslint-disable-next-line no-continue
                    continue
                }

                const isSomeFieldChanged = !prevModel || on.some((fieldPath: string) => {
                    const currentValue = get(model, fieldPath)
                    const prevValue = get(prevModel, fieldPath)

                    return !isEqual(currentValue, prevValue)
                })

                if (isSomeFieldChanged) {
                    yield fork(modify, form, model || {}, fieldId, field, dep)
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
    // @ts-ignore Проблема с типизацией saga
    takeEvery(setModel, resolveOnSetModel),
]
