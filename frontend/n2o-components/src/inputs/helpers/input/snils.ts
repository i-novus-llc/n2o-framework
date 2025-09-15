import { type MaskitoOptions } from '@maskito/core'

export const SNILS_MASK_ARRAY: Array<string | RegExp> = [
    /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, ' ', /\d/, /\d/,
]

export const SNILS_MASK: MaskitoOptions = { mask: SNILS_MASK_ARRAY }
export const DEFAULT_PLACEHOLDER = '___-___-___ __'
export const DEFAULT_INVALID_TEXT = 'Неверный СНИЛС'

/** Проверка валидности СНИЛС */
export function isValidSNILS(value: string): boolean {
    const cleanValue = value.replace(/\D/g, '')

    if (cleanValue.length !== 11 || /^0+$/.test(cleanValue)) {
        return false
    }

    // Расчет контрольной суммы
    let sum = 0

    for (let i = 0; i < 9; i += 1) {
        sum += parseInt(cleanValue[i], 10) * (9 - i)
    }

    let checkDigit = sum

    if (sum > 101) {
        checkDigit = sum % 101
    }
    if (checkDigit === 100 || checkDigit === 101) {
        checkDigit = 0
    }

    const actualCheckDigit = parseInt(cleanValue.slice(-2), 10)

    return checkDigit === actualCheckDigit
}

/** Форматирование значения в маскированный вид */
export function formatSNILS(value?: string | null): string {
    if (!value) { return '' }
    const cleanValue = value.replace(/\D/g, '')

    if (cleanValue.length !== 11) { return value }

    return cleanValue.replace(
        /(\d{3})(\d{3})(\d{3})(\d{2})/,
        '$1-$2-$3 $4',
    )
}
