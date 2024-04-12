import { executeExpression } from '../core/Expression/execute'

// @ts-ignore ignore import error from js file
import functions from './functions'

export { parseExpression } from '../core/Expression/parse'

export const DEFAULT_CONTEXT = {
    ...functions,
    // @ts-ignore _n2oEvalContext задаётся где-то в App. FIXME: переделать на явную передачу контекста
    // eslint-disable-next-line no-underscore-dangle
    ...window._n2oEvalContext,
}

/**
 * Выполняет JS выражение
 * @param expression {String} - Выражение, которое нужно выполнить
 * @param model - {Object} - Аргумент вызова (будет обогощен либами, типа lodash, moment и пр.)
 * @param {object} [ctx]
 * @deprecated
 */
export function evalExpression<ExpectedResult>(
    expression: string,
    model: object,
    ctx: Record<string, unknown> = {},
): ExpectedResult | void {
    const context = { ...DEFAULT_CONTEXT, ...ctx }

    return executeExpression<ExpectedResult, void>(expression, model, context)
}

export default evalExpression
