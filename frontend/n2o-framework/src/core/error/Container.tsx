import React, {
    createContext,
    useContext,
    useMemo,
    isValidElement,
} from 'react'

import { NOOP_FUNCTION } from '../../utils/emptyTypes'

import {
    ErrorContainerProps,
    ErrorContainerContextType,
    ErrorContainerProviderProps,
    ErrorComponent,
} from './types'

const ErrorHandlersContext = createContext<ErrorContainerContextType>([])

export function ErrorHandlersProvider({ value, isOnline, children }: ErrorContainerProviderProps) {
    const parentValue = useContext(ErrorHandlersContext)
    const mergedValue = useMemo(() => [...value, ...parentValue], [parentValue, value])

    return (
        <ErrorHandlersContext.Provider value={mergedValue}>
            {children}
        </ErrorHandlersContext.Provider>
    )
}

export function ErrorContainer({
    error,
    onReset = NOOP_FUNCTION,
    children,
}: ErrorContainerProps) {
    const handlers = useContext(ErrorHandlersContext)

    const errorComponent = useMemo(() => {
        if (!error) { return null }

        for (const handler of handlers) {
            const result = handler(error)

            if (result && isValidElement(result)) {
                return result
            }

            if (typeof result === 'function') {
                const Component = result as ErrorComponent

                return <Component error={error} onReset={onReset} />
            }
        }

        return <>{error.toString()}</>
    }, [error, handlers, onReset])

    // eslint-disable-next-line react/jsx-no-useless-fragment
    if (!errorComponent) { return <>{children}</> }

    return (
        <div className="n2o-ErrorContainer">
            {errorComponent}
        </div>
    )
}

ErrorContainer.displayName = 'ErrorContainer'
