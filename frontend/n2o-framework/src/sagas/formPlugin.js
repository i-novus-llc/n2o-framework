import { takeEvery, put, select } from 'redux-saga/effects'
import { touch, change, actionTypes } from 'redux-form'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'
import values from 'lodash/values'
import includes from 'lodash/includes'
import merge from 'lodash/merge'
import setWith from 'lodash/setWith'
import isArray from 'lodash/isArray'
import isFunction from 'lodash/isFunction'

import { addFieldMessage, removeFieldMessage } from '../actions/formPlugin'
import { makeFormByName, messageSelector } from '../selectors/formPlugin'
import { getWidgetFieldValidation } from '../selectors/widgets'
import { setModel } from '../actions/models'
import { SET_REQUIRED, UNSET_REQUIRED } from '../constants/formPlugin'
import { COPY } from '../constants/models'
import {
    makeGetModelByPrefixSelector,
    modelsSelector,
} from '../selectors/models'
import evalExpression, { parseExpression } from '../utils/evalExpression'
import * as validationPresets from '../core/validation/presets'

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
    for (const validationOption of widgetValidation) {
        if (validationOption.multi) {
            // ToDo: Делаю пока только для формы
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
                    // Add form mesage
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

    const path = target.field
    const treePath = includes(path, '.')

    const withTreeObject = (path, sheetValue) => setWith({}, path, sheetValue)

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
        if (sourceModel) {
            newModel = target.field
                ? merge({}, targetModel, withTreeObject(path, sourceModel))
                : sourceModel
        } else {
            newModel = {}
        }
    } else {
        newModel = target.field
            ? {
                ...targetModel,
                [target.field]: sourceModel,
            }
            : sourceModel
    }

    yield put(change(target.key, target.field, get(newModel, path)))
    yield put(setModel(target.prefix, target.key, newModel))
}

export const formPluginSagas = [
    takeEvery(
        [actionTypes.START_ASYNC_VALIDATION, actionTypes.CHANGE],
        removeMessage,
    ),
    takeEvery([SET_REQUIRED, UNSET_REQUIRED], checkFieldValidation),
    takeEvery(action => action.meta && action.meta.isTouched, addTouched),
    takeEvery(COPY, copyAction),
]
