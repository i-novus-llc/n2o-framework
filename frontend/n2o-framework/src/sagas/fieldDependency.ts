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

import { executeExpression } from '../core/Expression/execute'
import { makeFormByName, makeFormsByModel } from '../ducks/form/selectors'
import {
    setFieldDisabled,
    setFieldVisible,
    setFieldRequired,
    setFieldLoading,
    registerFieldExtra,
    dangerouslySetFieldValue,
    setFieldTooltip,
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
import { ValidationsKey } from '../core/validation/types'
import { addAlert } from '../ducks/alerts/store'
import { GLOBAL_KEY } from '../ducks/alerts/constants'
import { ModelPrefix } from '../core/datasource/const'
import { FETCH_TRIGGER } from '../core/dependencies/constants'
import { State as GlobalState } from '../ducks/State'
import { Form, Field, FieldDependency } from '../ducks/form/types'
import { RegisterFieldAction } from '../ducks/form/Actions'
import { SetModelAction } from '../ducks/models/Actions'

import fetchSaga from './fetch'

const FetchValueCache = new Map()

export function* fetchValue(
    values: Record<string, unknown>,
    { formName, datasource, modelPrefix }: Form,
    field: string,
    { dataProvider, valueFieldId = '' }: FieldDependency,
    evalContext: Record<string, unknown>,
) {
    const fetchValueKey = `${formName}.${field}`

    try {
        yield delay(300)
        yield put(setFieldLoading(formName, field, true))
        const state: GlobalState = yield select()
        // @ts-ignore ignore js file typing
        const { url, headersParams, baseQuery } = dataProviderResolver(state, { ...dataProvider, evalContext })

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

        const currentModel = isMultiModel ? model : get(model, valueFieldId, null)
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
    form: Form,
    values: Record<string, unknown>,
    fieldName: string,
    field: Field,
    dependency: FieldDependency,
) {
    const { formName, datasource, modelPrefix, fields } = form
    const { type, expression } = dependency

    const evalResult = expression && executeExpression<unknown>(expression, values, field.ctx)

    switch (type) {
        case 'enabled': {
            const nextEnabled = Boolean(evalResult)

            yield put(setFieldDisabled(formName, fieldName, !nextEnabled))

            if (nextEnabled) {
                yield put(setFieldTooltip(formName, fieldName, null))

                break
            }

            const { message } = dependency

            if (message) { yield put(setFieldTooltip(formName, fieldName, message)) }

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
            if (values?.[fieldName] !== null && evalResultCheck(evalResult)) {
                yield put(updateModel(modelPrefix, datasource, fieldName, null))
            }

            break
        }
        case 'required': {
            const currentRequired = field.required
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
            const { ctx = {} } = fields[fieldName]

            yield fork(fetchValue, values, form, fieldName, dependency, ctx)

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

const isParentFieldOf = (field: string, path: string) => (
    path.startsWith(`${field}.`) || // path: "field.inner", field: "field"
    path.startsWith(`${field}[`) // path: "field[index]", field: "field"
)
const shouldBeResolved = (
    dependency: FieldDependency,
    actionField: string,
    model: Record<string, unknown>,
    prevModel: Record<string, unknown>,
) => {
    const { on } = dependency

    return on?.some(dependencyField => (
        // Путь полностью совпал
        dependencyField === actionField ||
        // подписка на внешнее поле, изменилось внутренее: field="field[index].id", on="field"
        isParentFieldOf(dependencyField, actionField) ||
        // Подписка на внутрнее поле, изменилось внешнее: field="field", on="field[index].id"
        (
            isParentFieldOf(actionField, dependencyField) &&
            !isEqual(get(model, dependencyField), get(prevModel, dependencyField))
        )
    ))
}

interface ResolveOnUpdateModel {
    type: string
    meta: {
        key: string
        field: string
        prefix: ModelPrefix
        prevState: GlobalState
    }
    payload: {
        fieldName: string
        value: Record<string, unknown>
    }
}

export function* resolveOnUpdateModel({ type, meta, payload }: ResolveOnUpdateModel) {
    const { key: datasource, field, prefix, prevState } = meta

    // the updated model
    const { value } = payload
    // prev model
    const prevValue = get(prevState, `models.${prefix}.${datasource}.${field}`)

    if (isEqual(value, prevValue)) { return }

    const model: Record<string, unknown> = yield select(getModelByPrefixAndNameSelector(prefix, datasource))
    // @ts-ignore FIXME: Поправить типы
    const prevModel: Record<string, unknown> = getModelByPrefixAndNameSelector(prefix, datasource)(prevState || {})
    const forms: Form[] = yield select(makeFormsByModel(datasource, prefix))

    for (const form of forms) {
        const fieldName = type === registerFieldExtra.type ? payload.fieldName : field

        if (isEmpty(form.fields)) { return }

        for (const [fieldId, field] of Object.entries(form.fields)) {
            if (field.dependency) {
                for (const dep of field.dependency) {
                    if (shouldBeResolved(dep, fieldName, model, prevModel)) {
                        yield fork(modify, form, model, fieldId, field, dep)
                    }
                }
            }
        }
    }
}

export function* resolveOnInit({ payload }: RegisterFieldAction) {
    yield delay(16)

    const { formName, fieldName } = payload

    const form: Form = yield select(makeFormByName(formName))
    const model: Record<string, unknown> =
        yield select(getModelByPrefixAndNameSelector(form.modelPrefix, form.datasource))

    if (isEmpty(form.fields)) { return }

    for (const [fieldId, field] of Object.entries(form.fields)) {
        if (field.dependency) {
            for (const dependency of field.dependency) {
                const { applyOnInit } = dependency

                if ((fieldName === fieldId) && applyOnInit) {
                    yield fork(modify, form, model, fieldId, field, dependency)
                }
            }
        }
    }
}

function* resolveOnSetModel({ payload, meta = {} }: SetModelAction) {
    const { prefix, key: datasource, model, isDefault } = payload

    if (prefix === ModelPrefix.source || prefix === ModelPrefix.selected || !model) {
        return
    }

    const { prevState } = meta
    const forms: Form[] = yield select(makeFormsByModel(datasource, prefix))
    // @ts-ignore FIXME: Поправить типы
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
    takeEvery(registerFieldExtra, resolveOnInit),
    takeEvery([
        updateModel,
        appendFieldToArray,
        removeFieldFromArray,
        copyFieldArray,
    ], resolveOnUpdateModel),
    // @ts-ignore Проблема с типизацией saga
    takeEvery(setModel, resolveOnSetModel),
]
