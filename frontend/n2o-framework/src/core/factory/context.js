import { createContext } from 'react'

export const FactoryContext = createContext({
    resolveProps(props) { return props },
    getComponent() {},
})
