import React, { ComponentClass, FunctionComponentFactory } from 'react'

import { ErrorBoundary } from './Boundary'

export const withErrorBoundary = <
    TProps extends object,
>(Component: ComponentClass<TProps> | (FunctionComponentFactory<TProps> & { displayName?: string })) => {
    function WithErrorBoundary(props: TProps) {
        return (
            <ErrorBoundary>
                <Component {...props} />
            </ErrorBoundary>
        )
    }

    WithErrorBoundary.displayName = `WithErrorBoundary: ${Component.displayName || ''}`

    return WithErrorBoundary
}
