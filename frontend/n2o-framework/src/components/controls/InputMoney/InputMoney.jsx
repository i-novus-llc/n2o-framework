import React from 'react'
import PropTypes from 'prop-types'
import { withTranslation } from 'react-i18next'
import cn from 'classnames'
import isEqual from 'lodash/isEqual'
import forOwn from 'lodash/forOwn'
import isEmpty from 'lodash/isEmpty'
import split from 'lodash/split'
import replace from 'lodash/replace'
import includes from 'lodash/includes'
import isNaN from 'lodash/isNaN'
import last from 'lodash/last'
import toNumber from 'lodash/toNumber'
import isNil from 'lodash/isNil'

import InputMask from '../InputMask/InputMask'

const ReplaceableChar = {
    PREFIX: 'prefix',
    SUFFIX: 'suffix',
    THOUSANDS_SYMBOL: 'thousandsSeparatorSymbol',
    DECIMAL_SYMBOL: 'decimalSymbol',
}

/**
 * Компонент ввода денег
 * @reactProps {string} value - значение
 * @reactProps {string} className - класс компонента
 * @reactProps {string} prefix - вывод валюты в начале
 * @reactProps {string}  suffix - вывод валюты в конце
 * @reactProps {boolean}   includeThousandsSeparator - флаг включения разделителя тысяч
 * @reactProps {string}   thousandsSeparatorSymbol - разделитель тысяч
 * @reactProps {boolean}  allowDecimal - флаг разрешения float
 * @reactProps {string}  decimalSymbol - разделитель float
 * @reactProps {number}  decimalLimit - лимит float
 * @reactProps {number}  integerLimit - целочисленный лимит
 * @reactProps {boolean}  allowNegative - флаг включения отрицательных чисел
 * @reactProps {boolean}  allowLeadingZeroes - флаг разрешения нулей вначале
 * @example
 */
class InputMoney extends React.Component {
    constructor(props) {
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

    componentDidUpdate(prevProps) {
        const { value } = this.props

        if (!isEqual(prevProps.value, value)) {
            this.setState({ value })
        }
    }

    convertToMoney(value) {
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

    onBlur(value) {
        const { onBlur } = this.props
        const convertedValue = parseFloat(this.convertToFloat(value))

        if (onBlur) {
            onBlur(!isNaN(convertedValue) ? convertedValue : null)
        }
        this.setState({ value: convertedValue })
    }

    replaceSpecialSymbol = (value, searchValue, replaceValue) => (
        value.replace(searchValue, replaceValue)
    )

    convertToFloat(value) {
        const {
            allowDecimal,
            [ReplaceableChar.DECIMAL_SYMBOL]: DECIMAL_SYMBOL,
        } = this.props
        let convertedValue = value.toString()

        forOwn(ReplaceableChar, (char) => {
            const { [char]: replaceableChar } = this.props

            if (!isEmpty(replaceableChar)) {
                const pattern = replaceableChar.replace(
                    /[$()*+./?[\\\]^{|}-]/g,
                    '\\$&',
                )
                const regExp = new RegExp(pattern, 'g')
                const replaceableValue = char === ReplaceableChar.DECIMAL_SYMBOL ? '.' : ''

                convertedValue = this.replaceSpecialSymbol(
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

        if (
            includes(convertedValue, '.') &&
            last(split(convertedValue, '.')).length === 1
        ) {
            convertedValue += '0'
        }

        this.setState({ value: convertedValue })

        return convertedValue
    }

    onChange(value) {
        const { onChange, allowNegative } = this.props

        if (isNaN(toNumber(value))) { return }

        const convertedValue = (allowNegative && value === '-') || isNil(value)
            ? value
            : parseFloat(this.convertToFloat(value))

        if (onChange) {
            onChange(!isNaN(convertedValue) ? convertedValue : null)
        }
        this.setState({ value: convertedValue })
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
            preset: 'money',
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
}

InputMoney.propTypes = {
    /**
     * Значение
     */
    value: PropTypes.string,
    /**
     * Класс контрола
     */
    className: PropTypes.string,
    /**
     * Строка перед значением
     */
    prefix: PropTypes.string,
    /**
     * Строка после значния
     */
    suffix: PropTypes.string,
    /**
     * Флаг включения разделения по тысячам
     */
    includeThousandsSeparator: PropTypes.bool,
    /**
     * Символ разделяющий тысячи
     */
    thousandsSeparatorSymbol: PropTypes.string,
    /**
     * Разрешить копейки
     */
    allowDecimal: PropTypes.bool,
    /**
     * Символ разделитель копеек
     */
    decimalSymbol: PropTypes.string,
    /**
     * Лимит на количество символов после запятой
     */
    decimalLimit: PropTypes.number,
    /**
     * Целочисленный лимит
     */
    integerLimit: PropTypes.any,
    /**
     * Разрешить ввод отрицательных числе
     */
    allowNegative: PropTypes.bool,
    allowLeadingZeroes: PropTypes.bool,
    t: PropTypes.func,
    onBlur: PropTypes.func,
    onChange: PropTypes.func,
    guide: PropTypes.oneOfType([PropTypes.string, PropTypes.bool]),
}

InputMoney.defaultProps = {
    value: '',
    prefix: '',
    includeThousandsSeparator: true,
    thousandsSeparatorSymbol: ' ',
    allowDecimal: true,
    decimalSymbol: ',',
    decimalLimit: 2,
    integerLimit: 15,
    allowNegative: false,
    allowLeadingZeroes: false,
    guide: false,
    t: () => {},
}

export { InputMoney }

export default withTranslation()(InputMoney)
