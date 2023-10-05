export type TBaseProps = {
    className?: string,
    disabled?: boolean
    style?: object,
    visible?: boolean
}

export type TBaseInputProps<TValue> = {
    autoFocus?: boolean,
    autocomplete?: boolean,
    id?: string
    name?: string
    onBlur?(event: Event): void
    onChange?(newValue: TValue, event?: Event): void
    onFocus?(event: Event): void
    placeholder?: string
    readonly?: boolean
    value?: TValue
}

export type TOption<TValue> = {
    className?: string
    disabled?: boolean
    id: string | number
    label: string | JSX.Element
    tooltip?: string
    visible?: boolean
    value: TValue
}
