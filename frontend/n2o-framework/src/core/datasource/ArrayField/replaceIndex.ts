import { ContextType } from './Context'

export const replaceIndexKey = <T extends object | string>(data: T, ctx: ContextType): T => {
    let result = typeof data === 'string' ? data : JSON.stringify(data)

    for (const [key, index] of Object.entries(ctx)) {
        result = result.replaceAll(`[${key}]`, `[${index}]`)
    }

    return typeof data === 'string' ? result as T : JSON.parse(result)
}
