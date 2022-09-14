import { createContext } from 'react'

import { FactoryLevels } from './factoryLevels'

export const FactoryContext = createContext({
    resolveProps(props: object) { return props },
    // eslint-disable-next-line @typescript-eslint/ban-types
    getComponent<TComponent extends Function>(componentName: string, level: FactoryLevels): TComponent | void {},
})
