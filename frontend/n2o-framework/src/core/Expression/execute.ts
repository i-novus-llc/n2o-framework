// @ts-ignore ignore import error from js file
import warning from '../../utils/warning'

import { createFunction } from './utils/createFunction'

export { parseExpression } from './parse'

export function executeExpression<
    ExpectedResult,
    UnexpectedResult = void,
>(
    expression: string,
    model: object,
    context = model,
    def?: UnexpectedResult,
): ExpectedResult | UnexpectedResult {
    if (expression === 'false') {
        // @ts-ignore Игнорим жалобы на типы тут, т.к. точно знаем результат
        return false as ExpectedResult
    }
    if (expression === 'true') {
        // @ts-ignore Игнорим жалобы на типы тут, т.к. точно знаем результат
        return true as ExpectedResult
    }

    return execute(expression, model, context, def)
}

function execute<
    ExpectedResult,
    UnexpectedResult = void,
>(
    expression: string,
    model: object,
    context = {},
    def?: UnexpectedResult,
): ExpectedResult | UnexpectedResult {
    try {
        const argsExtended = Array.isArray(model) ? context : { ...context, ...model }

        const entries = Object.entries(argsExtended)
        const keys = entries.map(arr => arr[0])
        const values = entries.map(arr => arr[1])
        const fn = createFunction(keys, expression)

        return fn.apply(model, values) as ExpectedResult
    } catch (error) {
        warning(
            true,
            `Execute expression error: ${error instanceof Error ? error.message : error}. expression: ${expression}. model: ${JSON.stringify(model)}`,
        )

        return def as UnexpectedResult
    }
}
