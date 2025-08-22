import React, { ChangeEvent, FocusEvent, ComponentType } from 'react'
import classNames from 'classnames'
import { useMaskito } from '@maskito/react'
import { maskitoNumberOptionsGenerator } from '@maskito/kit'
import { type MaskitoOptions, maskitoTransform } from '@maskito/core'

import { removeTrailingExclusions, formatNumber, replaceChar, removeAllSpaces } from './utils'
import { type CommonMaskedInputProps } from './types'

/** символ с которым хранится float в хранилище после изменения */
export const STORE_DECIMAL_SYMBOL = '.'

export interface InputMoneyProps extends CommonMaskedInputProps {
    allowDecimal?: boolean
    allowNegative?: boolean
    decimalLimit?: number
    decimalSymbol?: string
    integerLimit?: number
    prefix?: string
    requireDecimal?: boolean
    suffix?: string
    thousandsSeparatorSymbol?: string
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

function Component({
    className,
    value: defaultValue,
    onChange,
    onBlur,
    disabled,
    autocomplete,
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

        onChange?.(maskedValue || null)
    }

    const handleBlur = (e: FocusEvent<HTMLInputElement>): void => {
        const { value: maskedValue } = e.target

        onBlur?.(maskedValue || null)
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
                autoComplete={autocomplete}
            />
        </div>
    )
}

/** Очистка значения от приставок */
export function withStoreEvent(
    WrappedComponent: ComponentType<InputMoneyProps>,
) {
    return function WithInterceptors(props: InputMoneyProps) {
        const { onChange, onBlur, suffix, decimalSymbol = ',' } = props

        const cleanValue = (value: string | null): string | null => {
            /** очистка от suffix */
            let result = suffix ? removeTrailingExclusions(value, [suffix]) : value

            /** очистка от пробелов */
            result = removeAllSpaces(result)

            /** замена decimalSymbol на STORE_DECIMAL_SYMBOL  */
            return replaceChar(result, decimalSymbol, STORE_DECIMAL_SYMBOL)
        }

        const handleChange = (value: string | null) => {
            onChange?.(cleanValue(value))
        }

        const handleBlur = (value: string | null) => {
            let result = cleanValue(value)

            if (result?.endsWith(STORE_DECIMAL_SYMBOL)) {
                result = removeTrailingExclusions(result, [STORE_DECIMAL_SYMBOL])
                onChange?.(result || null)
                onBlur?.(result || null)

                return
            }

            onBlur?.(result)
        }

        return (
            <WrappedComponent
                {...props}
                onChange={handleChange}
                onBlur={handleBlur}
            />
        )
    }
}

export const InputMoney = withStoreEvent(Component)
