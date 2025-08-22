export interface CommonMaskedInputProps {
    autocomplete?: string
    className?: string
    disabled?: boolean
    onChange?(value: string | null): void
    onBlur?(value: string | null): void
    value?: string | number | null
}
