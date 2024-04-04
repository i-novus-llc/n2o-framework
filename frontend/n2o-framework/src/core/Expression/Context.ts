import { createExtendedContext } from '../../utils/createExtendedContext'

function mergeFn <T extends Record<string, unknown>>(
    parent: T,
    current: T,
): T { return { ...parent, ...current } }

/**
 * React контекст выполнения вычисляемых выражений
 */
export const ExpressionContext = createExtendedContext<Record<string, unknown>>({}, mergeFn)
