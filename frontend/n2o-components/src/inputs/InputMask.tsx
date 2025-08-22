import React, { ChangeEvent, FocusEvent, useEffect, useState } from 'react'
import classNames from 'classnames'

import { withRightPlaceholder } from '../helpers/withRightPlaceholder'

import { useMask } from './useMask'
import { type CommonMaskedInputProps } from './types'

export interface MaskProps extends CommonMaskedInputProps {
    placeholder: string
    id: string
    clearOnBlur: boolean
    mask: string
}

function Component({
    placeholder,
    disabled,
    id,
    clearOnBlur,
    className,
    onChange,
    onBlur,
    autocomplete,
    value: propsValue,
    mask = '',
}: MaskProps) {
    const { maskRef, isMaskFilled, maskedValue } = useMask(mask, propsValue)
    // для отображения не полность заполненного value (при clearOnBlur = false)
    const [value, setValue] = useState<string>(maskedValue)

    useEffect(() => {
        if (maskedValue !== value) { setValue(maskedValue) }
    }, [maskedValue])

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { value: targetValue } = e.target

        setValue(targetValue)

        if (isMaskFilled(targetValue)) {
            onChange?.(targetValue)
        }
    }

    const handleBlur = (e: FocusEvent<HTMLInputElement>): void => {
        const { value: targetValue } = e.target

        if (clearOnBlur && !isMaskFilled(targetValue)) {
            onChange?.(null)
            onBlur?.(null)

            setValue('')

            return
        }

        onBlur?.(targetValue)
    }

    return (
        <input
            value={value}
            id={id}
            disabled={disabled}
            className={classNames(['form-control', 'n2o-input-mask', className])}
            ref={maskRef}
            placeholder={placeholder}
            onInput={handleChange}
            onBlur={handleBlur}
            autoComplete={autocomplete}
        />
    )
}

export const InputMask = withRightPlaceholder(Component)

Component.displayName = 'InputMask'
