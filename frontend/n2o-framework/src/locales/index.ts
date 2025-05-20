import ru from './ru/translation'
import en from './en/translation'

export const defaultLocalesPreset = {
    ru: {
        translation: ru,
    },
    en: {
        translation: en,
    },
}

export enum Locale {
    en = 'en',
    ru = 'ru',
}

export type LocaleArray = string[]

export type LocalesPreset = Record<string, { translation: Record<string, string> }>

export default defaultLocalesPreset
