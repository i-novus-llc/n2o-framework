import React from 'react'

import { useInputController } from './helpers/input/useInputController'
import { type InputProps } from './helpers/input/types'
import { isValidEmail, DEFAULT_PLACEHOLDER, DEFAULT_INVALID_TEXT } from './helpers/input/email'

export function InputEmail({
    id,
    className,
    onChange,
    onBlur,
    onFocus,
    onMessage,
    value,
    disabled = false,
    clearOnBlur = false,
    placeholder = DEFAULT_PLACEHOLDER,
    invalidText = DEFAULT_INVALID_TEXT,
}: InputProps) {
    const { stateValue, handleChange, handleBlur, inputClassName } = useInputController({
        value,
        onChange,
        onBlur,
        onMessage,
        invalidText,
        clearOnBlur,
        validate: isValidEmail,
        className,
    })

    return (
        <input
            type="email"
            id={id}
            value={stateValue}
            disabled={disabled}
            className={inputClassName}
            placeholder={placeholder}
            onChange={handleChange}
            onBlur={handleBlur}
            onFocus={onFocus}
        />
    )
}
