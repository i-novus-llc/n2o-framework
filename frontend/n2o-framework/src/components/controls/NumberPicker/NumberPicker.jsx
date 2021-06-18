import React, { useEffect, useState } from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import isNil from 'lodash/isNil'

// eslint-disable-next-line import/no-named-as-default
import InputNumber from '../InputNumber/InputNumber'

import { NumberPickerButton } from './NumberPickerButton'

/**
 * @param {string|number} value
 * @param {number} [min]
 * @param {number} [max]
 * @return {number}
 */
const parseValue = (value, min, max) => {
    if (value === '-' || value === '' || isNil(value)) {
        return min || 0
    }

    const numberValue = Number(value)

    if (!isNil(min) && numberValue < min) {
        return min
    }
    if (!isNil(max) && numberValue > max) {
        return max
    }

    return numberValue
}

/**
 * Компонент - инпут с возможностью увеличения/уменьшения значения на шаг
 * @reactProps {number} value - начальное значение
 * @reactProps {boolean} visible - отображается или нет
 * @reactProps {boolean} disabled - задизейблен инпут или нет
 * @reactProps {string} step - шаг, на который увеличивается / уменьшается значение
 * @reactProps {number} min - минимальное возможное значение
 * @reactProps {number} max - максимальное возможное значение
 * @reactProps {number} onChange - выполняется при изменении значения поля
 * @reactProps {object} style - стили компонента
 * @example
 * <NumberPicker onChange={onChange}
 *             value={1}
 *             step={1}
 *             max={100}
 *             min={1}
 * />
 */
export function NumberPicker(props) {
    const {
        visible,
        value,
        max,
        min,
        step,
        className,
        style,
        disabled,
        onChange,
    } = props
    const [isFocused, setFocus] = useState(false)
    // Не ставим крайние значения, пока поле в фокусе
    const parsedValue = isFocused ? value : parseValue(value, min, max)

    const onBlur = () => {
        onChange(parseValue(value, min, max))
        setFocus(false)
    }

    const handlerChange = (step) => {
        onChange(parseValue(value + step, min, max))
    }

    /*
     * Выставляем дефолтное значение
     * TODO по идее только для совместимости, договориться чтобы дефолтное значение приходило всегда
     *  вызывает лишний рендер
     */
    useEffect(() => {
        if (parsedValue !== value) {
            onChange(parsedValue)
        }
    }, [parsedValue, value, onChange])

    return (
        visible && (
            <div className={classNames('n2o-number-picker', className)} style={style}>
                <NumberPickerButton
                    disabled={disabled || min >= value}
                    onClick={() => handlerChange(-step)}
                >
                    <i className="fa fa-minus" aria-hidden="true" />
                </NumberPickerButton>
                <InputNumber
                    className="n2o-number-picker__input"
                    value={value}
                    min={min}
                    max={max}
                    step={step}
                    onChange={onChange}
                    showButtons={false}
                    disabled={disabled}
                    onBlur={onBlur}
                    onFocus={() => setFocus(true)}
                    mode="picker"
                />
                <NumberPickerButton
                    disabled={disabled || value >= max}
                    onClick={() => handlerChange(step)}
                >
                    <i className="fa fa-plus" aria-hidden="true" />
                </NumberPickerButton>
            </div>
        )
    )
}

NumberPicker.defaultProps = {
    visible: true,
    style: {},
    min: 0,
    max: 100,
    step: 1,
    disabled: false,
    onChange: () => {},
}

NumberPicker.PropTypes = {
    /**
     * флаг видимости
     */
    visible: PropTypes.bool,
    /**
     * значение контрола
     */
    value: PropTypes.number,
    /**
     * максимальное значение контрола
     */
    max: PropTypes.number,
    /**
     * максимальное значение контрола
     */
    min: PropTypes.number,
    /**
     * шаг изменения значения
     */
    step: PropTypes.number,
    /**
     * класс компонента
     */
    className: PropTypes.string,
    /**
     * стили компонента
     */
    style: PropTypes.object,
    /**
     * Callback на изменение
     */
    onChange: PropTypes.func,
}

export default NumberPicker
