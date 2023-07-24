const condition = (i: number, count: string, inclusive: boolean) => {
    if (inclusive) {
        return i <= parseInt(count, 10)
    }

    return i < parseInt(count, 10)
}

export const mapToNum = (
    count: number | string,
    callback: (value: number) => void,
    { increment = 1, start = 0, inclusive = false } = {},
) => {
    if (!count) { return null }
    const buf = []

    for (let i = start; condition(i, String(count), inclusive); i += increment) {
        buf.push(callback(i))
    }

    return buf
}
