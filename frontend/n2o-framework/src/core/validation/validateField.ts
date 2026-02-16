import get from 'lodash/get'

import evalExpression, { parseExpression } from '../../utils/evalExpression'
import { logger } from '../../utils/logger'

import { presets } from './presets'
import { VALIDATION_SEVERITY_PRIORITY as SEVERITY_PRIORITY } from './const'
import type { ExtraValidationConfig, Validation, ValidationResult } from './types'
import { Severity } from './types'

export async function validateField(
    fieldId: string,
    model: Record<string, unknown>,
    validations: Validation[],
    options: ExtraValidationConfig,
): Promise<ValidationResult[]> {
    const messages: ValidationResult[] = []

    const filtered = validations.filter((validation) => {
        if (typeof presets[validation.type] !== 'function') {
            logger.warn(`Validation error: not found preset for type="${validation.type}", field="${String(fieldId)}"`)

            return false
        }

        const conditions = validation.enablingConditions

        if (conditions.length) {
            return conditions.every(conditions => evalExpression(conditions, model))
        }

        return true
    })

    for (const validation of filtered) {
        const validationFunction = presets[validation.type]

        try {
            const valid = await validationFunction(fieldId, model, { ...validation, ...options })

            if (typeof valid !== 'boolean') {
                messages.push(valid)
            } else if (!valid) {
                const expression = parseExpression(validation.text)
                const currentModel = get(model, fieldId, null) ? model : { ...model, [fieldId]: '' }
                const text = expression
                    ? evalExpression<string>(expression, currentModel) || ''
                    : validation.text

                messages.push({ text, severity: validation.severity })
            }
        } catch (error) {
            logger.warn(`validate error: ${error instanceof Error ? error.message : error}`)
        }
    }

    return messages.sort((first, second) => SEVERITY_PRIORITY[first.severity] - SEVERITY_PRIORITY[second.severity])
}

const severityLevel: Record<Severity, Number> = {
    [Severity.danger]: 2,
    [Severity.warning]: 1,
    [Severity.success]: 0,
}

export const hasError = (
    messages: ValidationResult[],
    severity: Severity = Severity.danger,
): boolean => messages.some(message => (
    severityLevel[message.severity] >= severityLevel[severity]
))
