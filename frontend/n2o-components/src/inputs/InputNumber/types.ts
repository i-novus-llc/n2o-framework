import { KeyboardEvent } from 'react'

import { TBaseInputProps, TBaseProps } from '../../types'

export type InputNumberProps = TBaseProps & TBaseInputProps<TInputNumberValue> & {
    max?: number,
    min?: number,
    mode?: InputMode,
    onBlur?(value: TInputNumberValue): void,
    onKeyDown?(evt: KeyboardEvent<HTMLInputElement>): void,
    precision?: number,
    showButtons?: boolean,
    step?: string | number
}

export type InputNumberState = {
    value: TInputNumberValue
}
export const enum InputMode {
    DEFAULT = 'default',
    PICKER = 'picker',
}

export type TInputNumberValue = number | string | null
