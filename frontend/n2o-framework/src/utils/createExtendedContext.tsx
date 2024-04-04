import React, {
    createContext,
    Context,
    useContext,
    ReactNode,
    useMemo,
} from 'react'

/**
 * Создание "расширяесого" контекста, который объеденяет значение родительского с текущим
 */
export function createExtendedContext<T extends Record<PropertyKey, unknown> | unknown[]>(
    defaultValue: T,
    mergeFn: (parent: T, current: T) => T,
): Context<T> {
    const ReactContext = createContext(defaultValue)

    function Provider({ value, children }: { value: T, children?: ReactNode | undefined; }) {
        const parentValue = useContext(ReactContext)
        const mergedValue = useMemo(() => mergeFn(parentValue, value), [parentValue, value])

        return (
            <ReactContext.Provider value={mergedValue}>
                {children}
            </ReactContext.Provider>
        )
    }

    Object.assign(Provider, ReactContext.Provider)

    return new Proxy(ReactContext, {
        get(target: typeof ReactContext, key: keyof typeof ReactContext) {
            if (key === 'Provider') {
                return Provider
            }

            return target[key]
        },
    })
}
