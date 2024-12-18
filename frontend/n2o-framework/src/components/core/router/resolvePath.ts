export function resolvePath(base: string, url: string) {
    if (url.startsWith('/')) { return url }
    if (url.startsWith('./')) {
        return `${base}${base.endsWith('/') ? '' : '/'}${url.replace('./', '')}`
    }
    // todo "../"

    return `${base}${base.endsWith('/') ? '' : '/'}${url}`
}
