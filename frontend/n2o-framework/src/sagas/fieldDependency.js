import {
    call,
    put,
    select,
    take,
    fork,
    takeEvery,
    delay,
    cancel,
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
    registerFieldExtra,
} from '../ducks/form/store'
import { FETCH_VALUE } from '../core/api'
import { dataProviderResolver } from '../core/dataProviderResolver'
import { evalResultCheck } from '../utils/evalResultCheck'
import { startValidate } from '../ducks/datasource/store'
import { makeDatasourceIdSelector, makeWidgetByIdSelector } from '../ducks/widgets/selectors'

import fetchSaga from './fetch'

export function* fetchValue(form, field, { dataProvider, valueFieldId }) {
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
    }
}

// eslint-disable-next-line complexity
export function* modify(values, formName, fieldName, dependency = {}, field) {
    const { type, expression, on } = dependency

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
            const datasource = yield select(makeDatasourceIdSelector(formName))
            const form = yield select(makeWidgetByIdSelector(formName))
            const model = get(form, [
                'form',
                'modelPrefix',
            ])

            yield delay(50)
            yield put(startValidate(datasource, [fieldName], model, { touched: true }))

            break
        }
        case 'fetchValue': {
            const watcher = yield fork(fetchValue, formName, fieldName, dependency)
            const action = yield take((action) => {
                if (action.type !== actionTypes.CHANGE) { return false }
                // TODO Выяснить может ли fetch быть без зависимостей, если нет, то убрать
                if (!on || !on.length) { return true }

                return on.includes(get(action, 'meta.field'))
            })

            // TODO разобраться с этим условием, не уверен что актуально
            if (get(action, 'meta.field') !== fieldName) {
                yield cancel(watcher)
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
                if (
                    (
                        dep.applyOnInit && (
                            (actionType === actionTypes.INITIALIZE) ||
                            ((actionType === registerFieldExtra.type) && (fieldName === fieldId))
                        )
                    ) ||
                    some(
                        dep.on,
                        field => (
                            field === fieldName ||
                            (includes(field, '.') && includes(field, fieldName))
                        ),
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

export const fieldDependencySagas = [
    takeEvery([
        actionTypes.INITIALIZE,
        registerFieldExtra.type,
        actionTypes.CHANGE,
    ], resolveDependency),
]
