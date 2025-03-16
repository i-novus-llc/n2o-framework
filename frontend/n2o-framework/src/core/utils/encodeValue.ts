export const PATTERNS = {
    // a value similar to #{string}
    CONTEXT_VARIABLE: /#{[^}]+}/,
}

export function encodeValue(value: string | number, pattern: RegExp) {
    if (typeof value !== 'string' || !pattern.test(value)) { return value }

    return encodeURIComponent(value)
}
