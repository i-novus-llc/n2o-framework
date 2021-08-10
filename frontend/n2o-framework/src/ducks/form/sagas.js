import { takeEvery, put, select } from 'redux-saga/effects'
import { touch, change, actionTypes } from 'redux-form'
import get from 'lodash/get'
import set from 'lodash/set'
import isEmpty from 'lodash/isEmpty'
import values from 'lodash/values'
import includes from 'lodash/includes'
import merge from 'lodash/merge'
import isArray from 'lodash/isArray'
import isFunction from 'lodash/isFunction'

import { setModel, copyModel } from '../models/store'
import {
    makeGetModelByPrefixSelector,
    modelsSelector,
} from '../models/selectors'
import { getWidgetFieldValidation } from '../widgets/selectors'
import evalExpression, { parseExpression } from '../../utils/evalExpression'
import * as validationPresets from '../../core/validation/presets'

import { makeFormByName, messageSelector } from './selectors'
import {
    addFieldMessage,
    removeFieldMessage,
    setRequired,
    unsetRequired,
} from './store'

export function* removeMessage(action) {
    const state = yield select()

    const formName = get(action, 'meta.form')
    const fieldName = get(action, 'meta.field')

    if (formName && fieldName) {
        const message = yield select(messageSelector(formName, fieldName))

        const fieldValidation = getWidgetFieldValidation(
            state,
            formName,
            fieldName,
        )

        if (message && (!fieldValidation || isEmpty(fieldValidation))) {
            yield put(removeFieldMessage(formName, fieldName))
        }
    }
}

function* checkFieldValidation({ meta }) {
    const formName = meta.form
    const fieldName = meta.field
    const state = yield select()
    const formMessage = messageSelector(formName, fieldName)(state)
    const widgetValidation = getWidgetFieldValidation(state, formName, fieldName)

    if (!isArray(widgetValidation)) {
        return
    }

    let isValidResult = true

    // eslint-disable-next-line no-restricted-syntax
    for (const validationOption of widgetValidation) {
        if (validationOption.multi) {
            // ToDo: Делаю пока только для формы
            // eslint-disable-next-line no-continue
            continue
        }

        const validationFunction = validationPresets[validationOption.type]

        if (isFunction(validationFunction)) {
            const { values } = makeFormByName(formName)(state)
            const isValid = validationFunction(
                fieldName,
                values,
                validationOption,
                () => {},
            )

            if (!isValid) {
                isValidResult = false

                if (!formMessage) {
                    // Add form message
                    const message = {
                        text: validationOption.text,
                        severity: validationOption.severity,
                    }

                    yield put(addFieldMessage(formName, fieldName, message, false))
                }

                break
            }
        }
    }

    if (isValidResult && formMessage) {
        yield put(removeFieldMessage(formName, fieldName))
    }
}

export function* addTouched({ payload: { form, name } }) {
    yield put(touch(form, name))
}

export function* copyAction({ payload }) {
    const { target, source, mode = 'replace', sourceMapper } = payload
    const state = yield select(modelsSelector)
    let sourceModel = get(state, values(source).join('.'))
    const selectedTargetModel = yield select(
        makeGetModelByPrefixSelector(target.prefix, target.key),
    )
    const targetModel = selectedTargetModel || []
    const expression = parseExpression(sourceMapper)
    let newModel = {}
    const targetModelField = get(targetModel, [target.field], [])
    const treePath = includes(target.field, '.')

    if (expression) {
        sourceModel = evalExpression(expression, sourceModel)
    }

    if (mode === 'merge') {
        newModel = target.field
            ? {
                ...targetModel,
                [target.field]: {
                    ...targetModelField,
                    ...sourceModel,
                },
            }
            : { ...targetModel, ...sourceModel }
    } else if (mode === 'add') {
        if (!Array.isArray(sourceModel) || !Array.isArray(targetModelField)) {
            // eslint-disable-next-line no-console
            console.warn('Source or target is not an array!')
        }

        sourceModel = Object.values(sourceModel)

        newModel = target.field
            ? {
                ...targetModel,
                [target.field]: [...targetModelField, ...sourceModel],
            }
            : [...targetModelField, ...sourceModel]
    } else if (treePath) {
        newModel = merge({}, targetModel)
        set(newModel, target.field, sourceModel)
    } else {
        newModel = target.field
            ? {
                ...targetModel,
                [target.field]: sourceModel,
            }
            : sourceModel
    }

    const value = get(newModel, target.field)

    yield put(change(target.key, target.field, typeof value === 'undefined' ? null : value))
    yield put(setModel(target.prefix, target.key, newModel))
}

export const formPluginSagas = [
    takeEvery(
        [actionTypes.START_ASYNC_VALIDATION, actionTypes.CHANGE],
        removeMessage,
    ),
    takeEvery([setRequired.type, unsetRequired.type], checkFieldValidation),
    takeEvery(action => action.meta && action.meta.isTouched, addTouched),
    takeEvery(copyModel.type, copyAction),
]
