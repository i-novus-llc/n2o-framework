import type { MaskitoDateMode, MaskitoTimeMode } from '@maskito/kit'
import { CSSProperties, FocusEvent, KeyboardEvent, MouseEvent, ReactNode } from 'react'
import type { Dayjs } from 'dayjs'

interface Limits { min?: string, max?: string }

export interface DateMaskProps {
    className?: string
    dateMode: MaskitoDateMode
    timeMode: MaskitoTimeMode | null
    dateSeparator?: string
    placeholder?: string
    disabled?: boolean
    value: string | null
    onInput?(): void
    onFocus?(e: FocusEvent<HTMLInputElement>): void
    onBlur?(e: FocusEvent<HTMLInputElement>): void
    onKeyDown?(e: KeyboardEvent<HTMLInputElement>): void
    onClick?(e: MouseEvent<HTMLInputElement>): void
    ref(el: HTMLInputElement | null): void
    autoFocus?: boolean
    inputClassName?: string
    max?: Date
    min?: Date
    prefixComponent?: ReactNode
    suffixComponent?: ReactNode
    style?: CSSProperties
    // Восстанавливать установленное value после blur, при незаполненой маске
    restoreOnBlur?: boolean
    autocomplete?: string
}

export interface WithFormatProps extends Limits {
    dateFormat: string
    timeFormat?: string
    value?: Dayjs | null
}

export interface WithEventsProps extends Limits {
    onInputChange(value: Dayjs | null, name: string): void
    setVisibility(visible: boolean): void
    onFocus(e: FocusEvent<HTMLInputElement>, name: string): void
    onBlur(e: Dayjs | null, name: string): void
    onKeyDown(e: KeyboardEvent<HTMLInputElement>): void
    onClick?(e: MouseEvent<HTMLInputElement>): void
    name: string
    openOnFocus: boolean
    outputFormat: string
    fullFormat: string
}

export interface WithDecoratorsProps {
    name: string
    setVisibility(visible: boolean): void
    disabled: boolean
    inputClassName?: string
    setControlRef?(el: HTMLInputElement | null): void
    onClick?(e: MouseEvent<HTMLButtonElement>): void
}
