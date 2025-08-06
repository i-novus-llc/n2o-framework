const condition = (i: number, count: string, inclusive: boolean) => {
    if (inclusive) {
        return i <= parseInt(count, 10)
    }

    return i < parseInt(count, 10)
}

export const mapToNum = (
    count: number | string,
    callback: (value: number) => void,
    { increment = 1, start = 0, inclusive = false } = {},
) => {
    if (!count) { return null }
    const buf = []

    for (let i = start; condition(i, String(count), inclusive); i += increment) {
        buf.push(callback(i))
    }

    return buf
}

const escapeForRegExp = (string: string): string => string.replace(/[$()*+.?[\\\]^{|}]/g, '\\$&')

/**
 * Удаляет из конца строки все комбинации указанных подстрок.
 *
 * @param input Исходная строка для обработки.
 * @param exclusions Массив подстрок, которые нужно удалить с конца строки.
 * @returns Строка без комбинаций исключений в конце (пробелы обрезаны).
 *
 * @example
 * removeTrailingExclusions("hello!!", ["!", "!!"]); // "hello"
 * removeTrailingExclusions("file.tar.gz", [".gz", ".tar"]); // "file"
 */
export const removeTrailingExclusions = (input: string | null, exclusions: string[] = []) => {
    if (!input) { return input }

    const nonEmptyExclusions = exclusions.filter(exclusion => exclusion !== '')

    if (nonEmptyExclusions.length === 0) { return input.trim() }

    const processedExclusions = nonEmptyExclusions
        .map(escapeForRegExp)
        .filter((value, index, self) => self.indexOf(value) === index)
        .sort((a, b) => b.length - a.length)

    const regex = new RegExp(`(?:${processedExclusions.join('|')})*$`)

    return input.replace(regex, '').trim()
}

/**
 * Форматирует число, добавляя нули в дробную часть при необходимости.
 * Бэк н2о работает со string || number, из за чего могут отсекаться нули
 * - Для целых чисел возвращает строку без изменений
 * - Если decimalLimit не указан, возвращает исходное строковое представление
 * - Если дробная часть короче decimalLimit, дополняет нулями
 * - При decimalLimit=0 возвращает целую часть
 */
export function formatNumber(num: number, decimalLimit?: number): string {
    const numStr = num.toString()

    if (!numStr.includes('.') || !decimalLimit) {
        return numStr
    }

    const [integerPart, decimalPart = ''] = numStr.split('.')

    if (decimalLimit === 0) {
        return integerPart
    }

    if (decimalPart.length >= decimalLimit) {
        return numStr
    }

    // Дополняем нулями при необходимости
    const zerosToAdd = '0'.repeat(decimalLimit - decimalPart.length)

    return `${integerPart}.${decimalPart}${zerosToAdd}`
}

export function replaceChar(value: string | null, from: string, to: string) {
    if (!value) { return value }

    const escapedFrom = from.replace(/[$()*+.?[\\\]^{|}]/g, '\\$&')
    const regex = new RegExp(escapedFrom, 'g')

    return value.replace(regex, to)
}

export function removeAllSpaces(value: string | null) {
    if (!value) { return value }

    return value.replace(/\s/g, '')
}
