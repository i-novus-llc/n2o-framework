import React, { useCallback, useEffect } from 'react'
import PropTypes from 'prop-types'
import cn from 'classnames'
import isNil from 'lodash/isNil'
import isNaN from 'lodash/isNaN'

// eslint-disable-next-line import/no-named-as-default
import InputNumber from '../InputNumber/InputNumber'

import { NumberPickerButton } from './NumberPickerButton'

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

function NumberPicker(props) {
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

    let defaultValue = 0

    if (value === '-') {
        defaultValue = min || 0
    } else if (!isNil(value) && value !== '') {
        defaultValue = value
    } else if (!isNil(min) && min !== '') {
        defaultValue = min
    } else if (!isNil(max) && max !== '') {
        defaultValue = max
    }

    useEffect(() => {
        onChange(defaultValue)
    }, [defaultValue, onChange])

    const onBlur = useCallback(() => {
        if (value < min) {
            onChange(min)
        } else if (value > max) {
            onChange(max)
        } else if (isNil(value) || value === '' || isNaN(parseInt(value, 10))) {
            onChange(defaultValue)
        }
    }, [min, max, value, onChange, defaultValue])

    const handlerChange = (step) => {
        const nextValue = Number(value) + step

        if (isNil(value) || value === '') {
            onChange(defaultValue)
        } else if (min <= nextValue && nextValue <= max) {
            onChange(nextValue)
        } else if (nextValue < min) {
            onChange(min)
        } else if (nextValue > max) {
            onChange(max)
        }
    }

    return (
        visible && (
            <div className={cn('n2o-number-picker', className)} style={style}>
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
