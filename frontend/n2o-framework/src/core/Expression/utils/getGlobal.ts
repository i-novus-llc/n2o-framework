/**
 * Получение глобального контекста
 */
export function getGlobal(): typeof globalThis {
    // eslint-disable-next-line no-undef
    if (typeof globalThis !== 'undefined') { return globalThis }
    // eslint-disable-next-line no-restricted-globals
    if (typeof self !== 'undefined') { return self }
    if (typeof window !== 'undefined') { return window }
    // @ts-ignore FIXME: скорее всего уже можно обойтись одним globalThis
    if (typeof global !== 'undefined') { return global }

    // @ts-ignore FIXME: скорее всего уже можно обойтись одним globalThis
    return (function getThis() { return this }())
}
