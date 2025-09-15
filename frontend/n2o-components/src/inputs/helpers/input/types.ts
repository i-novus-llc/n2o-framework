export type Validate = (value: string) => boolean

export interface useInputControllerOptions {
    onChange?(value: string | null): void
    onBlur?(value: string | null): void
    onMessage?(error: Error): void
    invalidText?: string
    value?: string | null
    clearOnBlur?: boolean
}

export interface useInputControllerProps extends useInputControllerOptions {
    validate: Validate
    storeCleanValue?: boolean
    className?: string
}

export interface InputProps extends useInputControllerOptions {
    id?: string
    className?: string
    onFocus?(): void
    disabled?: boolean
    placeholder?: string
    autocomplete?: string
}
