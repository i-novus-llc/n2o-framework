import get from 'lodash/get'

import evalExpression, { parseExpression } from '../../utils/evalExpression'

import { presets } from './presets'
import { VALIDATION_SEVERITY_PRIORITY as SEVERITY_PRIORITY } from './const'
import type { Validation, ValidationResult } from './types'
import { Severity } from './types'

export interface ValidateField {
    allMessages: Record<string, ValidationResult[]>,
    model: { index?: string | number },
    validationKey: string,
    fields?: string[]
    validationList: Validation[],
    signal?: AbortSignal,
    datasourceId?: string,
    pageUrl?: string | null,
}

export async function validateField(options: ValidateField): Promise<ValidationResult[]> {
    const errors: ValidationResult[] = []

    const {
        validationKey, model, validationList,
        signal, datasourceId, pageUrl } = options

    const validations = validationList.filter((validation) => {
        if (typeof presets[validation.type] !== 'function') {
            // eslint-disable-next-line no-console
            console.warn(`Validation error: not found preset for type="${validation.type}", field="${String(validationKey)}"`)

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
            const valid = await validationFunction(
                validationKey, model, { ...validation, signal, datasourceId, pageUrl },
            )

            if (typeof valid !== 'boolean') {
                const { text, severity } = valid

                errors.push({ text, severity })
            } else if (!valid) {
                const expression = parseExpression(validation.text)
                const currentModel = get(model, validationKey, null) ? model : { ...model, [validationKey]: '' }
                const text = expression
                    ? evalExpression<string>(expression, currentModel) || ''
                    : validation.text

                errors.push({ text, severity: validation.severity })
            }
        } catch (error) {
            // eslint-disable-next-line no-console
            console.warn(`validate error: ${error instanceof Error ? error.message : error}`)
        }
    }

    return errors.sort((first, second) => SEVERITY_PRIORITY[first.severity] - SEVERITY_PRIORITY[second.severity])
}

export const hasError = (messages: ValidationResult[]): boolean => messages.some(message => (
    message.severity === Severity.danger
))
