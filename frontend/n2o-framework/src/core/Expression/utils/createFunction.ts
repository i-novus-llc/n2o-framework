import { createGlobalContext } from './createGlobalContext'

export type ExpressionFunction = <TReturn = unknown>(...context: unknown[]) => TReturn

const expressionCache = new Map<string, ExpressionFunction>()

/**
 * Создает функцию из текста
 * @param args {String[]} - массив имен переменных
 * @param code {String} - код для выполнения
 * @returns {Function} - Функция, созданная из текста code
 */
export function createFunction(args: string[], code: string): ExpressionFunction {
    const joinedArgs = args.join(',')
    const key = `${joinedArgs}|||${code}`

    const expressionFunction = expressionCache.get(key)

    if (expressionFunction) { return expressionFunction }
    // eslint-disable-next-line no-new-func,@typescript-eslint/no-implied-eval
    const creator = new Function(
        'globalContext',
        `with(globalContext) {
            return function (${joinedArgs}) { return (${code}) }
        }`,
    ) as (global: object) => ExpressionFunction
    const func: ExpressionFunction = creator(createGlobalContext())

    expressionCache.set(key, func)

    return func
}
