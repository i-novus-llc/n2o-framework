import React from 'react'

import { withRightPlaceholder } from '../helpers/withRightPlaceholder'

import { useMask } from './helpers/input/mask'
import { useInputController } from './helpers/input/useInputController'
import { type InputProps } from './helpers/input/types'

export interface MaskProps extends InputProps {
    mask?: string
}

function Component({
    placeholder,
    disabled,
    id,
    clearOnBlur,
    className,
    onChange,
    onBlur,
    onMessage,
    invalidText,
    value,
    mask = '',
    autocomplete,
}: MaskProps) {
    const { maskRef, isMaskFilled, maskedValue } = useMask(mask, value)

    const { stateValue, handleChange, handleBlur, inputClassName } = useInputController({
        value: maskedValue,
        onChange,
        onBlur,
        onMessage,
        invalidText,
        clearOnBlur,
        validate: isMaskFilled,
        className,
    })

    return (
        <input
            value={stateValue}
            id={id}
            disabled={disabled}
            className={inputClassName}
            ref={maskRef}
            placeholder={placeholder}
            onInput={handleChange}
            onBlur={handleBlur}
            autoComplete={autocomplete}
        />
    )
}

export const InputMask = withRightPlaceholder(Component)
