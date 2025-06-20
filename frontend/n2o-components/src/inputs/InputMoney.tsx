import React from 'react'
import cn from 'classnames'
import isNaN from 'lodash/isNaN'
import isEqual from 'lodash/isEqual'
import isNil from 'lodash/isNil'
import forOwn from 'lodash/forOwn'
import includes from 'lodash/includes'
import last from 'lodash/last'
import split from 'lodash/split'
import replace from 'lodash/replace'
import isUndefined from 'lodash/isUndefined'

import { TBaseProps } from '../types'

import { InputMask, PresetType } from './InputMask'

type InputMoneyProps = TBaseProps & typeof defaultProps & {
    allowDecimal?: boolean,
    allowLeadingZeroes?: boolean,
    allowNegative?: boolean,
    decimalLimit?: number,
    decimalSymbol?: string,
    guide?: boolean,
    includeThousandsSeparator?: boolean,
    integerLimit?: number,
    onBlur?(value: number | null): void,
    onChange?(value: string | null): void,
    prefix?: string,
    suffix?: string,
    t?(str?: string): string,
    thousandsSeparatorSymbol?: string,
    value?: string
}

export const ReplaceableChar = {
    PREFIX: 'prefix',
    SUFFIX: 'suffix',
    THOUSANDS_SYMBOL: 'thousandsSeparatorSymbol',
    DECIMAL_SYMBOL: 'decimalSymbol',
} as const

const replaceSpecialSymbol = (value: string, searchValue: RegExp | string, replaceValue: string) => (
    value.replace(searchValue, replaceValue)
)

const defaultProps = {
    value: '',
    includeThousandsSeparator: true,
    thousandsSeparatorSymbol: ' ',
    allowDecimal: true,
    decimalSymbol: ',',
    decimalLimit: 2,
    integerLimit: 15,
    allowNegative: false,
    allowLeadingZeroes: false,
    guide: false,
    t: (str: string) => str,
}

type State = {
    value: string
}

export class InputMoney extends React.Component<InputMoneyProps, State> {
    constructor(props: InputMoneyProps) {
        super(props)

        this.state = {
            value: props.value,
        }

        this.onBlur = this.onBlur.bind(this)
        this.onChange = this.onChange.bind(this)
        this.convertToMoney = this.convertToMoney.bind(this)
        this.convertToFloat = this.convertToFloat.bind(this)
        this.getInputMoneyProps = this.getInputMoneyProps.bind(this)
    }

    componentDidUpdate(prevProps: InputMoneyProps) {
        const { value } = this.props

        if (!isEqual(prevProps.value, value)) {
            this.setState({ value })
        }
    }

    onBlur(value: string) {
        const { onBlur } = this.props
        const convertedValue = parseFloat(this.convertToFloat(value))

        if (onBlur) {
            onBlur(!isNaN(convertedValue) ? convertedValue : null)
        }

        this.setState({ value: isNaN(convertedValue) ? '' : String(convertedValue) })
    }

    convertToMoney(value: string) {
        const {
            allowDecimal,
            [ReplaceableChar.DECIMAL_SYMBOL]: DECIMAL_SYMBOL,
        } = this.props

        if (value !== '') {
            value = replace(value, '.', DECIMAL_SYMBOL)
        }

        const splitBySymbol = split(value, DECIMAL_SYMBOL)

        if (!allowDecimal) {
            return splitBySymbol[0]
        }

        return value
    }

    convertToFloat(value: string) {
        const {
            allowDecimal,
            [ReplaceableChar.DECIMAL_SYMBOL]: DECIMAL_SYMBOL,
        } = this.props
        let convertedValue = value.toString()

        forOwn(ReplaceableChar, (char) => {
            const { [char]: replaceableChar } = this.props

            if (!isUndefined(replaceableChar)) {
                const pattern = replaceableChar.replace(
                    /[$()*+./?[\\\]^{|}-]/g,
                    '\\$&',
                )
                const regExp = new RegExp(pattern, 'g')
                const replaceableValue = char === ReplaceableChar.DECIMAL_SYMBOL ? '.' : ''

                convertedValue = replaceSpecialSymbol(
                    convertedValue,
                    regExp,
                    replaceableValue,
                )
            }
        })

        const { value: stateValue } = this.state

        if (
            allowDecimal &&
            includes(stateValue, '.') &&
            !includes(value, DECIMAL_SYMBOL)
        ) {
            convertedValue = convertedValue.substring(0, convertedValue.length - 3)
        }

        return convertedValue
    }

    trimNonNumericEnd = (value: string) => (value && /[,.]$/.test(value) ? value.slice(0, -1) : value)

    onChange(valueProps: string) {
        const { onChange, allowNegative } = this.props
        const value = valueProps.replace(/\s+/g, '')

        const convertedValue = (allowNegative && value === '-') || isNil(value)
            ? value
            : this.trimNonNumericEnd(this.convertToFloat(value))

        onChange?.(isNaN(convertedValue) ? null : String(convertedValue))
        this.setState({ value: isNaN(convertedValue) ? '' : String(convertedValue) })
    }

    getInputMoneyProps() {
        const {
            t,
            value,
            className,
            suffix = t('rub'),
            prefix,
            includeThousandsSeparator,
            thousandsSeparatorSymbol,
            allowDecimal,
            decimalSymbol,
            decimalLimit,
            integerLimit,
            allowNegative,
            allowLeadingZeroes,
        } = this.props
        const { value: stateValue } = this.state

        return {
            ...this.props,
            preset: 'money' as PresetType,
            value: this.convertToMoney(value || stateValue),
            onChange: this.onChange,
            onBlur: this.onBlur,
            className: cn('n2o-input-money', className),
            presetConfig: {
                suffix,
                prefix,
                includeThousandsSeparator,
                thousandsSeparatorSymbol,
                allowDecimal,
                decimalSymbol,
                decimalLimit,
                integerLimit,
                requireDecimal: false,
                allowNegative,
                allowLeadingZeroes,
            },
        }
    }

    render() {
        return <InputMask {...this.getInputMoneyProps()} />
    }

    static defaultProps = defaultProps
}
