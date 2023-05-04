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
import keys from 'lodash/keys'
import { actionTypes, change } from 'redux-form'

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
import { combineModels } from '../ducks/models/store'
import { makeWidgetByIdSelector } from '../ducks/widgets/selectors'
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
            yield put(
                change(form, field, {
                    keepDirty: true,
                    value: nextFieldValue,
                }),
            )
        }
    } catch (e) {
        if (values[field] !== null) {
            yield put(
                change(form, field, {
                    keepDirty: true,
                    value: null,
                    error: true,
                }),
            )
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
export function* modify(values, formName, fieldName, dependency = {}, field) {
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
                yield put(enableField(formName, fieldName))
            } else {
                yield put(disableField(formName, fieldName))
            }

            break
        }
        case 'visible': {
            const nextVisible = Boolean(evalResult)

            if (nextVisible) {
                yield put(showField(formName, fieldName))
            } else {
                yield put(hideField(formName, fieldName))
            }

            break
        }
        case 'setValue': {
            if (evalResult === undefined || isEqual(evalResult, get(values, fieldName))) {
                break
            }

            yield put(
                change(formName, fieldName, {
                    keepDirty: true,
                    value: evalResult,
                }),
            )

            break
        }
        case 'reset': {
            if (values[fieldName] !== null && evalResultCheck(evalResult)) {
                yield put(
                    change(formName, fieldName, {
                        keepDirty: true,
                        value: null,
                    }),
                )
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
                yield put(setRequired(formName, fieldName))
            } else {
                yield put(unsetRequired(formName, fieldName))
            }

            break
        }
        case 'reRender': {
            const form = yield select(makeWidgetByIdSelector(formName))
            const prefix = get(form, [
                'form',
                'modelPrefix',
            ])

            yield delay(50)
            yield put(startValidate(formName, ValidationsKey.Validations, prefix, [fieldName], { touched: true }))

            break
        }
        case 'fetchValue': {
            yield fork(
                fetchValue,
                values,
                formName,
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
    const isChangeAction = actionType === actionTypes.CHANGE

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
                    yield fork(modify, values, formName, fieldId, dep, field)
                }
            }
        }
    }
}

export function* resolveDependency({ type, meta, payload }) {
    /* Костыль под определенный кейс.
     * Прим. Мультисет собирается с помощью зависимости от селекта.
     * delay - для обхода гонки в форме,
     * зависимость отрабатывает раньше чем redux form изменяет поля с пред. данными
     * если delay будет безусловный, в полях при обработке начнется мерцание.
     */
    if (Array.isArray(payload)) {
        yield delay(16)
    }

    try {
        const { form: formName, field: fieldName } = meta
        const form = yield select(makeFormByName(formName))
        const fieldKeys = type === actionTypes.INITIALIZE ? keys(form.registeredFields) : null

        if (isEmpty(form) || isEmpty(form.registeredFields)) {
            return
        }

        if (fieldKeys && meta.lastInitialValues) {
            for (const field of fieldKeys) {
                yield call(
                    checkAndModify,
                    form.values || {},
                    form.registeredFields,
                    formName,
                    field,
                    type,
                )
            }
        } else {
            yield call(
                checkAndModify,
                form.values || {},
                form.registeredFields,
                formName,
                type === registerFieldExtra.type ? payload.name : fieldName,
                type,
            )
        }
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
            form: id,
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
                    form: datasource,
                },
            })
        }
    }
}

export const fieldDependencySagas = [
    takeEvery([
        actionTypes.INITIALIZE,
        registerFieldExtra.type,
        actionTypes.CHANGE,
        initializeDependencies.type,
    ], resolveDependency),
    takeEvery(resolveRequest, resolveDependencyOnInit),
    takeLatest(combineModels, resolveDependencyDefaultModels),
]
