import type { IValidation, IValidationResult } from './IValidation'
import { validateField, hasError as checkErrors } from './validateField'

const findIndexRegexp = /\[index(.)*]/ig

export const validateAndSetMessages = async (
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

export const validateModel = async (
    model: object,
    validations: Record<string, IValidation[]>,
): Promise<Record<string, IValidationResult[]>> => {
    const entries = Object.entries(validations)
    const allMessages: Record<string, IValidationResult[]> = {}

    for (const [field, validationList] of entries) {
        if (!field.match(findIndexRegexp)) {
            await validateAndSetMessages(allMessages, model, field, validationList)
        } else {
            const fieldArrayName: string = field.split(findIndexRegexp)?.[0]
            const arrayFieldValue = (model as Record<string, unknown>)[fieldArrayName]

            for (let i = 0; i < (arrayFieldValue as []).length; i++) {
                const fieldName = field.replaceAll('index', i.toString())

                await validateAndSetMessages(
                    allMessages,
                    model,
                    fieldName,
                    JSON.parse(
                        JSON.stringify(validationList).replaceAll('index', i.toString()),
                    ),
                )
            }
        }
    }

    return allMessages
}

export const hasError = (
    messages: Record<string, IValidationResult[]>,
): boolean => Object.values(messages).some(checkErrors)
