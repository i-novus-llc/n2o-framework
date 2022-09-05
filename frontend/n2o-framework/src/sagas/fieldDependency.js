import {
    call,
    put,
    select,
    fork,
    takeEvery,
    takeLatest,
    delay,
    cancel,
} from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import some from 'lodash/some'
import includes from 'lodash/includes'
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

import fetchSaga from './fetch'

const FetchValueCache = new Set()

export function* fetchValue(form, field, { dataProvider, valueFieldId }, cleanUp) {
    try {
        yield delay(300)
        yield put(setLoading(form, field, true))
        const state = yield select()
        const { url, headersParams } = yield call(
            dataProviderResolver,
            state,
            dataProvider,
        )
        const response = yield call(fetchSaga, FETCH_VALUE, {
            url,
            headers: headersParams,
        })

        const isMultiModel = get(response, 'list').length > 1

        const model = isMultiModel
            ? get(response, 'list', null)
            : get(response, 'list[0]', null)

        const currentModel = isMultiModel ? model : model[valueFieldId]

        if (model) {
            yield put(
                change(form, field, {
                    keepDirty: true,
                    value: valueFieldId ? currentModel : model,
                }),
            )
        }
    } catch (e) {
        yield put(
            change(form, field, {
                keepDirty: true,
                value: null,
                error: true,
            }),
        )
        // eslint-disable-next-line no-console
        console.error(e)
    } finally {
        yield put(setLoading(form, field, false))
        cleanUp()
    }
}

// eslint-disable-next-line complexity
export function* modify(values, formName, fieldName, dependency = {}, field) {
    const { type, expression } = dependency

    const evalResult = expression
        ? evalExpression(expression, values)
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
            const model = get(form, [
                'form',
                'modelPrefix',
            ])

            yield delay(50)
            yield put(startValidate(formName, [fieldName], model, { touched: true }))

            break
        }
        case 'fetchValue': {
            const fetchValueKey = `${formName}.${fieldName}`
            const task = yield fork(
                fetchValue,
                formName,
                fieldName,
                dependency,
                () => FetchValueCache.delete(fetchValueKey),
            )

            if (FetchValueCache.has(fetchValueKey)) {
                yield cancel(task)
            } else {
                FetchValueCache.add(fetchValueKey)
            }

            break
        }
        default:
            break
    }
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

                if (
                    needRunOnInit ||
                    (someDepNeedRun && (actionType === actionTypes.CHANGE || actionType === actionTypes.INITIALIZE))
                ) {
                    yield fork(modify, values, formName, fieldId, dep, field)
                }
            }
        }
    }
}

export function* resolveDependency({ type, meta, payload }) {
    try {
        const { form: formName, field: fieldName } = meta
        const form = yield select(makeFormByName(formName))
        const fieldKeys = type === actionTypes.INITIALIZE ? keys(form.registeredFields) : null

        if (isEmpty(form) || isEmpty(form.registeredFields)) {
            return
        }

        if (fieldKeys) {
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
