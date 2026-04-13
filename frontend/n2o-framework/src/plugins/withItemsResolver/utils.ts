import { libAsterisk } from '../Menu/helpers'

export const EXPRESSION_SYMBOL = ':'

interface ResolveExpressionResult {
    key: string | undefined;
    value: string | undefined;
}

export const resolveExpression = (location: { pathname: string }, path: string): ResolveExpressionResult => {
    if (!location || !path) { return { key: undefined, value: undefined } }
    let key

    const expressionPosition = path.split('/').findIndex((e) => {
        key = e.substring(1).replaceAll(libAsterisk, '')

        return e.startsWith(EXPRESSION_SYMBOL)
    })
    const value = location.pathname.split('/')[expressionPosition]

    return { key, value }
}
