import { type MaskitoPreprocessor } from '@maskito/core'
import { maskitoAddOnFocusPlugin, maskitoCaretGuard, maskitoPrefixPostprocessorGenerator, maskitoRemoveOnBlurPlugin } from '@maskito/kit'

import { COUNTRY_PHONE_CONFIGS, CountryIsoCodes } from './phone'

function createPhonePlugins(prefix: string) {
    return [
        maskitoAddOnFocusPlugin(prefix),
        maskitoRemoveOnBlurPlugin(prefix),
        // Forbids to put caret before non-removable country prefix
        // But allows to select all value!
        maskitoCaretGuard((value, [from, to]) => [
            from === to ? prefix.length : 0,
            value.length,
        ]),
    ]
}

// @INFO для плагина copy-paste из буфера
// Прим. вставка "89123456789" => "+7 (912) 345-67-89"
export function createCompletePhoneInsertionPreprocessor(countryCode: CountryIsoCodes): MaskitoPreprocessor {
    const createTrimPrefix = (countryCode: CountryIsoCodes) => {
        return (value: string): string => value.replace(COUNTRY_PHONE_CONFIGS[countryCode].codePattern || /^(\+)?/, '')
    }

    const countDigits = (value: string): number => value.replaceAll(/\D/g, '').length
    const trimPrefix = createTrimPrefix(countryCode)

    return ({ elementState, data }) => {
        const { value, selection } = elementState

        return {
            elementState: {
                selection,
                value: countDigits(value) > COUNTRY_PHONE_CONFIGS[countryCode].expectedLengths
                    ? trimPrefix(value)
                    : value,
            },
            data: countDigits(data) >= COUNTRY_PHONE_CONFIGS[countryCode].expectedLengths
                ? trimPrefix(data)
                : data,
        }
    }
}

// Готовые препроцессоры для работы useMask
export const PROCESSORS = {
    phone: {
        RU: {
            postprocessors: [maskitoPrefixPostprocessorGenerator(COUNTRY_PHONE_CONFIGS[CountryIsoCodes.RU].prefix)],
            preprocessors: [createCompletePhoneInsertionPreprocessor(CountryIsoCodes.RU)],
            plugins: createPhonePlugins(COUNTRY_PHONE_CONFIGS[CountryIsoCodes.RU].prefix),
        },
        KZ: {
            postprocessors: [maskitoPrefixPostprocessorGenerator(COUNTRY_PHONE_CONFIGS[CountryIsoCodes.KZ].prefix)],
            preprocessors: [createCompletePhoneInsertionPreprocessor(CountryIsoCodes.KZ)],
            plugins: createPhonePlugins(COUNTRY_PHONE_CONFIGS[CountryIsoCodes.KZ].prefix),
        },
        BY: {
            postprocessors: [maskitoPrefixPostprocessorGenerator(COUNTRY_PHONE_CONFIGS[CountryIsoCodes.BY].prefix)],
            preprocessors: [createCompletePhoneInsertionPreprocessor(CountryIsoCodes.BY)],
            plugins: createPhonePlugins(COUNTRY_PHONE_CONFIGS[CountryIsoCodes.BY].prefix),
        },
        US: {
            postprocessors: [maskitoPrefixPostprocessorGenerator(COUNTRY_PHONE_CONFIGS[CountryIsoCodes.US].prefix)],
            preprocessors: [createCompletePhoneInsertionPreprocessor(CountryIsoCodes.US)],
            plugins: createPhonePlugins(COUNTRY_PHONE_CONFIGS[CountryIsoCodes.US].prefix),
        },
    },
}
