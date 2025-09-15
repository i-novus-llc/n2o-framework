import React, { useCallback } from 'react'
import { useMaskito } from '@maskito/react'
import { MaskitoOptions } from '@maskito/core'
import { maskitoWithPlaceholder } from '@maskito/kit'

import { Versions, DEFAULT_PLACEHOLDER, DEFAULT_INVALID_TEXT, validateUUID, UUID_MASK_ARRAY } from './helpers/input/uuid'
import { useInputController } from './helpers/input/useInputController'
import { type InputProps } from './helpers/input/types'

const UUID_MASK: MaskitoOptions = { mask: UUID_MASK_ARRAY }

export interface InputUUIDProps extends InputProps {
    uuidVersion?: Versions
}

export function InputUUID({
    id,
    className,
    onChange,
    onBlur,
    onFocus,
    onMessage,
    value,
    disabled = false,
    clearOnBlur = false,
    uuidVersion = Versions.ANY,
    placeholder = DEFAULT_PLACEHOLDER,
    invalidText = DEFAULT_INVALID_TEXT,
}: InputUUIDProps) {
    const isValidUUID = useCallback((value) => {
        return validateUUID(value, uuidVersion)
    }, [uuidVersion])

    const { stateValue, handleChange, handleBlur, inputClassName } = useInputController({
        value,
        onChange,
        onBlur,
        onMessage,
        invalidText,
        clearOnBlur,
        validate: isValidUUID,
        className,
    })
    const placeholderOptions = maskitoWithPlaceholder(placeholder)

    const createOptions = (): MaskitoOptions => {
        const baseMask = [...UUID_MASK_ARRAY]

        if (uuidVersion !== Versions.ANY) {
            // Для конкретной версии фиксируем нужную цифру
            if (baseMask[14] instanceof RegExp) {
                baseMask[14] = uuidVersion.charAt(1)
            }
            // Для конкретных версий требуем вариант RFC 4122
            if (baseMask[19] instanceof RegExp) {
                baseMask[19] = /[89ab]/i
            }
        } else {
            // Для ANY разрешаем любые hex-символы во всех позициях
            baseMask.forEach((_, index) => {
                if (![8, 13, 18, 23].includes(index)) {
                    baseMask[index] = /[\da-f]/i
                }
            })
        }

        return { ...UUID_MASK, mask: baseMask, ...placeholderOptions }
    }

    const maskRef = useMaskito({ options: createOptions() })

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
