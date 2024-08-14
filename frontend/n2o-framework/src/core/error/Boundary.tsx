import React, { Component, createContext, ErrorInfo } from 'react'

import { ErrorContainer } from './Container'
import { ErrorBoundaryContextType, ErrorBoundaryProps } from './types'

const toError = (error: unknown): Error => {
    if (error instanceof Error) { return error }
    if (error) { return new Error(String(error)) }

    return new Error('Unknown error')
}

export const ErrorBoundaryContext = createContext<ErrorBoundaryContextType>({
    onError(error: Error | string): void {
        // eslint-disable-next-line no-console
        console.error(error)
    },
})

export class ErrorBoundary extends Component<ErrorBoundaryProps> {
    state = {
        error: null,
    }

    static displayName = 'ErrorBoundary'

    static getDerivedStateFromError(error: Error) {
        return { error }
    }

    componentDidCatch(error: Error, errorInfo: ErrorInfo) {
        const { onError } = this.props

        onError?.(error, errorInfo)
    }

    reset = () => {
        const { onReset } = this.props

        onReset?.()
        this.setState({
            error: null,
        })
    }

    onAsyncError = (errorInfo: Error | string) => {
        const { onError } = this.props
        const error = toError(errorInfo)

        onError?.(error)
        this.setState({ error })
    }

    render() {
        const { error } = this.state
        const { children } = this.props

        return (
            <ErrorBoundaryContext.Provider value={{ onError: this.onAsyncError }}>
                {/* @ts-ignore FIXME разобраться в типизации */}
                <ErrorContainer error={error} onReset={this.reset}>
                    {children}
                </ErrorContainer>
            </ErrorBoundaryContext.Provider>
        )
    }
}
