/**
 * Проверяет, является ли строка JS выражением
 * @param value {String} - Проверяемая строка
 * @returns {String|Boolean} - Найденное JS выражение, или false
 */
export function parseExpression(value: unknown): string | false {
    if (typeof value !== 'string') { return false }
    if (value.startsWith('`') && value.endsWith('`')) {
        return value.substring(1, value.length - 1)
    }

    return false
}
