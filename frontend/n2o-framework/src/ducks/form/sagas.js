import { takeEvery, put, select, debounce, call } from 'redux-saga/effects'
import { touch, change, actionTypes, focus } from 'redux-form'
import get from 'lodash/get'
import set from 'lodash/set'
import isEmpty from 'lodash/isEmpty'
import values from 'lodash/values'
import includes from 'lodash/includes'
import merge from 'lodash/merge'
import isArray from 'lodash/isArray'
import isFunction from 'lodash/isFunction'
import keys from 'lodash/keys'

import { tabTraversal } from '../regions/sagas'
import { setModel, copyModel } from '../models/store'
import {
    makeGetModelByPrefixSelector,
    modelsSelector,
} from '../models/selectors'
import { getWidgetFieldValidation } from '../widgets/selectors'
import evalExpression, { parseExpression } from '../../utils/evalExpression'
import * as validationPresets from '../../core/validation/presets'
import { regionsSelector } from '../regions/store'

import { formsSelector, makeFormByName, messageSelector } from './selectors'
import { addMessage } from './constants'
import {
    failValidate,
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
    yield call(checkFieldValidation, { meta: { form: target.key, field: target.field } })
}

function* setFocus({ payload }) {
    const { form, name: fieldName, asyncValidating } = payload

    if (asyncValidating === fieldName) {
        return
    }

    const regions = yield select(regionsSelector)
    const forms = yield select(formsSelector)

    if (Object.values(forms).some(form => form.active)) {
        return
    }

    const allTabs = Object.values(regions)
        .filter(region => region.tabs)
        .map(({ tabs }) => tabs)

    for (const tabs of allTabs) {
        const isTargetFormInTabs = yield tabTraversal(null, tabs, null, form)

        if (isTargetFormInTabs) {
            let fieldName = ''

            const firstInvalidForm = tabs
                .find(({ invalid }) => invalid)
                ?.content
                .find(({ id }) => {
                    const { registeredFields = {} } = forms?.[id] || {}

                    return keys(registeredFields).some((currentFieldName) => {
                        const { message } = registeredFields[currentFieldName]

                        if (message) {
                            fieldName = currentFieldName

                            return true
                        }

                        return false
                    })
                })

            if (firstInvalidForm?.id) {
                yield put(focus(firstInvalidForm.id, fieldName))

                return
            }
        }
    }

    yield put(focus(form, fieldName))
}

export const formPluginSagas = [
    takeEvery(
        [actionTypes.START_ASYNC_VALIDATION, actionTypes.CHANGE],
        removeMessage,
    ),
    takeEvery([setRequired.type, unsetRequired.type], checkFieldValidation),
    takeEvery(action => action.meta && action.meta.isTouched, addTouched),
    takeEvery(failValidate, function* touchOnFailValidate({ payload }) {
        const { id, fields } = payload
        const keys = Object.keys(fields)

        yield put(touch(id, ...keys))
    }),
    takeEvery(copyModel.type, copyAction),
    debounce(100, addMessage, setFocus),
]
