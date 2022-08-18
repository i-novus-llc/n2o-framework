import { isEmpty } from 'lodash'

import { dataSourceModelsSelector, dataSourceValidationSelector } from '../../ducks/datasource/selectors'
import { failValidate, resetValidation } from '../../ducks/datasource/store'

import { MODEL_PREFIX, VALIDATION_SEVERITY } from './const'
import { validateField } from './validateField'

/**
 * Валидация datasource по стейту
 * FIXME практически полная копия validate саги, но с возвращаемым значением для использования
 *  сделано для быстрой реализации
 *  подумать как свести в одно и переделать
 * @param {object} state
 * @param {string} datasourceId
 * @param {MODEL_PREFIX} prefix
 * @param {Function} dispatch
 * @returns {boolean}
 */
export const validate = async (
    state,
    datasourceId,
    prefix = MODEL_PREFIX.active,
    dispatch = () => {},
    touched = false,
) => {
    const validation = dataSourceValidationSelector(datasourceId)(state)
    const models = dataSourceModelsSelector(datasourceId)(state)
    const model = models[prefix] || {}
    const entries = Object.entries(validation)

    dispatch(resetValidation(datasourceId, [], prefix))

    const allMessages = {}

    for (const [field, validationList] of entries) {
        const messages = await validateField(field, model, validationList || [])

        if (messages?.length) {
            allMessages[field] = messages
        }
    }

    const invalid = Object.values(allMessages).some(messages => messages.some(message => (
        message.severity === VALIDATION_SEVERITY.danger || message.severity === VALIDATION_SEVERITY.warning
    )))

    if (!isEmpty(allMessages)) {
        dispatch(failValidate(datasourceId, allMessages, prefix, { touched }))
    }

    return !invalid
}
