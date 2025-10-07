import React, { useMemo } from 'react'

import { useInputController } from './helpers/input/useInputController'
import { type InputProps } from './helpers/input/types'
import {
    COUNTRY_PHONE_CONFIGS,
    DEFAULT_COUNTRIES,
    DEFAULT_INVALID_TEXT,
    CountryIsoCodes,
} from './helpers/input/phone'
import { useMask } from './helpers/input/useMask'
import { PROCESSORS } from './helpers/input/processors'

export interface InputPhoneProps extends InputProps {
    countries?: CountryIsoCodes[]
}

export function InputPhone({
    autocomplete,
    className,
    id,
    onChange,
    onBlur,
    onFocus,
    onMessage,
    placeholder,
    value,
    clearOnBlur = false,
    countries = DEFAULT_COUNTRIES,
    invalidText = DEFAULT_INVALID_TEXT,
    disabled = false,
}: InputPhoneProps) {
    // @INFO пока поддерживается только 1 код
    const countryCode = countries[0] || CountryIsoCodes.RU

    const config = useMemo(() => {
        return COUNTRY_PHONE_CONFIGS[countryCode] || COUNTRY_PHONE_CONFIGS.DEFAULT
    }, [countryCode])

    const { maskRef, maskedValue } = useMask(
        {
            mask: config.mask,
            placeholder: config.placeholder,
            defaultValue: value,
            processors: PROCESSORS.phone[countryCode],
        },
    )

    const prepareToStore = (value: string) => {
        // @INFO убирает все разделители
        return value.replace(/[^\d+]/g, '')
    }

    const { stateValue, handleChange, handleBlur, inputClassName } = useInputController({
        value: maskedValue,
        onChange,
        onBlur,
        onMessage,
        invalidText,
        clearOnBlur,
        prepareToStore,
        validate: config.validate,
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
            placeholder={placeholder}
            onInput={handleChange}
            onBlur={handleBlur}
            onFocus={onFocus}
            pattern={config.pattern}
        />
    )
}
