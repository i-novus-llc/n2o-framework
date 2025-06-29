import React, { KeyboardEvent, useEffect, useState } from 'react'
import classNames from 'classnames'
import isNull from 'lodash/isNull'

import { InputNumber } from '../InputNumber'
import { TBaseInputProps, TBaseProps } from '../../types'
import { EMPTY_OBJECT, NOOP_FUNCTION } from '../../utils/emptyTypes'

import { NumberPickerButton } from './NumberPickerButton'
import { parseValue, type Value } from './utils'

import '../../styles/controls/NumberPicker.scss'

type NumberPickerProps = TBaseProps & TBaseInputProps<Value> & {
    max?: number
    min?: number
    onKeyDown?(evt: KeyboardEvent<HTMLInputElement>): void,
    step?: number,
    value: Value
}

export const NumberPicker = ({
    className,
    style = EMPTY_OBJECT,
    visible = true,
    value = null,
    max = 100,
    min = 0,
    step = 1,
    disabled = false,
    onChange = NOOP_FUNCTION,
    onKeyDown = NOOP_FUNCTION,
}: NumberPickerProps) => {
    const [isFocused, setFocus] = useState(false)
    const isDecreaseBtnDisabled = disabled || (!isNull(value) && min >= Number(value))
    const isIncreaseBtnDisabled = disabled || (!isNull(value) && max <= Number(value))

    // Не ставим крайние значения, пока поле в фокусе
    const parsedValue = isFocused ? value : parseValue(value, min, max)

    const handlerChange = (step: number) => {
        onChange?.(parseValue(Number(value) + step, min, max))
    }

    const increaseBtnClick = () => handlerChange(-step)
    const decreaseBtnClick = () => handlerChange(step)

    const onBlur = () => {
        onChange?.(parseValue(value, min, max))
        setFocus(false)
    }

    /*
     * Выставляем дефолтное значение
     * TODO по идее только для совместимости, договориться чтобы дефолтное значение приходило всегда
     *  вызывает лишний рендер
     */
    useEffect(() => {
        if (parsedValue !== value) {
            onChange?.(parsedValue)
        }
    }, [parsedValue, value, onChange])

    return (
        visible && (
            <div className={classNames('n2o-number-picker', className)} style={style}>
                <NumberPickerButton
                    disabled={isDecreaseBtnDisabled}
                    onClick={increaseBtnClick}
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
                    onKeyDown={onKeyDown}
                    controlButtons={false}
                    disabled={disabled}
                    onBlur={onBlur}
                    onFocus={() => setFocus(true)}
                />
                <NumberPickerButton
                    disabled={isIncreaseBtnDisabled}
                    onClick={decreaseBtnClick}
                >
                    <i className="fa fa-plus" aria-hidden="true" />
                </NumberPickerButton>
            </div>
        )
    )
}
