import { INDEX_REGEXP } from './const'

export const keyToRegexp = (validationKey: string) => new RegExp(validationKey.replace(
    INDEX_REGEXP,
    '\\[(\\d+)]',
))

export const isMulti = (validationKey: string) => validationKey.match(INDEX_REGEXP)

export const filterByFields = (validationKey: string, fields: string[]): boolean => {
    if (isMulti(validationKey)) {
        const regexp = keyToRegexp(validationKey)

        return fields.some(field => field.match(regexp))
    }

    return fields.includes(validationKey)
}
