import isEqual from 'lodash/isEqual'
import isArray from 'lodash/isArray'
import isBoolean from 'lodash/isBoolean'
import isFunction from 'lodash/isFunction'
import each from 'lodash/each'
import forEach from 'lodash/forEach'
import isEmpty from 'lodash/isEmpty'
import find from 'lodash/find'
import pickBy from 'lodash/pickBy'
import get from 'lodash/get'
import compact from 'lodash/compact'
import map from 'lodash/map'
import has from 'lodash/has'
import flatten from 'lodash/flatten'
import { batchActions } from 'redux-batched-actions'

import { isPromise } from '../../tools/helpers'
import { addFieldMessage, removeFieldMessage } from '../../ducks/form/store'

import * as presets from './presets'

/**
 * @typedef {Object} ValidationMessage
 * @property {'danger'|'warning'|'success'} severity
 * @property {string} text
 */

/**
 * @typedef {Record<string, Array<ValidationMessage>>} ValidationResult
 */

/**
 * @typedef {Object} ValidationConfig
 * @property {'danger'|'warning'|'success'} severity
 * @property {string} text
 * @property {string} validationKey
 * @property {string|'required'} type
 * TODO Заполнить нормально все поля
 */

/**
 * @param {Array<ValidationMessage>} messages
 * @return {ValidationMessage | void}
 */
function findPriorityMessage(messages) {
    return (
        find(messages, { severity: 'danger' }) ||
        find(messages, { severity: 'warning' }) ||
        find(messages, { severity: 'success' })
    )
}

/**
 * есть ли ошибки или нет
 * @param {ValidationResult} messages
 * @returns {boolean}
 */
function hasError(messages) {
    return flatten(Object.values(messages)).reduce((res, msg) => msg.severity === 'danger' || res, false)
}

/**
 * @param {string} fieldId
 * @param {Object} message
 * @param {string|boolean} [message.text]
 * @param {'danger'|'warning'|'success'|boolean} [message.severity]
 * @param {ValidationMessage} options
 * @param {ValidationResult} errors
 * @return {ValidationResult} errors
 */
function addError(
    fieldId,
    { text = true, severity = true },
    options = {},
    errors,
) {
    if (!errors[fieldId]) {
        errors[fieldId] = []
    }

    errors[fieldId].push({
        text: isBoolean(text) ? options.text : text,
        severity: isBoolean(severity) ? options.severity : severity,
    })

    return errors
}

function getMultiFields(registeredFields, fieldId) {
    const regExp = new RegExp(`${fieldId}(\\[.*]).*$`)

    return compact(
        map(registeredFields, (field, fieldId) => (regExp.test(fieldId) ? fieldId : null)),
    )
}

/**
 * функция валидации
 * @param {Record<string, ValidationConfig>} validationConfig
 * @param {string} formName
 * @param {Object} state
 * @param {boolean} isTouched
 * @return {Function}
 */
export const validateField = (
    validationConfig,
    formName,
    state,
    isTouched = false,
) => (values, dispatch) => validate(validationConfig, formName, state, isTouched, values, dispatch)

export const getStoreValidator = (
    validationConfig,
    formName,
    store,
) => (values, dispatch) => {
    const state = store ? store.getState() : {}
    const stateValues = get(state, ['form', formName, 'values'])

    /*
     * TODO: разобраться
     * при изменении данных формы из кода
     * в asyncChangeFields приходят не актуальный values
     * поэтому данные берём из стора сами
     */
    return validate(validationConfig, formName, state, false, stateValues, dispatch)
}

/**
 * @param {Record<string, ValidationConfig>} validationConfig
 * @param {string} formName
 * @param {Object} state
 * @param {boolean} isTouched
 * @param {Object} values
 * @param {Function} dispatch
 * @return {Promise<boolean>} has error
 */
export function validate(
    validationConfig,
    formName,
    state,
    isTouched = false,
    values,
    dispatch,
) {
    const registeredFields = get(state, ['form', formName, 'registeredFields'])
    const validation = pickBy(validationConfig, (value, key) => get(registeredFields, `${key}.visible`, true))
    const errors = {}
    const promiseList = [Promise.resolve()]

    each(validation, (validationList, fieldId) => {
        if (isArray(validationList)) {
            each(validationList, (options) => {
                const resolveValidationResult = (isValid, fieldId) => {
                    if (isPromise(isValid)) {
                        promiseList.push(
                            new Promise((resolve) => {
                                isValid.then(
                                    (resp) => {
                                        each(resp && resp.message, (message) => {
                                            addError(fieldId, message, options, errors)
                                        })
                                        resolve()
                                    },
                                    () => {
                                        resolve()
                                    },
                                )
                            }),
                        )
                    } else if (!isValid) {
                        addError(fieldId, {}, options, errors)
                    }
                }

                const validationFunction = presets[options.type]

                const callValidationFunction = (fieldId) => {
                    const isVisibleField = get(registeredFields, [`${fieldId}`, 'visible'], true)

                    if (isVisibleField) {
                        const isValid = isFunction(validationFunction) &&
                        validationFunction(fieldId, values, options, dispatch)

                        resolveValidationResult(isValid, fieldId)
                    } else {
                        resolveValidationResult(true, fieldId)
                    }
                }

                if (options.multi) {
                    const multiFields = getMultiFields(registeredFields, fieldId) || []

                    forEach(multiFields, fieldId => callValidationFunction(fieldId))
                } else {
                    callValidationFunction(fieldId)
                }
            })
        }
    })

    return Promise.all(promiseList).then(() => {
        // eslint-disable-next-line consistent-return
        const messagesAction = map(errors, (messages, fieldId) => {
            if (!isEmpty(messages)) {
                const message = findPriorityMessage(messages)
                const nowTouched = get(registeredFields, [fieldId, 'touched'])

                if (
                    (isTouched && !nowTouched) ||
                    !isEqual(message, get(registeredFields, [fieldId, 'message']))
                ) {
                    return addFieldMessage(formName, fieldId, message, isTouched)
                }
            }
        }).filter(Boolean)

        each(registeredFields, (field, key) => {
            const currentError = has(errors, key)
            const errorInStore = field.message

            if (!currentError && errorInStore) {
                dispatch(removeFieldMessage(formName, key))
            }
        })

        if (messagesAction) {
            dispatch(batchActions(messagesAction))
        }

        return hasError(errors)
    })
}

export default function createValidator(
    validationConfig = {},
    formName,
    store,
    fields,
) {
    return {
        asyncValidate: getStoreValidator(validationConfig, formName, store),
        asyncBlurFields: fields || [],
    }
}
