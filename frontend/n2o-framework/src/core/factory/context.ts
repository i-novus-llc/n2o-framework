import { createContext, ComponentType } from 'react'

import { FactoryLevels } from './factoryLevels'

type Prop = Record<string, unknown>
type Props = Prop | Prop[]

export const FactoryContext = createContext({
    resolveProps<U, T extends Props = Props>(props: T, options?: unknown): U { return props as unknown as U },
    getComponent<P>(componentName: string, level?: FactoryLevels): ComponentType<P> | void {},
})
