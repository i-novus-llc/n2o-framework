const keySymbol = Symbol('uniq')
let iterator = 0
const map = new WeakMap<object, string>()

type Unique = { [keySymbol]: string }
type Id = { id: string | number }

function hasUnique(item: unknown): item is Unique {
    return typeof item === 'object' && item !== null && (keySymbol in item)
}

function hasIdField(item: unknown): item is { id: unknown } {
    return (typeof item === 'object') && (item !== null) && ('id' in item)
}

function hasId(item: unknown): item is Id {
    return hasIdField(item) && (
        (typeof item.id === 'string' && item.id.length > 0) ||
        typeof item.id === 'number'
    )
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

    if (hasUnique(item)) { return item }
    if (hasId(item)) { return item }

    return Object.defineProperty(newItem, keySymbol, { value: generateKey() }) as T & Unique
}

export function getKey(item: unknown): string | null {
    if (!item || typeof item !== 'object') { return null }
    if (hasUnique(item)) { return item[keySymbol] }
    if (hasId(item)) { return `${item.id}` }
    if (map.has(item)) { return map.get(item) as string }

    const key = generateKey()

    map.set(item, key)

    return key
}
