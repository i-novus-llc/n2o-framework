import { useCallback, useContext, useMemo, useRef } from 'react'

import { ExpressionContext } from './Context'
import { propsResolver, Resolve } from './propsResolver'

export function useResolved<
    Resolved extends Resolve<Prop>,
    Prop = unknown,
>(
    props: Prop,
    model: Record<string, unknown> | Array<Record<string, unknown>>,
    ignoreKeys: string[] = [],
): Resolved {
    const resolve = useResolver()

    return useMemo(
        () => resolve(props, model, ignoreKeys),
        [props, model, ignoreKeys, resolve],
    )
}

export function useResolver() {
    const expressionContext = useContext(ExpressionContext)
    const contextRef = useRef(expressionContext)

    contextRef.current = expressionContext

    return useCallback(
        (prop, model, ignoreKeys: string[] = []) => propsResolver(prop, model, contextRef.current, ignoreKeys),
        [contextRef],
    )
}
