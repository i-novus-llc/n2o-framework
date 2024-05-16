import type { Validation, ValidationResult } from './types'
import { filterByFields, getCtxByModel, getCtxFromField, isMulti, keyToRegexp } from './utils'
import { validateField, ValidateField, hasError as checkErrors } from './validateField'

const validateSimple = async (options: ValidateField): Promise<void> => {
    const messages = await validateField(options)

    const { allMessages, validationKey } = options

    if (messages.length) {
        allMessages[validationKey] = messages
    }
}

const validateMulti = async (options: ValidateField): Promise<void> => {
    const { model, validationKey } = options
    const list = getCtxByModel(validationKey, model)

    for (const [fieldName, ctx] of list) {
        await validateSimple({ ...options, validationKey: fieldName, model: { ...ctx, ...model } })
    }
}

const validateMultiByFields = async (options: ValidateField): Promise<void> => {
    const { fields, validationKey, model } = options
    const findIndexRegexp = keyToRegexp(validationKey)

    if (!fields) {
        return
    }

    for (const field of fields) {
        const ctx = getCtxFromField(field, findIndexRegexp)

        if (ctx) {
            await validateSimple({ ...options, validationKey: field, model: { ...ctx, ...model } })
        }
    }
}

interface Options { fields?: string[], signal?: AbortSignal, datasourceId?: string, pageUrl?: string | null }

export const validateModel = async (
    model: object,
    validations: Record<string, Validation[]>,
    options: Options = {},
): Promise<Record<string, ValidationResult[]>> => {
    let entries = Object.entries(validations)
    const allMessages: Record<string, ValidationResult[]> = {}

    const { fields, signal, datasourceId, pageUrl } = options

    if (fields?.length) {
        entries = entries.filter(([key]) => filterByFields(key, fields))
    }

    const settings = {
        allMessages,
        model,
        fields,
        signal,
        datasourceId,
        pageUrl,
    }

    for (const [validationKey, validationList] of entries) {
        const options = { ...settings, validationKey, validationList }

        if (isMulti(validationKey)) {
            if (fields?.length) {
                // Валидация конкретных строк мультисета
                await validateMultiByFields(options)
            } else {
                // Валидация всех строк мультисета
                await validateMulti(options)
            }
        } else {
            // Валидация простого поля
            await validateSimple(options)
        }
    }

    return allMessages
}

export const hasError = (
    messages: Record<string, ValidationResult[]>,
): boolean => Object.values(messages).some(checkErrors)
