import { isEmpty } from 'lodash'

import { dataSourceModelsSelector, dataSourceValidationSelector } from '../../ducks/datasource/selectors'
import { failValidate } from '../../ducks/datasource/store'

import { MODEL_PREFIX } from './const'
import { validateField } from './validateField'

/**
 * Валидация datasource по стейту
 * FIXME практически полная копия validate саги, но с возвращаемым значением для использования
 *  сделано для быстрой реализации
 *  подумать как свести в одно и переделать
 * @param {object} state
 * @param {string} datasourceId
 * @param {Function} dispatch
 * @returns {boolean}
 */
export const validate = (
    state,
    datasourceId,
    dispatch,
    touched,
) => {
    const validation = dataSourceValidationSelector(datasourceId)(state)
    const models = dataSourceModelsSelector(datasourceId)(state)
    const model = models[MODEL_PREFIX.active]
    const entries = Object.entries(validation)

    const errors = {}

    entries.forEach(([field, validationList]) => {
        const messages = validateField(field, model, validationList || [])

        if (messages?.length) {
            errors[field] = messages
        }
    })

    if (isEmpty(errors)) {
        return true
    }

    dispatch(failValidate(datasourceId, errors, { touched }))

    return false
}
