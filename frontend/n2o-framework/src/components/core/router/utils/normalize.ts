export function normalize(path: string) {
    const parts = path
        .replace(/^\//, '')
        .replace(/\/+$/, '')
        .split('/')
    const result: string[] = []

    for (const part of parts) {
        if (part === '.' || part === '') { continue }
        if (part === '..') {
            result.pop()

            continue
        }

        result.push(part)
    }

    if (!result.length) { return '/' }

    return `/${result.join('/')}${path.endsWith('/') ? '/' : ''}`
}
