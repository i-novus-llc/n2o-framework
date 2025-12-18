import { type MaskitoDateMode, type MaskitoTimeMode } from '@maskito/kit'

export function preparePlaceholder(input: string): string {
    return input.replace(/[A-Za-zЁА-яё]/g, '_')
}

export const getFloatingPlaceholder = (dateMode?: MaskitoDateMode | null, timeMode?: MaskitoTimeMode | null) => {
    if (dateMode && timeMode) { return preparePlaceholder(`${dateMode}${timeMode}`) }
    if (dateMode) { return preparePlaceholder(dateMode) }
    if (timeMode) { return preparePlaceholder(timeMode) }

    return ''
}
