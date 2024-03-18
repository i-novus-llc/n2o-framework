import type { Validation, ValidationResult } from './types'
import { filterByFields, getCtxByModel, getCtxFromField, isMulti, keyToRegexp } from './utils'
import { validateField, hasError as checkErrors } from './validateField'

const validateSimple = async (
    allMessages: Record<string, ValidationResult[]>,
    model: object,
    field: string,
    validationList: Validation[],
    signal?: AbortSignal,
): Promise<void> => {
    const messages = await validateField(
        field as keyof (typeof model),
        model,
        validationList || [],
        signal,
    )

    if (messages.length) {
        allMessages[field] = messages
    }
}

const validateMulti = async (
    allMessages: Record<string, ValidationResult[]>,
    model: object,
    validationKey: string,
    validationList: Validation[],
    signal?: AbortSignal,
): Promise<void> => {
    const list = getCtxByModel(validationKey, model)

    for (const [fieldName, ctx] of list) {
        await validateSimple(
            allMessages,
            { ...ctx, ...model },
            fieldName,
            validationList,
            signal,
        )
    }
}

const validateMultiByFields = async (
    allMessages: Record<string, ValidationResult[]>,
    model: object,
    validationKey: string,
    validationList: Validation[],
    fields: string[],
    signal?: AbortSignal,
): Promise<void> => {
    const findIndexRegexp = keyToRegexp(validationKey)

    for (const field of fields) {
        const ctx = getCtxFromField(field, findIndexRegexp)

        if (ctx) {
            await validateSimple(
                allMessages,
                { ...ctx, ...model },
                field,
                validationList,
                signal,
            )
        }
    }
}

export const validateModel = async (
    model: object,
    validations: Record<string, Validation[]>,
    fields?: string[],
    signal?: AbortSignal,
): Promise<Record<string, ValidationResult[]>> => {
    let entries = Object.entries(validations)
    const allMessages: Record<string, ValidationResult[]> = {}

    if (fields?.length) {
        entries = entries.filter(([key]) => filterByFields(key, fields))
    }

    for (const [validationKey, validationList] of entries) {
        if (isMulti(validationKey)) {
            if (fields?.length) {
                // Валидация конерктных строк мультисета
                await validateMultiByFields(
                    allMessages,
                    model,
                    validationKey,
                    validationList,
                    fields,
                    signal,
                )
            } else {
                // Валидация всех строк мультисета
                await validateMulti(
                    allMessages,
                    model,
                    validationKey,
                    validationList,
                    signal,
                )
            }
        } else {
            // Валидация простого поля
            await validateSimple(allMessages, model, validationKey, validationList, signal)
        }
    }

    return allMessages
}

export const hasError = (
    messages: Record<string, ValidationResult[]>,
): boolean => Object.values(messages).some(checkErrors)
