import { ContextType } from './Context'

export function replaceIndex(data: string, ctx: ContextType) {
    let result = data

    for (const [key, value] of Object.entries(ctx)) {
        result = result.replaceAll(`[${key}]`, `[${value}]`)
    }

    return result
}
