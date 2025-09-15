import React from 'react'
import { useMaskito } from '@maskito/react'
import { type MaskitoOptions } from '@maskito/core'
import { maskitoWithPlaceholder } from '@maskito/kit'

import { useInputController } from './helpers/input/useInputController'
import { type InputProps } from './helpers/input/types'
import {
    DEFAULT_PLACEHOLDER,
    DEFAULT_INVALID_TEXT,
    SNILS_MASK,
    isValidSNILS,
    formatSNILS,
} from './helpers/input/snils'

export function InputSNILS({
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
        value: formatSNILS(value),
        onChange,
        onBlur,
        onMessage,
        invalidText,
        clearOnBlur,
        validate: isValidSNILS,
        className,
        storeCleanValue: true,
    })
    const placeholderOptions = maskitoWithPlaceholder(placeholder)
    const options: MaskitoOptions = { ...SNILS_MASK, ...placeholderOptions }
    const maskRef = useMaskito({ options })

    return (
        <input
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
