import React, { ChangeEvent, FocusEvent } from 'react'
import classNames from 'classnames'
import { useMaskito } from '@maskito/react'
import { maskitoNumberOptionsGenerator } from '@maskito/kit'
import { maskitoTransform } from '@maskito/core'

export interface InputMoneyProps {
    allowDecimal?: boolean
    allowNegative?: boolean
    className?: string
    decimalLimit?: number
    decimalSymbol?: string
    disabled?: boolean
    integerLimit?: number
    prefix?: string
    requireDecimal?: boolean
    suffix?: string
    thousandsSeparatorSymbol?: string
    value?: string
    onChange?(value: string | null): void
    onBlur?(value: string | null): void
}

const cleanValueFromSuffix = (value: string, suffix?: string): string => {
    if (!value) { return value }

    return value.replace(new RegExp(`${suffix}$`), '').trim()
}

export function InputMoney({
    className,
    value: defaultValue,
    onChange,
    onBlur,
    disabled,
    allowNegative = false,
    requireDecimal = false,
    decimalLimit = 2,
    integerLimit = 15,
    decimalSymbol = ',',
    thousandsSeparatorSymbol = '.',
    prefix = '',
    suffix = ' руб.',
    allowDecimal = true,
}: InputMoneyProps) {
    const options = maskitoNumberOptionsGenerator({
        decimalSeparator: decimalSymbol,
        thousandSeparator: thousandsSeparatorSymbol,
        postfix: suffix,
        prefix,
        minimumFractionDigits: (allowDecimal && requireDecimal) ? decimalLimit : 0,
        maximumFractionDigits: allowDecimal ? decimalLimit : 0,
        min: allowNegative ? -Infinity : 0,
    })

    const maskRef = useMaskito({ options })
    const value = defaultValue ? maskitoTransform(String(defaultValue), options) : ''

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { value: maskedValue } = e.target

        const cleanValue = maskedValue
            .replace(new RegExp(`[^0-9${decimalSymbol}${allowNegative ? '-' : ''}]`, 'g'), '')

        const [integerPart] = cleanValue.split(decimalSymbol)

        if (integerPart && integerPart.replace(/-/g, '').length > integerLimit) {
            return
        }

        onChange?.(cleanValueFromSuffix(maskedValue, suffix) || null)
    }

    const handleBlur = (e: FocusEvent<HTMLInputElement>): void => {
        const { value: targetValue } = e.target

        onBlur?.(cleanValueFromSuffix(targetValue, suffix) || null)
    }

    return (
        <div className="n2o-control-container">
            <input
                type="text"
                inputMode="decimal"
                value={value}
                ref={maskRef}
                className={classNames(className, 'n2o-input-money', 'form-control')}
                onInput={handleChange}
                onBlur={handleBlur}
                disabled={disabled}
            />
        </div>
    )
}
