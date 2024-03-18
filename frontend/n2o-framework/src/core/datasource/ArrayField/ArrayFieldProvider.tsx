import React, { ReactNode, useContext } from 'react'

import { DepthContext } from './Context'

type ArrayFieldProviderProps = {
    children: ReactNode
}

/**
 * Компонент-обёртка над мультисетатами для контроля глубины вложенности
 */
export function ArrayFieldProvider({ children }: ArrayFieldProviderProps) {
    const currentDepth = useContext(DepthContext)

    return (
        <DepthContext.Provider value={currentDepth + 1}>
            {children}
        </DepthContext.Provider>
    )
}
