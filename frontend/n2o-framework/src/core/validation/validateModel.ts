import { Severity } from './types'
import type { ExtraValidationConfig, Validation, ValidationResult } from './types'
import { getCtxByModel, getCtxFromField, isMulti } from './utils'
import { validateField, hasError as checkErrors } from './validateField'

// Валидация простого поля
const validateSimple = async (
    fieldId: string,
    model: Record<string, unknown>,
    validations: Validation[],
    options: ExtraValidationConfig,
    buffer: Record<string, ValidationResult[]>,
): Promise<void> => {
    const messages = await validateField(fieldId, model, validations, options)

    if (messages.length) {
        buffer[fieldId] = messages
    }
}

// Валидация всех строк мультисета
const validateMulti = async (
    validationKey: string,
    model: Record<string, unknown>,
    validations: Validation[],
    options: ExtraValidationConfig,
    buffer: Record<string, ValidationResult[]>,
): Promise<void> => {
    const list = getCtxByModel(validationKey, model)

    for (const [fieldName, ctx] of list) {
        await validateSimple(fieldName, { ...ctx, ...model }, validations, options, buffer)
    }
}

export const validateFields = async (
    fields: Record<string, Validation[]>,
    model: Record<string, unknown>,
    options: ExtraValidationConfig,
) => {
    const messages: Record<string, ValidationResult[]> = {}

    for (const [fieldName, validations] of Object.entries(fields)) {
        const ctx = getCtxFromField(fieldName) || {}

        await validateSimple(fieldName, { ...ctx, ...model }, validations, options, messages)
    }

    return messages
}

export const validateModel = async (
    model: Record<string, unknown>,
    validations: Record<string, Validation[]>,
    options: ExtraValidationConfig,
): Promise<Record<string, ValidationResult[]>> => {
    const messages: Record<string, ValidationResult[]> = {}

    for (const [validationKey, validationList] of Object.entries(validations)) {
        if (isMulti(validationKey)) {
            await validateMulti(validationKey, model, validationList, options, messages)
        } else {
            await validateSimple(validationKey, model, validationList, options, messages)
        }
    }

    return messages
}

export const hasError = (
    messages: Record<string, ValidationResult[]>,
    severity: Severity = Severity.danger,
): boolean => Object.values(messages).some(message => checkErrors(message, severity))
