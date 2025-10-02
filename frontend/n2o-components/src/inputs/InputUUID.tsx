import React, { useCallback, useMemo } from 'react'

import { Versions, DEFAULT_PLACEHOLDER, DEFAULT_INVALID_TEXT, validateUUID, UUID_MASK_ARRAY } from './helpers/input/uuid'
import { useInputController } from './helpers/input/useInputController'
import { type InputProps } from './helpers/input/types'
import { useMask } from './helpers/input/useMask'

const prepareMaskForVersion = (version: Versions) => {
    const mask = [...UUID_MASK_ARRAY]

    if (version !== Versions.ANY) {
        // Для конкретной версии фиксируем нужную цифру
        if (mask[14] instanceof RegExp) {
            mask[14] = version.charAt(1)
        }
        // Для конкретных версий требуем вариант RFC 4122
        if (mask[19] instanceof RegExp) {
            mask[19] = /[89ab]/i
        }
    } else {
        // Для ANY разрешаем любые hex-символы во всех позициях
        mask.forEach((_, index) => {
            if (![8, 13, 18, 23].includes(index)) {
                mask[index] = /[\da-f]/i
            }
        })
    }

    return mask
}

export interface InputUUIDProps extends InputProps {
    uuidVersion?: Versions
}

export function InputUUID({
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
    uuidVersion = Versions.ANY,
}: InputUUIDProps) {
    const mask = useMemo(() => prepareMaskForVersion(uuidVersion), [uuidVersion])
    const { maskRef, maskedValue } = useMask(mask, DEFAULT_PLACEHOLDER, value)
    const isValidUUID = useCallback((value) => {
        return validateUUID(value, uuidVersion)
    }, [uuidVersion])

    const { stateValue, handleChange, handleBlur, inputClassName } = useInputController({
        value: maskedValue,
        onChange,
        onBlur,
        onMessage,
        invalidText,
        clearOnBlur,
        validate: isValidUUID,
        className,
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
