import { ContextType } from './Context'

export function replaceIndexKey(data: string, ctx: ContextType) {
    let result = data

    for (const [key, value] of Object.entries(ctx)) {
        result = result.replaceAll(`[${key}]`, `[${value}]`)
    }

    return result
}

const replaceWord = (str: string, from: string, to: string) => {
    // \b и \$ кофликтуют, когда стоят рядом
    const prefix = from.startsWith('$') ? '' : '\\b'
    const suffix = from.endsWith('$') ? '' : '\\b'
    // Экранируем, чтобы регулярка не ломалась на спецсимволе
    const word = from.replace('$', '\\$')
    const reg = new RegExp(`${prefix}${word}${suffix}`, 'g')

    return str.replaceAll(reg, to)
}

export const replaceIndex = (
    obj: object,
    ctx: ContextType,
) => {
    let jsonString = JSON.stringify(obj)

    for (const [key, index] of Object.entries(ctx)) {
        jsonString = replaceWord(jsonString, key, `${index}`)
    }

    return JSON.parse(jsonString)
}
