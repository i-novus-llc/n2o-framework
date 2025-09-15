import React, { useMemo } from 'react'
import { useMaskito } from '@maskito/react'
import { type MaskitoOptions, maskitoTransform } from '@maskito/core'
import { maskitoWithPlaceholder } from '@maskito/kit'

import { useInputController } from './helpers/input/useInputController'
import { type InputProps } from './helpers/input/types'
import { COUNTRY_PHONE_CONFIGS, DEFAULT_COUNTRY, DEFAULT_COUNTRIES, DEFAULT_INVALID_TEXT } from './helpers/input/phone'

export interface InputPhoneProps extends InputProps {
    countries?: string[]
}

export function InputPhone({
    id,
    className,
    onChange,
    onBlur,
    onFocus,
    onMessage,
    value,
    placeholder,
    disabled = false,
    clearOnBlur = false,
    countries = DEFAULT_COUNTRIES,
    invalidText = DEFAULT_INVALID_TEXT,
}: InputPhoneProps) {
    const countryConfig = useMemo(() => {
        const countryCode = countries[0]?.toUpperCase() || DEFAULT_COUNTRY

        return COUNTRY_PHONE_CONFIGS[countryCode as keyof typeof COUNTRY_PHONE_CONFIGS] ||
            COUNTRY_PHONE_CONFIGS.DEFAULT
    }, [countries])

    const isValidPhone = (phone: string): boolean => {
        return countryConfig.validate(phone)
    }

    const formattedInitialValue = useMemo(() => {
        if (!value) { return '' }

        return maskitoTransform(value, {
            mask: countryConfig.mask,
            ...maskitoWithPlaceholder(placeholder || countryConfig.placeholder),
        })
    }, [value, countryConfig, placeholder])

    const { stateValue, handleChange, handleBlur, inputClassName } = useInputController({
        value: formattedInitialValue,
        onChange,
        onBlur,
        onMessage,
        invalidText,
        clearOnBlur,
        validate: isValidPhone,
        className,
    })

    const options: MaskitoOptions = useMemo(() => {
        const maskitoPlaceholder = maskitoWithPlaceholder(
            placeholder || countryConfig.placeholder,
        )

        return { mask: countryConfig.mask, ...maskitoPlaceholder }
    }, [countryConfig, placeholder])

    const maskRef = useMaskito({ options })

    return (
        <input
            type="tel"
            id={id}
            ref={maskRef}
            value={stateValue}
            disabled={disabled}
            className={inputClassName}
            placeholder={placeholder || countryConfig.placeholder}
            onInput={handleChange}
            onBlur={handleBlur}
            onFocus={onFocus}
            pattern={countryConfig.pattern}
        />
    )
}
