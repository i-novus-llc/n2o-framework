import React, { forwardRef, ComponentType, ForwardRefExoticComponent, PropsWithoutRef, RefAttributes } from 'react'

import { ErrorBoundary } from './Boundary'

export const withErrorBoundary = <TProps extends object>(
    Component: ComponentType<TProps>,
): ComponentType<TProps> | ForwardRefExoticComponent<PropsWithoutRef<TProps> & RefAttributes<HTMLElement>> => {
    // Проверяем, является ли компонент forwardRef
    const isForwardRef = (Component as { $$typeof?: symbol }).$$typeof === Symbol.for('react.forward_ref')

    if (isForwardRef) {
        const Wrapped = forwardRef<HTMLElement, TProps>((props, ref) => (
            <ErrorBoundary>
                <Component {...props} ref={ref} />
            </ErrorBoundary>
        ))

        Wrapped.displayName = `WithErrorBoundary(${Component.displayName || Component.name || 'Component'})`

        return Wrapped
    }

    // Обычный компонент без поддержки ref
    const Wrapped = (props: TProps) => (
        <ErrorBoundary>
            <Component {...props} />
        </ErrorBoundary>
    )

    Wrapped.displayName = `WithErrorBoundary(${Component.displayName || Component.name || 'Component'})`

    return Wrapped
}
