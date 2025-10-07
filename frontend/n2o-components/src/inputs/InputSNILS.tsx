import React from 'react'

import { useInputController } from './helpers/input/useInputController'
import { type InputProps } from './helpers/input/types'
import {
    DEFAULT_PLACEHOLDER,
    DEFAULT_INVALID_TEXT,
    SNILS_MASK,
    isValidSNILS,
    formatSNILS,
} from './helpers/input/snils'
import { useMask } from './helpers/input/useMask'

export function InputSNILS({
    autocomplete,
    className,
    clearOnBlur = false,
    disabled = false,
    id,
    invalidText = DEFAULT_INVALID_TEXT,
    onChange,
    onBlur,
    onFocus,
    onMessage,
    placeholder,
    value,
}: InputProps) {
    const { maskRef, maskedValue } = useMask(
        {
            mask: SNILS_MASK,
            placeholder: DEFAULT_PLACEHOLDER,
            defaultValue: formatSNILS(value),
        },
    )

    const prepareToStore = (value: string) => {
        return value.replace(/\D/g, '')
    }

    const { stateValue, handleChange, handleBlur, inputClassName } = useInputController({
        value: maskedValue,
        onChange,
        onBlur,
        onMessage,
        invalidText,
        clearOnBlur,
        validate: isValidSNILS,
        className,
        prepareToStore,
    })

    return (
        <input
            autoComplete={autocomplete}
            id={id}
            ref={maskRef}
            value={stateValue}
            disabled={disabled}
            className={inputClassName}
            placeholder={placeholder}
            onInput={handleChange}
            onBlur={handleBlur}
            onFocus={onFocus}
        />
    )
}
