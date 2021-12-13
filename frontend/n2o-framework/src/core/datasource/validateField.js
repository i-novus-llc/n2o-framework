import evalExpression from '../../utils/evalExpression'
import * as presets from '../validation/presets'

import { VALIDATION_SEVERITY_PRIORITY as SEVERITY_PRIORITY } from './const'

/**
 * @typedef {Object} Validation
 * @property {VALIDATION_SEVERITY} severity
 * @property {string} text
 * @property {string} validationKey
 * @property {string} type
 * @property {string | boolean} [enabled]
 * @property {string} [expression] only for type='condition'
 */
/**
 * @typedef {Object} ValidationResult
 * @property {VALIDATION_SEVERITY} severity
 * @property {string} text
 */

/**
 * @param {string} field
 * @param {object} model
 * @param {Validation[]} validationList
 * @return {ValidationResult[]}
 */
export function validateField(field, model, validationList) {
    const errors = []

    validationList.filter((validation) => {
        if (typeof validation.type !== 'function') {
            // eslint-disable-next-line no-console
            console.warn(`Validation error: not found preset for type="${validation.type}", field="${field}"`)

            return false
        }
        if (typeof validation.enabled === 'boolean') { return validation.enabled }
        if (typeof validation.enabled === 'string') { return evalExpression(validation.enabled, model) }

        return true
    })

    // eslint-disable-next-line no-restricted-syntax
    for (const validation of validationList) {
        const validationFunction = presets[validation.type]

        if (!validationFunction(field, model, validation)) {
            errors.push({
                text: validation.text,
                severity: validation.severity,
            })
        }
    }

    return errors.sort((first, second) => SEVERITY_PRIORITY[first.severity] - SEVERITY_PRIORITY[second.severity])
}
