const condition = (i, count, inclusive) => {
    if (inclusive) {
        return i <= parseInt(count, 10)
    }

    return i < parseInt(count, 10)
}

export const mapToNum = (
    count,
    callback,
    { increment = 1, start = 0, inclusive = false } = {},
) => {
    if (!count) { return null }
    const buf = []

    for (let i = start; condition(i, count, inclusive); i += increment) {
        buf.push(callback(i))
    }

    return buf
}
