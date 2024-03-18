import evalExpression, { parseExpression } from '../../utils/evalExpression'

import { presets } from './presets'
import { VALIDATION_SEVERITY_PRIORITY as SEVERITY_PRIORITY } from './const'
import type { Validation, ValidationResult } from './types'
import { Severity } from './types'

export async function validateField<
    TData extends object = object,
    TKey extends keyof TData = keyof TData
>(
    field: TKey,
    model: TData,
    validationList: Validation[],
    signal?: AbortSignal,
): Promise<ValidationResult[]> {
    const errors: ValidationResult[] = []

    const validations = validationList.filter((validation) => {
        if (typeof presets[validation.type] !== 'function') {
            // eslint-disable-next-line no-console
            console.warn(`Validation error: not found preset for type="${validation.type}", field="${String(field)}"`)

            return false
        }

        const conditions = validation.enablingConditions

        if (conditions.length) {
            return conditions.every(conditions => evalExpression(conditions, model))
        }

        return true
    })

    for (const validation of validations) {
        const validationFunction = presets[validation.type]

        try {
            const valid = await validationFunction(field, model, { ...validation, signal })

            if (!valid) {
                const expression = parseExpression(validation.text)
                const text = expression
                    ? evalExpression<string>(expression, model) || ''
                    : validation.text

                errors.push({
                    text,
                    severity: validation.severity,
                })
            }
        } catch (error) {
            // eslint-disable-next-line no-console
            console.warn(`validate error: ${error instanceof Error ? error.message : error}`)
        }
    }

    return errors.sort((first, second) => SEVERITY_PRIORITY[first.severity] - SEVERITY_PRIORITY[second.severity])
}

export const hasError = (messages: ValidationResult[]): boolean => messages.some(message => (
    message.severity === Severity.danger || message.severity === Severity.warning
))
