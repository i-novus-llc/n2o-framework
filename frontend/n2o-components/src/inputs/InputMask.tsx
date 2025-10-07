import React, { useCallback, useMemo } from 'react'

import { withRightPlaceholder } from '../helpers/withRightPlaceholder'

import { useMask } from './helpers/input/useMask'
import { useInputController } from './helpers/input/useInputController'
import { type InputProps } from './helpers/input/types'

export interface MaskProps extends InputProps {
    mask?: string
}

function Component({
    autocomplete,
    className,
    clearOnBlur,
    disabled,
    id,
    invalidText,
    mask = '',
    onChange,
    onBlur,
    onMessage,
    value,
    placeholder,
}: MaskProps) {
    const options = useMemo(() => {
        const maskArray = Array.from(mask)

        return {
            mask: maskArray.map(char => (char === '9' ? /\d/ : char)),
            placeholder: maskArray.map(char => (char === '9' ? '_' : char)).join(('')),
        }
    }, [mask])
    const { maskRef, maskedValue } = useMask({
        mask: options.mask,
        placeholder: options.placeholder,
        defaultValue: value,
    })

    const isMaskFilled = useCallback((value: string): boolean => {
        if (value === '') { return true }

        // Количество цифровых символов в маске (количество '9')
        const digitsCount = mask.split(/\d/g).length - 1
        // Количество фактически введенных цифр
        const enteredDigits = value.replace(/\D/g, '').length

        return enteredDigits === digitsCount
    }, [])

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
