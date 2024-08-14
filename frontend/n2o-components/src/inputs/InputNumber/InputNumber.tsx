import React, { KeyboardEvent } from 'react'
import classNames from 'classnames'
import toNumber from 'lodash/toNumber'
import toString from 'lodash/toString'
import isNil from 'lodash/isNil'
import isNaN from 'lodash/isNaN'
import isEqual from 'lodash/isEqual'

import '../../styles/controls/InputNumber.scss'

import { withRightPlaceholder } from '../../helpers/withRightPlaceholder'
import { Input } from '../Input'

import {
    formatToFloat,
    isValid,
    matchesWhiteList,
    getPrecision, prepareValue,
} from './utils'
import { InputMode, InputNumberProps, InputNumberState, TInputNumberValue } from './types'

export class InputNumberComponent extends React.Component<InputNumberProps, InputNumberState> {
    stepPrecition: number

    input: HTMLDivElement | null = null

    constructor(props: InputNumberProps) {
        super(props)
        const { value, step = '0.1' } = props
        const isValidValue = !isNil(value) && !isNaN(toNumber(value)) && value !== ''

        this.stepPrecition = getPrecision(step, value)
        this.state = {
            value: this.resolveValue((isValidValue ? toNumber(value) : null)),
        }
    }

    componentDidUpdate(prevProps: InputNumberProps) {
        const { value } = this.props

        if (isNaN(value)) {
            return
        }

        if (prevProps.value !== value && !isNil(value)) {
            this.setState({ value: this.resolveValue(value) })
        } else if (
            !isEqual(prevProps.value, value) &&
            (value === '' || isNil(value))
        ) {
            this.setState({ value: null })
        }
    }

    resolveValue = (value: TInputNumberValue) => {
        const { precision } = this.props

        const ceilValue = Math.trunc(Number(value))
        const isFloat = Number(value) % 1 !== 0

        if (value === null || value === '' || isNaN(toNumber(value))) { return value }

        if (precision === undefined) { return ceilValue }

        if (precision === null) { return value }

        return isFloat
            ? value
                .toString()
                .substr(0, ceilValue.toString().length + 1 + precision)
            : value
    }

    onChange = (value: TInputNumberValue) => {
        const num = prepareValue(value)

        const parsedValue = this.resolveValue(num)
        const { onChange, mode } = this.props

        if (isNil(parsedValue)) {
            this.setState({ value: null }, () => onChange?.(null))
        }

        if (matchesWhiteList(parsedValue)) {
            this.setState({ value: this.resolveValue(value) }, () => {
                const isPicker = mode === InputMode.PICKER

                if (!isNaN(toNumber(value)) || isPicker) {
                    if (isPicker) {
                        onChange?.(this.resolveValue(value))
                    } else {
                        onChange?.(Number(this.resolveValue(value)))
                    }
                }
            })
        }
    }

    buttonHandler(type: string) {
        const { min = 0, max = 100, step = 0.1, onChange, onBlur } = this.props
        const { value } = this.state
        const delta = toNumber(formatToFloat(step, this.stepPrecition))
        const val = !isNil(value) && value !== ''
            ? toNumber(value).toFixed(this.stepPrecition)
            : null
        const currentValue = toNumber(formatToFloat(val, this.stepPrecition))
        let newValue: TInputNumberValue = currentValue

        if (value === null || value === '') {
            let closestToZero = 0

            if (max < 0) { closestToZero = max } else if (min > 0) { closestToZero = min }

            this.setState({ value: closestToZero }, () => onChange?.(closestToZero))
        }

        if (type === 'up') {
            newValue = currentValue + delta
        }

        if (type === 'down') {
            newValue = currentValue - delta
        }

        if (isValid(newValue, min, max)) {
            newValue = newValue.toFixed(this.stepPrecition)

            this.setState({ value: newValue }, () => {
                onChange?.(Number(newValue))
                onBlur?.(Number(newValue))
            })
        }
    }

    onBlur = () => {
        const { max = 100, min = 0, onBlur, onChange, mode = InputMode.DEFAULT } = this.props
        const { value: stateValue } = this.state

        if (stateValue === '-' && mode !== InputMode.PICKER) {
            return
        }

        const value = this.resolveValue(formatToFloat(stateValue))

        if (!isNil(value) && isValid(value, min, max)) {
            this.setState({ value }, () => onBlur?.(value))
        } else {
            this.setState({ value: null }, () => {
                onBlur?.(null)
                onChange?.(null)
            })
        }
    }

    onKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
        const upKeyCode = 38
        const downKeyCode = 40
        let type

        const { onKeyDown } = this.props

        if (onKeyDown) {
            onKeyDown(e)
        }

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

    render() {
        const {
            visible = true,
            disabled = false,
            name,
            showButtons = true,
            className,
            onFocus,
            autoFocus = false,
            placeholder,
            max,
            min,
            step,
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
                        className={classNames('form-control', className)}
                        onBlur={this.onBlur}
                        onFocus={onFocus}
                        onChange={({ target }) => this.onChange(target.value)}
                        disabled={disabled}
                        autoFocus={autoFocus}
                        placeholder={placeholder}
                        min={min}
                        max={max}
                        step={step}
                    />
                    {showButtons && (
                        <div className="n2o-input-number-buttons">
                            <button
                                aria-label="button-up"
                                type="button"
                                onClick={() => this.buttonHandler('up')}
                                disabled={disabled}
                                tabIndex={-1}
                            >
                                <i className="fa fa-angle-up" aria-hidden="true" />
                            </button>
                            <button
                                aria-label="button-down"
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

export const InputNumber = withRightPlaceholder(InputNumberComponent)
