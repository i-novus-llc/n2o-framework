import React, { ChangeEvent, KeyboardEvent } from 'react'
import { useMaskito } from '@maskito/react'
import { maskitoNumberOptionsGenerator } from '@maskito/kit'
import classNames from 'classnames'
import { maskitoTransform } from '@maskito/core'

import { withRightPlaceholder } from '../helpers/withRightPlaceholder'

import { type CommonMaskedInputProps } from './types'

export interface InputNumberProps extends Omit<CommonMaskedInputProps, 'onChange' | 'onBlur'> {
    min: number
    max: number
    precision?: number
    step: number
    visible?: boolean
    controlButtons?: boolean
    onKeyDown?(event: KeyboardEvent<HTMLInputElement>): void
    onFocus?(): void
    onChange?(value: number | null): void
    onBlur?(value: number | null): void
}

export enum DIRECTION_STEP {
    INCREASE = 'increase',
    DECREASE = 'decrease',
}

export enum KEY {
    UP = 'ArrowUp',
    DOWN = 'ArrowDown',
}

const MINUS_SIGN = '-'

function Component({
    className,
    onChange,
    onBlur,
    onKeyDown,
    onFocus,
    min,
    max,
    step: propsStep,
    value: defaultValue,
    precision: maximumFractionDigits,
    disabled = false,
    controlButtons = true,
    visible = true,
}: InputNumberProps) {
    // TODO @INFO Backend присылает type of step = string, но в NumberPicker number type
    const step = Number(propsStep)

    const options = maskitoNumberOptionsGenerator({
        minusSign: MINUS_SIGN,
        thousandSeparator: '',
        min,
        max,
        maximumFractionDigits,
    })
    const maskRef = useMaskito({ options })
    const value = (defaultValue || defaultValue === 0) ? maskitoTransform(String(defaultValue), options) : ''

    if (!visible) { return null }

    const keepWithinRange = (value: number) => {
        if (value < min) {
            return min
        }

        if (value > max) {
            return max
        }

        return value
    }

    const handleStepChange = (direction: DIRECTION_STEP) => {
        let numberValue: number

        if (value) {
            const currentValue = parseFloat(value)

            numberValue = direction === DIRECTION_STEP.INCREASE
                ? parseFloat((currentValue + step).toFixed(maximumFractionDigits))
                : parseFloat((currentValue - step).toFixed(maximumFractionDigits))
        } else {
            numberValue = direction === DIRECTION_STEP.INCREASE ? step : 0 - step
        }

        const inputValue = keepWithinRange(numberValue)

        onChange?.(inputValue)
    }

    const increase = () => handleStepChange(DIRECTION_STEP.INCREASE)
    const decrease = () => handleStepChange(DIRECTION_STEP.DECREASE)

    const handleKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
        if (e.key === KEY.UP) { increase() }
        if (e.key === KEY.DOWN) { decrease() }

        onKeyDown?.(e)
    }

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { value: maskedValue } = e.target

        if (maskedValue === MINUS_SIGN) { return }

        const numberValue = maskedValue ? Number(maskedValue) : null

        onChange?.(numberValue)
    }

    const handleBlur = (e: ChangeEvent<HTMLInputElement>) => {
        const { value: maskedValue } = e.target

        if (maskedValue === MINUS_SIGN) {
            onBlur?.(min)

            return
        }

        const numberValue = maskedValue ? Number(maskedValue) : null

        onBlur?.(numberValue)
    }

    return (
        <div className="n2o-input-number">
            <input
                value={value}
                ref={maskRef}
                className={classNames('n2o-input', 'form-control', className)}
                onKeyDown={handleKeyDown}
                onInput={handleChange}
                onBlur={handleBlur}
                onFocus={onFocus}
                disabled={disabled}
                step={step}
                min={min}
                max={max}
            />
            {controlButtons && (
                <div className="n2o-input-number-buttons">
                    <button
                        aria-label="button-up"
                        type="button"
                        onClick={increase}
                        tabIndex={-1}
                    >
                        <i className="fa fa-angle-up" aria-hidden="true" />
                    </button>
                    <button
                        aria-label="button-down"
                        type="button"
                        onClick={decrease}
                        tabIndex={-1}
                    >
                        <i className="fa fa-angle-down" aria-hidden="true" />
                    </button>
                </div>
            )}
        </div>
    )
}

export const InputNumber = withRightPlaceholder<InputNumberProps>(Component)
