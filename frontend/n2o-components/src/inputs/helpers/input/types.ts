export type Validate = (value: string) => boolean

export interface useInputControllerOptions {
    clearOnBlur?: boolean
    invalidText?: string
    onBlur?(value: string | null): void
    onChange?(value: string | null): void
    onMessage?(error: Error): void
    value?: string | null
}

export interface useInputControllerProps extends useInputControllerOptions {
    className?: string
    storeCleanValue?: boolean
    validate: Validate
}

export interface InputProps extends useInputControllerOptions {
    autocomplete?: string
    className?: string
    disabled?: boolean
    id?: string
    onFocus?(): void
    placeholder?: string
}
