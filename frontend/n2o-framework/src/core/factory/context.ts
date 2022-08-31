import React, { createContext } from 'react'

import { FactoryLevels } from './factoryLevels'

type levels = keyof typeof FactoryLevels

export const FactoryContext = createContext({
    resolveProps(props: object) { return props },
    getComponent(componentName: string, level: levels): React.ReactElement | void {},
})
