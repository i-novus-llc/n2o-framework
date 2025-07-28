import { MutableRefObject, RefObject } from 'react'

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

const escapeForRegExp = (string: string): string => {
    return string.replace(/[$()*+.?[\\\]^{|}]/g, '\\$&')
}

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
export const removeTrailingExclusions = (input: string, exclusions: string[] = []): string => {
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
