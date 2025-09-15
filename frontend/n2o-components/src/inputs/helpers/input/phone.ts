export const DEFAULT_COUNTRY = 'RU'
export const DEFAULT_COUNTRIES = [DEFAULT_COUNTRY]
export const DEFAULT_INVALID_TEXT = 'Неверный номер телефона'

/** Конфигурации масок телефонных номеров для маскито */
export const COUNTRY_PHONE_CONFIGS = {
    RU: {
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
    },
    KZ: {
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
    },
    BY: {
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
    },
    US: {
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
    },
    DEFAULT: {
        mask: Array(15).fill(/\d/),
        placeholder: '__________',
        validate: () => true,
        pattern: '^\\d{1,15}$', // Разрешает от 1 до 15 цифр
    },
}
