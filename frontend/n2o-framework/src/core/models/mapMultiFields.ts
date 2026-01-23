export function mapMultiFields<
    T,
    O extends Record<string, T>,
    F extends (arg: { item: T, subName: string, index: number, fullName: string }) => void | { name: string, value: T },
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
