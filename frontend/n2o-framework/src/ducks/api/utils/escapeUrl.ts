const ESCAPED_SYMBOL = '%26'

export function escapeUrl(url: string) {
    return url.replaceAll('&', ESCAPED_SYMBOL)
}
