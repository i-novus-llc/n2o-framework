// TODO move to form/sagas
import {
    call,
    put,
    select,
    fork,
    takeEvery,
    delay,
    debounce,
} from 'redux-saga/effects'
import { createAction } from '@reduxjs/toolkit'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import get from 'lodash/get'
import set from 'lodash/set'

import { executeExpression } from '../core/Expression/execute'
import { parseExpression } from '../core/Expression/parse'
import { makeFormByName, makeFormsByModel, makeFormsByModelLink } from '../ducks/form/selectors'
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
import {
    appendToArray,
    combineModels,
    copyFieldArray,
    removeFromArray,
    setModel,
    updateModel,
} from '../ducks/models/store'
import { getModelByPrefixAndNameSelector, getModelSelector, Model } from '../ducks/models/selectors'
import { addAlert } from '../ducks/alerts/store'
import { GLOBAL_KEY } from '../ducks/alerts/constants'
import { ModelPrefix } from '../core/models/types'
import { getFieldPath } from '../core/models/getModelPath'
import { FETCH_TRIGGER } from '../core/dependencies/constants'
import { State as GlobalState } from '../ducks/State'
import { Form, Field, FieldDependency } from '../ducks/form/types'
import { RegisterFieldAction } from '../ducks/form/Actions'
import type {
    AppendToArrayAction, CopyFieldArrayAction,
    MergeModelAction, RemoveFromArrayAction,
    SetModelAction, UpdateModelAction,
} from '../ducks/models/Actions'
import { State } from '../ducks/models/Models'
import { logger } from '../utils/logger'

import fetchSaga from './fetch'

const FetchValueCache = new Map()

// TODO возможно это должно находиться в data provider resolver
function encodeTemplateUrl(url: string): string {
    return url.replace(/#{([^{}]*{?[^{}]*)+}/g, (match) => {
        const inner = match.slice(2, -1) // Убираем "#{" и "}"

        return `%23%7B${encodeURIComponent(inner)}%7D`
    })
}

export function* fetchValue(
    model: Model | Model[],
    { formName, modelLink }: Form,
    field: string,
    { dataProvider, valueFieldId = '' }: FieldDependency,
    evalContext: Record<string, unknown>,
    validate = true,
) {
    const fetchValueKey = `${formName}.${field}`

    try {
        yield delay(300)
        yield put(setFieldLoading(formName, field, true))
        const state: GlobalState = yield select()
        // @ts-ignore ignore js file typing
        const { url, headersParams, baseQuery, basePath } = dataProviderResolver(state, { ...dataProvider, evalContext })

        if (isEqual(baseQuery, FetchValueCache.get(fetchValueKey))) { return }

        FetchValueCache.set(fetchValueKey, baseQuery)

        const response: { list: Model[] } =
            yield call(fetchSaga, FETCH_VALUE, {
                url: encodeTemplateUrl(url),
                headers: headersParams,
                baseQuery,
                basePath,
            })

        const isMultiModel = get(response, 'list', []).length > 1

        const responseModel = isMultiModel
            ? get(response, 'list', null) as Model[] | null
            : get(response, 'list[0]', null) as Model | null

        const currentModel = isMultiModel ? responseModel : get(responseModel, valueFieldId, null)
        const prevFieldValue = get(model, field)
        const nextFieldValue = valueFieldId ? currentModel : responseModel

        if (!isEqual(prevFieldValue, nextFieldValue)) {
            yield put(updateModel(modelLink, field, nextFieldValue, validate))
        }
    } catch (error) {
        if (get(model, field)) {
            yield put(updateModel(modelLink, field, null, validate))
        }

        const messages = get(error, ['json', 'error', 'meta', 'alert', 'message'])

        if (messages) {
            const [alert] = messages
            const { placement } = alert || GLOBAL_KEY

            yield put(addAlert(placement, alert))
        }

        logger.error(error)
    } finally {
        yield put(setFieldLoading(formName, field, false))
        FetchValueCache.delete(fetchValueKey)
    }
}

function checkCondition(
    condition: string | boolean | void,
    values: Model | Model[],
    ctx?: Record<string, unknown>,
): boolean {
    if (condition === undefined) { return true }
    if (typeof condition === 'string') {
        const exp = parseExpression(condition) || condition

        return !!executeExpression<boolean>(exp, values, ctx)
    }

    return condition
}

const ResolveDependencyAction = createAction('n2o/form/resolveDependency')
let defModels: Partial<State> = {}

// eslint-disable-next-line complexity
export function* resolveDependency(
    form: Form,
    model: Model | Model[],
    fieldName: string,
    field: Field,
    dependency: FieldDependency,
    isInit: boolean,
) {
    const { formName, modelLink } = form
    const { type, expression, validate, enabled: condition } = dependency

    if (!checkCondition(condition, model, field.ctx)) { return }

    const evalResult = expression && executeExpression<boolean | string>(expression, model, field.ctx)

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
            if (evalResult === undefined || isEqual(evalResult, get(model, fieldName))) {
                break
            }

            if (isInit) {
                set(defModels, getFieldPath({ ...modelLink, field: fieldName }), evalResult)

                yield put(ResolveDependencyAction())
            } else {
                yield put(updateModel(modelLink, fieldName, evalResult, validate))
            }

            break
        }
        case 'reset': {
            if (get(model, fieldName, null) !== null) {
                yield put(updateModel(modelLink, fieldName, null, validate))
            }

            break
        }
        case 'required': {
            const currentRequired = field.required
            const nextRequired = Boolean(evalResult)

            if (currentRequired === nextRequired) { break }

            yield put(setFieldRequired(formName, fieldName, nextRequired, !isInit && validate))

            break
        }
        case 'fetchValue': {
            const { ctx = {}, visible } = field

            if (visible) {
                yield fetchValue(model, form, fieldName, dependency, ctx, !isInit && validate)
            }

            break
        }
        /* FIXME Временное решение, fetch с помощью dataProvider
            нужно перенести _fetchData из withFetchData в middleware
             fetchTrigger используется в withObserveDependency
        **/
        case 'fetch': {
            if (field.visible) {
                yield put(dangerouslySetFieldValue(
                    formName,
                    fieldName,
                    FETCH_TRIGGER,
                    Math.random(),
                ))
            }

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
const shouldBeResolved = <T extends Model | Model[]>(
    dependency: FieldDependency,
    actionField: string,
    model: T,
    prevModel: T,
    checkInner = true,
) => {
    const { on } = dependency

    return on?.some(dependencyField => (
        // Путь полностью совпал
        dependencyField === actionField ||
        // подписка на внешнее поле, изменилось внутренее: field="field[index].id", on="field"
        isParentFieldOf(dependencyField, actionField) ||
        // Подписка на внутрнее поле, изменилось внешнее: field="field", on="field[index].id"
        (
            checkInner &&
            isParentFieldOf(actionField, dependencyField) &&
            !isEqual(get(model, dependencyField), get(prevModel, dependencyField))
        )
    ))
}

function* resolveOnUpdateList(action: AppendToArrayAction | RemoveFromArrayAction | CopyFieldArrayAction) {
    const { meta, payload } = action
    const { fieldName } = payload

    if (!fieldName) { return }

    yield resolveOnUpdateModel({ meta, payload } as UpdateModelAction, false)
}

export function* resolveOnUpdateModel({ meta = {}, payload }: UpdateModelAction, checkInner = true) {
    const { prevState } = meta
    const { modelLink, fieldName } = payload
    const state: GlobalState = yield select()
    const selector = getModelSelector(modelLink)
    const prevModel = prevState ? selector(prevState) || ({} as Model) : {}
    const model = selector(state) || ({} as Model)

    if (isEqual(get(model, fieldName), get(prevModel, fieldName))) { return }

    const forms: Form[] = yield select(makeFormsByModelLink(modelLink))

    for (const form of forms) {
        for (const [fieldId, field] of Object.entries(form.fields)) {
            if (!field.dependency) { continue }
            for (const dep of field.dependency) {
                if (shouldBeResolved(dep, fieldName, model, prevModel, checkInner)) {
                    yield fork(resolveDependency, form, model, fieldId, field, dep, false)
                }
            }
        }
    }
}

export function* resolveOnInit({ payload }: RegisterFieldAction) {
    const { formName, fieldName } = payload
    const state: GlobalState = yield select()
    const form = makeFormByName(formName)(state)

    if (!form || isEmpty(form.fields)) { return }
    const model = getModelSelector(form.modelLink)(state) || ({} as Model)
    const field = form.fields?.[fieldName]

    if (!field?.dependency) { return }

    for (const dependency of field.dependency) {
        const { applyOnInit } = dependency

        if (applyOnInit) {
            yield fork(resolveDependency, form, model, fieldName, field, dependency, true)
        }
    }
}

function* compareAndResolve<T extends Model | Model[]>(
    datasource: string,
    prefix: ModelPrefix,
    model: T,
    prevModel: T,
    isDefault: boolean,
) {
    const forms: Form[] = yield select(makeFormsByModel(datasource, prefix))

    for (const form of forms) {
        for (const [fieldId, field] of Object.entries(form.fields)) {
            const { dependency = [] } = field

            // Обход каждой зависимости
            for (const dep of dependency) {
                const { on = [], applyOnInit } = dep

                if (isDefault && !applyOnInit) { continue }

                const isSomeFieldChanged = on.some((fieldPath: string) => {
                    const currentValue = get(model, fieldPath)
                    const prevValue = get(prevModel, fieldPath)

                    return !isEqual(currentValue, prevValue)
                })

                if (isSomeFieldChanged) {
                    yield fork(resolveDependency, form, model, fieldId, field, dep, isDefault)
                }
            }
        }
    }
}

function* resolveOnSetModel({ payload, meta = {} }: SetModelAction) {
    const { modelLink, model, isDefault } = payload
    const { prefix, id: datasource } = modelLink

    if (!model) { return }

    /*
     * Костыль-задержка.
     * При удалении строк мультисета через действия edit-list/set-value не успевают удалиться поля строки
     */
    yield delay(16)

    const { prevState } = meta
    // @ts-ignore FIXME: Поправить типы
    const prevModel = getModelByPrefixAndNameSelector(prefix, datasource)(prevState || {})

    yield compareAndResolve(datasource, prefix, model, prevModel, !!isDefault)
}

function* resolveOnDefault({ payload, meta = {} }: MergeModelAction) {
    const { combine } = payload
    const { prevState = {} } = meta

    for (const [prefix, models] of Object.entries(combine)) {
        for (const id of Object.keys(models)) {
            const model: Model | Model[] = yield select(getModelByPrefixAndNameSelector(prefix as ModelPrefix, id))
            // @ts-ignore FIXME: Поправить типы
            const prevModel = getModelByPrefixAndNameSelector(prefix, id)(prevState)

            yield compareAndResolve(
                id,
                prefix as ModelPrefix,
                model as Model | Model[],
                prevModel as Model | Model[],
                true,
            )
        }
    }
}

export const fieldDependencySagas = [
    takeEvery(registerFieldExtra.type, resolveOnInit),
    takeEvery([
        appendToArray,
        removeFromArray,
        copyFieldArray,
    ], resolveOnUpdateList),
    takeEvery(updateModel.type, resolveOnUpdateModel),
    takeEvery(setModel.type, resolveOnSetModel),
    takeEvery(combineModels.type, resolveOnDefault),
    /*
     * Если кто-то перетёр (пришли данные из запроса) модель раньше,
     * чем сработало схлопывание дефолтных зависимостей,
     * то надо сбросить буфер, чтобы он несработал позже на неактуальных данных
     */
    takeEvery(setModel.type, ({ payload }: SetModelAction<ModelPrefix.source>) => {
        const { modelLink, model } = payload
        const { prefix, id: key } = modelLink

        if (!model) {
            delete defModels[prefix]?.[key]

            return
        }
        if (prefix !== ModelPrefix.source) { return }

        for (const [formPrefix, defaultModel] of Object.entries(defModels) as Array<[ModelPrefix, Model]>) {
            if (defaultModel[key]) {
                // чистим только пересекающиеся поля
                for (const field of Object.keys(model[0] || {})) {
                    // @ts-ignore defModels[prefix][key] объявлен как object и ts не даёт обращаться по ключу в нём
                    delete defModels[formPrefix][key][field]
                }

                if (isEmpty(defaultModel[key])) { delete defaultModel[key] }
            }
            if (isEmpty(defModels[formPrefix])) { delete defModels[formPrefix] }
        }
    }),
    debounce(50, ResolveDependencyAction, function* combineDefault() {
        if (!isEmpty(defModels)) {
            yield put(combineModels(defModels))

            defModels = {}
        }
    }),
]
