import React, { useMemo } from 'react'

import { useInputController } from './helpers/input/useInputController'
import { type InputProps } from './helpers/input/types'
import { COUNTRY_PHONE_CONFIGS, DEFAULT_COUNTRY, DEFAULT_COUNTRIES, DEFAULT_INVALID_TEXT } from './helpers/input/phone'
import { useMask } from './helpers/input/useMask'

export interface InputPhoneProps extends InputProps {
    countries?: string[]
}

export function InputPhone({
    autocomplete,
    className,
    clearOnBlur = false,
    countries = DEFAULT_COUNTRIES,
    disabled = false,
    id,
    invalidText = DEFAULT_INVALID_TEXT,
    onChange,
    onBlur,
    onFocus,
    onMessage,
    placeholder,
    value,
}: InputPhoneProps) {
    const config = useMemo(() => {
        const countryCode = countries[0]?.toUpperCase() || DEFAULT_COUNTRY

        return COUNTRY_PHONE_CONFIGS[countryCode as keyof typeof COUNTRY_PHONE_CONFIGS] ||
            COUNTRY_PHONE_CONFIGS.DEFAULT
    }, [countries])

    const { maskRef, maskedValue } = useMask(config.mask, config.placeholder, value)
    const isValidPhone = (phone: string): boolean => {
        return config.validate(phone)
    }

    const { stateValue, handleChange, handleBlur, inputClassName } = useInputController({
        value: maskedValue,
        onChange,
        onBlur,
        onMessage,
        invalidText,
        clearOnBlur,
        validate: isValidPhone,
        className,
    })

    return (
        <input
            autoComplete={autocomplete}
            type="tel"
            id={id}
            ref={maskRef}
            value={stateValue}
            disabled={disabled}
            className={inputClassName}
            placeholder={placeholder || config.placeholder}
            onInput={handleChange}
            onBlur={handleBlur}
            onFocus={onFocus}
            pattern={config.pattern}
        />
    )
}
