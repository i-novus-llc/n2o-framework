import type { IValidation, IValidationResult } from './IValidation'
import { validateField, hasError as checkErrors } from './validateField'

export const validateModel = async (
    model: object,
    validations: Record<string, IValidation[]>,
): Promise<Record<string, IValidationResult[]>> => {
    const entries = Object.entries(validations)
    const allMessages: Record<string, IValidationResult[]> = {}

    for (const [field, validationList] of entries) {
        const messages = await validateField(
            field as keyof (typeof model),
            model,
            validationList || [],
        )

        if (messages.length) {
            allMessages[field] = messages
        }
    }

    return allMessages
}

export const hasError = (
    messages: Record<string, IValidationResult[]>,
): boolean => Object.values(messages).some(checkErrors)
