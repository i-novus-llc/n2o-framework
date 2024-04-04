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
    const currentValue = useContext(ArrayFieldContext)
    const depth = useContext(DepthContext)

    const nextValue = useMemo(() => {
        const contextValue: ContextType = {
            ...currentValue,
            [`$index_${depth}`]: index,
        }

        if (depth === 0) { contextValue.index = index }

        return contextValue
    }, [depth, index, currentValue])

    return (
        <ExpressionContext.Provider value={nextValue}>
            <ArrayFieldContext.Provider value={nextValue}>
                {children}
            </ArrayFieldContext.Provider>
        </ExpressionContext.Provider>
    )
}
