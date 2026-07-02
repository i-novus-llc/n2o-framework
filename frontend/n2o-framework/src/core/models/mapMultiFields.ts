type MapFnArg<T> = {
    item: T,
    subName: string,
    index: number,
    fullName: string
}

type MapFn<T> = (arg: MapFnArg<T>) => void | { name: string, value: T }

export function mapMultiFields<
    T,
    O extends Record<string, T>,
    F extends MapFn<T>,
>(obj: O, field: string, mapFn: F) {
    const newObj: Record<string, T> = {}
    const escaped = field.replaceAll(/([$[^])/g, '\\$1')
    const mask = new RegExp(`${escaped}\\[(\\d+)]\\.(.+)`)

    for (const [fullName, item] of Object.entries(obj)) {
        const match = fullName.match(mask)

        if (match) {
            const [, i, subName] = match
            const index = Number(i)
            const next = mapFn({ item, subName, index, fullName })

            if (!next) { continue }

            newObj[next.name] = next.value
        } else {
            // not multi-set fields
            newObj[fullName] = item
        }
    }

    return newObj
}

export function getOnAppend<T>(field: string, position: number) {
    return function mapOnAppend<P = T>({ item: value, fullName: name, subName, index }: MapFnArg<P>) {
        // index before removed elements
        if (index < position) { return { name, value } }

        return { name: `${field}[${index + 1}].${subName}`, value }
    }
}

export function getOnRemove<T>(field: string, start: number, count: number) {
    return function mapOnRemove<P = T>({ item: value, fullName: name, subName, index }: MapFnArg<P>) {
        // index before removed elements
        if (index < start) { return { name, value } }
        // removed elements: ignore it
        if ((index >= start) && (index < start + count)) { return undefined }

        // after removed: shift index
        const newIndex = index - count

        return { name: `${field}[${newIndex}].${subName}`, value }
    }
}
