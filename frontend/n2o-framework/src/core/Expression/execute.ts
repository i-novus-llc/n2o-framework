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
    model: object = {},
    context = {},
    def?: UnexpectedResult,
): ExpectedResult | UnexpectedResult {
    try {
        const extendedContext = Array.isArray(model) ? context : { ...context, ...model }
        const fn = createFunction(expression)

        return fn.call(model, extendedContext) as ExpectedResult
    } catch (error) {
        warning(
            true,
            `Execute expression error: ${error instanceof Error ? error.message : error}. expression: ${expression}. model: ${JSON.stringify(model)}`,
        )

        return def as UnexpectedResult
    }
}
