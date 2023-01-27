import { get } from 'lodash'

import { INDEX_REGEXP } from './const'
import type { IValidation, IValidationResult } from './IValidation'
import { filterByFields, isMulti, keyToRegexp } from './utils'
import { validateField, hasError as checkErrors } from './validateField'

const validateSimple = async (
    allMessages: Record<string, IValidationResult[]>,
    model: object,
    field: string,
    validationList: IValidation[],
): Promise<void> => {
    const messages = await validateField(
        field as keyof (typeof model),
        model,
        validationList || [],
    )

    if (messages.length) {
        allMessages[field] = messages
    }
}

const validateMulti = async (
    allMessages: Record<string, IValidationResult[]>,
    model: object,
    validationKey: string,
    validationList: IValidation[],
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
        )
    }
}

const validateMultiByFields = async (
    allMessages: Record<string, IValidationResult[]>,
    model: object,
    validationKey: string,
    validationList: IValidation[],
    fields: string[],
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
            )
        }
    }
}

export const validateModel = async (
    model: object,
    validations: Record<string, IValidation[]>,
    fields?: string[],
): Promise<Record<string, IValidationResult[]>> => {
    let entries = Object.entries(validations)
    const allMessages: Record<string, IValidationResult[]> = {}

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
                )
            } else {
                // Валидация конерктных строк мультисета
                await validateMulti(
                    allMessages,
                    model,
                    validationKey,
                    validationList,
                )
            }
        } else {
            // Валидация простого поля
            await validateSimple(allMessages, model, validationKey, validationList)
        }
    }

    return allMessages
}

export const hasError = (
    messages: Record<string, IValidationResult[]>,
): boolean => Object.values(messages).some(checkErrors)
