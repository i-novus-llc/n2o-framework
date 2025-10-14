export const DEFAULT_INVALID_TEXT = 'Неверный номер телефона'

export enum CountryIsoCodes {
    RU = 'RU',
    BY = 'BY',
    KZ = 'KZ',
    US = 'US',
}

export const DEFAULT_COUNTRIES = [CountryIsoCodes.RU]

const RU_KZ = {
    // 10 цифр без +7
    expectedLengths: 11,
    prefix: '+7 ',
    mask: [
        '+', '7', ' ', '(',
        /\d/, /\d/, /\d/,
        ')', ' ',
        /\d/, /\d/, /\d/,
        '-',
        /\d/, /\d/,
        '-',
        /\d/, /\d/,
    ],
    placeholder: '+7 (___) ___-__-__',
    validate: (phone: string) => {
        const digits = phone.replace(/\D/g, '')

        return digits.length === 11 && digits.startsWith('7')
    },
    pattern: '^\\+7\\s\\(\\d{3}\\)\\s\\d{3}-\\d{2}-\\d{2}$',
    codePattern: /^(\+?7?\s?8?)\s?/,
}

/** Конфигурации масок телефонных номеров для маскито */
export const COUNTRY_PHONE_CONFIGS = {
    RU: {
        isoCode: CountryIsoCodes.RU,
        ...RU_KZ,
    },
    KZ: {
        isoCode: CountryIsoCodes.KZ,
        ...RU_KZ,
    },
    BY: {
        isoCode: CountryIsoCodes.BY,
        // 9 цифр без +375 (например: 29 123-45-67)
        expectedLengths: 12,
        prefix: '+375 ',
        mask: [
            '+', '3', '7', '5', ' ', '(',
            /\d/, /\d/,
            ')', ' ',
            /\d/, /\d/, /\d/,
            '-',
            /\d/, /\d/,
            '-',
            /\d/, /\d/,
        ],
        placeholder: '+375 (__) ___-__-__',
        validate: (phone: string) => {
            const digits = phone.replace(/\D/g, '')

            return digits.length === 12 && digits.startsWith('375')
        },
        pattern: '^\\+375\\s\\(\\d{2}\\)\\s\\d{3}-\\d{2}-\\d{2}$',
        codePattern: /^(\+?375?\s?)/,
    },
    US: {
        isoCode: CountryIsoCodes.US,
        // 10 цифр без +1
        expectedLengths: 11,
        prefix: '+1 ',
        mask: [
            '+', '1', ' ', '(',
            /\d/, /\d/, /\d/,
            ')', ' ',
            /\d/, /\d/, /\d/,
            '-',
            /\d/, /\d/, /\d/, /\d/,
        ],
        placeholder: '+1 (___) ___-____',
        validate: (phone: string) => {
            const digits = phone.replace(/\D/g, '')

            return digits.length === 11 && digits.startsWith('1')
        },
        pattern: '^\\+1\\s\\(\\d{3}\\)\\s\\d{3}-\\d{4}$',
        codePattern: /^(\+?1?\s?)/,
    },
    DEFAULT: {
        mask: Array(15).fill(/\d/),
        placeholder: '__________',
        validate: () => true,
        pattern: '^\\d{1,15}$', // Разрешает от 1 до 15 цифр
        codePattern: /^(\+)?/,
    },
}
