function normalize(path: string) {
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

    return `/${result.join('/')}${path.endsWith('/') ? '/' : ''}`
}

export function resolvePath(base: string, path: string) {
    if (path.startsWith('http://') || path.startsWith('https://')) { return path }
    if (path.startsWith('/')) { return normalize(path) }

    return normalize(`${base}${base.endsWith('/') ? '' : '/'}${path}`)
}
