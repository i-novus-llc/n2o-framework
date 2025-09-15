export enum Versions {
    V1 = 'v1',
    V2 = 'v2',
    V3 = 'v3',
    V4 = 'v4',
    V5 = 'v5',
    ANY = 'any',
}

export const DEFAULT_PLACEHOLDER = '________-____-____-____-____________'
export const DEFAULT_INVALID_TEXT = 'Неверный формат UUID'

export function validateUUID(value: string, version: Versions): boolean {
    // Проверка базовой структуры UUID
    if (!value || value.length !== 36) { return false }
    if (value[8] !== '-' || value[13] !== '-' || value[18] !== '-' || value[23] !== '-') { return false }

    const hexPart = value.replace(/-/g, '')

    if (!/^[\da-f]{32}$/i.test(hexPart)) { return false }

    // Для версии 'any' достаточно базовой проверки
    if (version === Versions.ANY) { return true }

    // Для конкретных версий проверяем версию и вариант
    const versionByte = value.charAt(14)
    const variantByte = value.charAt(19).toLowerCase()

    return (
        versionByte === version.charAt(1) &&
        ['8', '9', 'a', 'b'].includes(variantByte)
    )
}

// Очистка от тире
export const prepareToStore = (value: string) => {
    return value.replace(/-/g, '').replace(/_/g, '')
}

// Универсальная маска для UUID (поддерживает все версии)
export const UUID_MASK_ARRAY: Array<string | RegExp> = Array(36)
    .fill(null)
    .map((_, index) => ([8, 13, 18, 23].includes(index) ? '-' : /[\da-f]/i))
