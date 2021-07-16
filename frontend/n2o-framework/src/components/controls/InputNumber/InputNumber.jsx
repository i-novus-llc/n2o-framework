import React from 'react'
import PropTypes from 'prop-types'
import cn from 'classnames'
import toNumber from 'lodash/toNumber'
import toString from 'lodash/toString'
import isNil from 'lodash/isNil'
import isNaN from 'lodash/isNaN'
import isEqual from 'lodash/isEqual'

import withRightPlaceholder from '../withRightPlaceholder'
import Input from '../Input/Input'

import {
    formatToFloat,
    isValid,
    matchesWhiteList,
    getPrecision,
} from './utils'

const inputMode = {
    DEFAULT: 'default',
    PICKER: 'picker',
}

/**
 * Компонент - инпут для ввода чисел с возможностью увеличения/уменьшения значения на шаг
 * @reactProps {number} value - начальное значение
 * @reactProps {boolean} visible - отображается или нет
 * @reactProps {boolean} disabled - задизейблен инпут или нет
 * @reactProps {string} step - шаг, на который увеличивается / уменьшается значение
 * @reactProps {number} min - минимальное возможное значение
 * @reactProps {number} max - максимальное возможное значение
 * @reactProps {string} name - имя поля
 * @reactProps {number} showButtons - отображать кнопки для увеличения/уменьшения значения / не отображать
 * @reactProps {number} onChange - выполняется при изменении значения поля
 * @reactProps {number} precision - количество знаков после запятой
 * @example
 * <InputNumber onChange={this.onChange}
 *             value={1}
 *             step='0.1'
 *             name='InputNumberExample' />
 */
export class InputNumber extends React.Component {
    constructor(props) {
        super(props)
        const { value } = props

        this.stepPrecition = getPrecision(props.step)
        this.pasted = false
        this.state = {
            value: this.resolveValue(
                !isNil(value) && !isNaN(toNumber(value)) && value !== ''
                    ? toNumber(value)
                    : null,
            ),
        }
        this.onChange = this.onChange.bind(this)
        this.onPaste = this.onPaste.bind(this)
        this.onKeyDown = this.onKeyDown.bind(this)
        this.onBlur = this.onBlur.bind(this)
        this.resolveValue = this.resolveValue.bind(this)
    }

    componentDidUpdate(prevProps) {
        const { value } = this.props

        if (prevProps.value !== value && !isNil(value)) {
            this.setState({ value: this.resolveValue(value) })
        } else if (
            !isEqual(prevProps.value, value) &&
            (value === '' || isNil(value))
        ) {
            this.setState({ value: null })
        }
    }

    /**
     * Обработчик вставки
     * @param {Event} evt - событие
     */
    onPaste(evt) {
        this.pasted = true
        this.setState({ value: this.resolveValue(evt.target.value) })
    }

    resolveValue(value) {
        const { precision } = this.props

        const ceilValue = Math.trunc(value)
        const isFloat = value % 1 !== 0

        if (value === null || value === '' || isNaN(toNumber(value))) {
            return value
        }

        if (precision === undefined) {
            return ceilValue
        } if (precision === null) {
            return value
        }

        return isFloat
            ? value
                .toString()
                .substr(0, ceilValue.toString().length + 1 + precision)
            : value
    }

    onChange(value) {
        let num

        if (value === '') {
            num = null
        } else if (value === '-') {
            num = value
        } else {
            num = toNumber(value)
        }

        const parsedValue = this.resolveValue(num)
        const { onChange, mode } = this.props

        if (isNil(parsedValue)) {
            this.setState({ value: null }, () => onChange(null))
        }

        if (matchesWhiteList(parsedValue) || this.pasted) {
            this.setState({ value: this.resolveValue(value) }, () => {
                if (!isNaN(toNumber(value)) || mode === inputMode.PICKER) {
                    onChange(this.resolveValue(value))
                }
            })
        }
    }

    /**
     * Обрабатывает изменение значения с клавиатуры
     * @param type {string} - 'up' (увеличение значения) или 'down' (уменьшение значения)
     */
    buttonHandler(type) {
        const { min, max, step, onChange, onBlur } = this.props
        const { value } = this.state
        const delta = toNumber(formatToFloat(step, this.stepPrecition))
        const val = !isNil(value) && value !== ''
            ? toNumber(value).toFixed(this.stepPrecition)
            : null
        const currentValue = toNumber(formatToFloat(val, this.stepPrecition))
        let newValue = currentValue

        if (type === 'up') {
            newValue = currentValue + delta
        } else if (type === 'down') {
            newValue = currentValue - delta
        }

        if (isValid(newValue, min, max)) {
            newValue = newValue.toFixed(this.stepPrecition)

            this.setState({ value: newValue }, () => {
                onChange(newValue)
                onBlur(newValue)
            })
        }
    }

    onBlur() {
        const { max, min, onBlur, mode } = this.props
        const { value: stateValue } = this.state

        if (stateValue === '-' && mode !== inputMode.PICKER) {
            return
        }

        const value = this.resolveValue(formatToFloat(stateValue))

        this.pasted = false

        if (!isNil(value) && isValid(value, min, max)) {
            this.setState({ value }, () => onBlur(value))
        } else {
            this.setState({ value: null }, () => onBlur())
        }
    }

    /**
     * Вызывает buttonHandler с нужным аргументом (в зависимости от нажатой клавиши)
     * @param e
     */
    onKeyDown(e) {
        const upKeyCode = 38
        const downKeyCode = 40
        let type

        if (e.keyCode === upKeyCode) {
            type = 'up'
        }
        if (e.keyCode === downKeyCode) {
            type = 'down'
        }

        if (type) {
            e.preventDefault()
            this.buttonHandler(type)
        }
    }

    /**
     * Базовый рендер
     * */
    render() {
        const {
            visible,
            disabled,
            name,
            step,
            min,
            max,
            showButtons,
            className,
            onFocus,
            autoFocus,
            placeholder,
        } = this.props
        const { value } = this.state

        return (
            visible && (
                <div
                    className="n2o-input-number"
                    ref={(input) => {
                        this.input = input
                    }}
                >
                    <Input
                        onKeyDown={this.onKeyDown}
                        name={name}
                        value={toString(value)}
                        step={step}
                        min={min}
                        max={max}
                        className={cn(['form-control', { [className]: className }])}
                        onBlur={this.onBlur}
                        onFocus={onFocus}
                        onChange={({ target }) => this.onChange(target.value)}
                        onPaste={this.onPaste}
                        disabled={disabled}
                        autoFocus={autoFocus}
                        placeholder={placeholder}
                    />
                    {showButtons && (
                        <div className="n2o-input-number-buttons">
                            <button
                                type="button"
                                onClick={() => this.buttonHandler('up')}
                                disabled={disabled}
                                tabIndex={-1}
                            >
                                <i className="fa fa-angle-up" aria-hidden="true" />
                            </button>
                            <button
                                type="button"
                                onClick={() => this.buttonHandler('down')}
                                disabled={disabled}
                                tabIndex={-1}
                            >
                                <i className="fa fa-angle-down" aria-hidden="true" />
                            </button>
                        </div>
                    )}
                </div>
            )
        )
    }
}

InputNumber.defaultProps = {
    disabled: false,
    visible: true,
    step: '0.1',
    autoFocus: false,
    showButtons: true,
    onChange: () => {},
    onBlur: () => {},
    onFocus: () => {},
    mode: inputMode.DEFAULT,
}

InputNumber.propTypes = {
    /**
     * Значение
     */
    value: PropTypes.number,
    /**
     * Флаг видимости
     */
    visible: PropTypes.bool,
    /**
     * Флаг активности
     */
    disabled: PropTypes.bool,
    /**
     * Шаг для изменения значения по кнопкам
     */
    step: PropTypes.string,
    /**
     * Минимальное значение
     */
    min: PropTypes.number,
    /**
     * Максимальное значение
     */
    max: PropTypes.number,
    /**
     * Название контрола
     */
    name: PropTypes.string,
    /**
     * Флаг показа кнопок изменения значения
     */
    showButtons: PropTypes.bool,
    /**
     * Callback на изменение
     */
    onChange: PropTypes.func,
    onBlur: PropTypes.func,
    onFocus: PropTypes.func,
    /**
     * Класс
     */
    className: PropTypes.string,
    /**
     * Флаг автофокуса на контрол
     */
    autoFocus: PropTypes.bool,
    /**
     * Количество знаков после запятой
     */
    precision: PropTypes.number,
    /**
     * Режим использования компонента
     */
    mode: PropTypes.oneOf([inputMode.DEFAULT, inputMode.PICKER]),
    placeholder: PropTypes.string,
}

export const InputNumberPlaceholder = withRightPlaceholder(InputNumber)
export default InputNumberPlaceholder
