import { createContext } from 'react'

export type ContextType = {
    [key: `$index_${number}`]: number
    index?: number
}

export const DepthContext = createContext(-1)

export const ArrayFieldContext = createContext<ContextType>({})
