const ESCAPED_SPACE = '%26'
const ESCAPED_LEFT_BRACKET = '%5B'
const ESCAPED_RIGHT_BRACKET = '%5D'

export function escapeUrl(url: string) {
    return url
        .replaceAll('&', ESCAPED_SPACE)
        .replaceAll('[', ESCAPED_LEFT_BRACKET)
        .replaceAll(']', ESCAPED_RIGHT_BRACKET)
}
