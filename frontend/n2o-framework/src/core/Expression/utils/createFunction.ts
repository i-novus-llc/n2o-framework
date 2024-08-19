import { createGlobalContext } from './createGlobalContext'

export type ExpressionFunction = <TReturn = unknown>(model: object) => TReturn

const expressionCache = new Map<string, ExpressionFunction>()

/**
 * Создает функцию из текста
 * @param code {String} - код для выполнения
 * @returns {Function} - Функция, созданная из текста code
 */
export function createFunction(code: string): ExpressionFunction {
    const expressionFunction = expressionCache.get(code)

    if (expressionFunction) { return expressionFunction }
    // eslint-disable-next-line no-new-func,@typescript-eslint/no-implied-eval
    const creator = new Function(
        'globalContext',
        `with(globalContext) {
            return function (model) {
                with(model) { return (${code}) }
            }
        }`,
    ) as (global: object) => ExpressionFunction
    const func: ExpressionFunction = creator(createGlobalContext())

    expressionCache.set(code, func)

    return func
}
