const keySymbol = Symbol('uniq')
let iterator = 0
const map = new WeakMap<object, string>()

function hasKey(item: unknown): item is { [keySymbol]: string } {
    return typeof item === 'object' && item !== null && (keySymbol in item)
}

function generateKey() {
    const next = iterator < Number.MAX_SAFE_INTEGER
        ? iterator + 1
        : Number.MIN_SAFE_INTEGER
    const key = `key-${iterator}`

    iterator = next

    return key
}

export function setKey<T>(item: T): T {
    if (!item || typeof item !== 'object') { return item }

    const newItem = Array.isArray(item)
        ? item.map(setKey)
        : Object.fromEntries(Object.entries(item).map(([k, v]) => [k, setKey(v)]))

    return Object.assign(newItem, {
        [keySymbol]: hasKey(item) ? item[keySymbol] : generateKey(),
    }, item)
}

export function getKey(item: unknown): string | null {
    if (!item || typeof item !== 'object') { return null }
    if (hasKey(item)) { return item[keySymbol] }
    if (map.has(item)) { return map.get(item) as string }

    const key = generateKey()

    map.set(item, key)

    return key
}
