type MapFn<T> = (arg: {
    item: T,
    subName: string,
    index: number,
    fullName: string
}) => void | { name: string, value: T }

export function mapMultiFields<
    T,
    O extends Record<string, T>,
    F extends MapFn<T>,
>(obj: O, field: string, mapFn: F) {
    const newObj: Record<string, T> = {}
    const mask = new RegExp(`${field}\\[(\\d+)]\\.(.+)`)

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

export function getOnAppend<T>(field: string, position: number): MapFn<T> {
    return ({ item: value, fullName: name, subName, index }) => {
        // index before removed elements
        if (index < position) { return { name, value } }

        return { name: `${field}[${index + 1}].${subName}`, value }
    }
}

export function getOnRemove<T>(field: string, start: number, count: number): MapFn<T> {
    return ({ item: value, fullName: name, subName, index }) => {
        // index before removed elements
        if (index < start) { return { name, value } }
        // removed elements: ignore it
        if ((index >= start) && (index < start + count)) { return undefined }

        // after removed: shift index
        const newIndex = index - count

        return { name: `${field}[${newIndex}].${subName}`, value }
    }
}
