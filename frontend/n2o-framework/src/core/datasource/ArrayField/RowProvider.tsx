import React, { ReactNode, useContext, useMemo } from 'react'

import { ExpressionContext } from '../../Expression/Context'

import { ArrayFieldContext, DepthContext, ContextType } from './Context'

type RowProviderProps = {
    index: number
    children: ReactNode
}

/**
 * Компоеннт-обёртка над строками мультисета для добавления индекса строки в контекст выполнения экспрешенов
 */
export function RowProvider({ index, children }: RowProviderProps) {
    const evalContext = useContext(ExpressionContext)
    const multiContext = useContext(ArrayFieldContext)
    const depth = useContext(DepthContext)

    const nextCtx = useMemo(() => {
        const multi: ContextType = {
            ...multiContext,
            [`$index_${depth}`]: index,
        }

        if (depth === 0) { multi.index = index }

        const expression = { ...evalContext, ...multi }

        return { multi, expression }
    }, [depth, evalContext, index, multiContext])

    return (
        <ExpressionContext.Provider value={nextCtx.expression}>
            <ArrayFieldContext.Provider value={nextCtx.multi}>
                {children}
            </ArrayFieldContext.Provider>
        </ExpressionContext.Provider>
    )
}
