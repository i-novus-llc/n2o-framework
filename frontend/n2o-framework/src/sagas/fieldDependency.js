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
import some from 'lodash/some'
import includes from 'lodash/includes'
import get from 'lodash/get'
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
    addFieldMessage,
    removeFieldMessage,
    registerFieldExtra,
} from '../ducks/form/store'
import { FETCH_VALUE } from '../core/api'
import { dataProviderResolver } from '../core/dataProviderResolver'
import { evalResultCheck } from '../utils/evalResultCheck'
import * as presets from '../core/validation/presets'

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

        const isMultiModel = get(response, 'list').length > 1

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
            if (evalResult === undefined || isEqual(evalResult, values[fieldName])) {
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
            const state = yield select()
            const fieldValidationList = get(state, [
                'widgets',
                formName,
                'validation',
                fieldName,
            ])
            const currentMessage = get(field, ['message', 'text'])
            let i = 0

            while (i < fieldValidationList.length) {
                const fieldValidation = fieldValidationList[i]
                const { type } = fieldValidation

                if (type === 'condition' && presets[type]) {
                    const isValid = presets[type](fieldName, values, fieldValidation)

                    if (!isValid) {
                        if (fieldValidation.text !== currentMessage) {
                            const message = {
                                severity: fieldValidation.severity,
                                text: fieldValidation.text,
                            }

                            yield put(addFieldMessage(formName, fieldName, message))
                        }

                        break
                    }
                }

                i += 1
            }

            if (i === fieldValidationList.length && currentMessage) {
                yield put(removeFieldMessage(formName, fieldName))
            }

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

export function* checkAndModify(
    values,
    fields,
    formName,
    fieldName,
    actionType,
) {
    // eslint-disable-next-line no-restricted-syntax
    for (const fieldId of Object.keys(fields)) {
        const field = fields[fieldId]

        if (field.dependency) {
            // eslint-disable-next-line no-restricted-syntax
            for (const dep of field.dependency) {
                if (
                    (
                        dep.applyOnInit && (
                            (actionType === actionTypes.INITIALIZE) ||
                            (actionType === actionTypes.RESET) ||
                            ((actionType === registerFieldExtra.type) && (fieldName === fieldId))
                        )
                    ) ||
                    (
                        some(
                            dep.on,
                            field => (
                                field === fieldName ||
                                (includes(field, '.') && includes(field, fieldName))
                            ),
                        ) && actionType === actionTypes.CHANGE
                    )

                ) {
                    yield fork(modify, values, formName, fieldId, dep, field)
                }
            }
        }
    }
}

export function* resolveDependency(action) {
    try {
        const { form: formName, field: fieldName } = action.meta
        const form = yield select(makeFormByName(formName))

        if (isEmpty(form) || isEmpty(form.registeredFields)) {
            return
        }

        yield call(
            checkAndModify,
            form.values || {},
            form.registeredFields,
            formName,
            action.type === registerFieldExtra.type ? action.payload.name : fieldName,
            action.type,
        )
    } catch (e) {
        // todo: падает тут из-за отсутствия формы
        // eslint-disable-next-line no-console
        console.error(e)
    }
}

export function* catchAction() {
    yield takeEvery(actionTypes.INITIALIZE, resolveDependency)
    yield takeEvery(registerFieldExtra.type, resolveDependency)
    yield takeEvery(actionTypes.CHANGE, resolveDependency)
    yield takeEvery(actionTypes.RESET, resolveDependency)
}

export const fieldDependencySagas = [catchAction]
