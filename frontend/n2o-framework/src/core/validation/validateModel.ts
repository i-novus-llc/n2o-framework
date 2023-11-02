import { get } from 'lodash'

import { INDEX_REGEXP } from './const'
import type { Validation, ValidationResult } from './types'
import { filterByFields, isMulti, keyToRegexp } from './utils'
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
    const fieldArrayName: string = validationKey.split(INDEX_REGEXP)?.[0]
    const arrayFieldValue: object[] = get(model, fieldArrayName, [])

    for (let index = 0; index < arrayFieldValue.length; index++) {
        const fieldName = validationKey.replaceAll(INDEX_REGEXP, `[${index}]`)

        await validateSimple(
            allMessages,
            { ...model, index },
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
        const match = field.match(findIndexRegexp)

        if (match) {
            const [, index] = match

            await validateSimple(
                allMessages,
                { ...model, index },
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
                // Валидация всех строк мультисета
                await validateMultiByFields(
                    allMessages,
                    model,
                    validationKey,
                    validationList,
                    fields,
                    signal,
                )
            } else {
                // Валидация конерктных строк мультисета
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
