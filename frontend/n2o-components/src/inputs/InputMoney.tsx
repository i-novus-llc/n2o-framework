import React, { ChangeEvent, FocusEvent } from 'react'
import classNames from 'classnames'
import { useMaskito } from '@maskito/react'
import { maskitoNumberOptionsGenerator } from '@maskito/kit'
import { type MaskitoOptions, maskitoTransform } from '@maskito/core'

import { removeTrailingExclusions, formatNumber } from './utils'

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
    value?: string | number | null | undefined
    onChange?(value: string | null): void
    onBlur?(value: string | null): void
}

/**
 * Преобразует значение по умолчанию для денежного поля ввода:
 * - Пустое значение → пустая строка
 * - Строка → применяет форматирование maskitoTransform
 * - Число → форматирует через formatNumber + maskitoTransform
 */
function getDefaultValue(
    defaultValue: InputMoneyProps['value'],
    decimalLimit: InputMoneyProps['decimalLimit'],
    options: MaskitoOptions,
) {
    if (!defaultValue) { return '' }

    if (typeof defaultValue === 'string') { return maskitoTransform(defaultValue, options) }

    return maskitoTransform(formatNumber(defaultValue, decimalLimit), options)
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
    thousandsSeparatorSymbol,
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

    const value = getDefaultValue(defaultValue, decimalLimit, options)

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { value: maskedValue } = e.target

        const cleanValue = maskedValue
            .replace(new RegExp(`[^0-9${decimalSymbol}${allowNegative ? '-' : ''}]`, 'g'), '')

        const [integerPart] = cleanValue.split(decimalSymbol)

        if (integerPart && integerPart.replace(/-/g, '').length > integerLimit) {
            return
        }

        onChange?.(removeTrailingExclusions(maskedValue, [suffix]) || null)
    }

    const handleBlur = (e: FocusEvent<HTMLInputElement>): void => {
        const { value: targetValue } = e.target

        const input = removeTrailingExclusions(targetValue, [suffix])

        // @INFO maskito не удаляет decimalSymbol в конце value
        if (input.endsWith(decimalSymbol)) {
            const cleanedInput = removeTrailingExclusions(input, [decimalSymbol])

            onChange?.(cleanedInput || null)
            onBlur?.(cleanedInput || null)

            return
        }

        onBlur?.(input || null)
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
