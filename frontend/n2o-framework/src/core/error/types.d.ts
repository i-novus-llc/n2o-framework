import {
    ComponentClass,
    ErrorInfo,
    FunctionComponentFactory,
    ReactNode,
} from 'react'

export type ErrorComponentProps = {
    onReset<TArg = unknown>(arg: TArg): void
    error: Error
}

export type ErrorComponent = (
    ComponentClass<ErrorComponentProps> |
    FunctionComponentFactory<ErrorComponentProps>
)

export type ErrorHandler = (error: Error) => (
    ErrorComponent | // Component constructor
    ReactNode | // Component
    void // next handler
)

export type ErrorContainerContextType = ErrorHandler[]

export type ErrorContainerProps = {
    error?: Error & { status?: number } | null
    onReset<TArg = unknown>(arg: TArg): void
    children?: ReactNode | undefined
}

export type ErrorContainerProviderProps = {
    value: ErrorContainerContextType
    children?: ReactNode | undefined
    isOnline?: boolean
}

export type ErrorBoundaryContextType = {
    onError(error: Error | string): void
}

export type ErrorBoundaryProps = {
    onReset?(): void
    onError?(error: Error, errorInfo?: ErrorInfo): void
    children: ReactNode
}
