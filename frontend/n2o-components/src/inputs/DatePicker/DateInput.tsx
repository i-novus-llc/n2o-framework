import React, {
    ChangeEventHandler,
    FocusEventHandler,
    KeyboardEvent,
    KeyboardEventHandler,
    MouseEventHandler,
} from 'react'
import dayjs from 'dayjs'
import cx from 'classnames'
import isNaN from 'lodash/isNaN'
import omit from 'lodash/omit'
import MaskedInput from 'react-text-mask'

import { formatToMask, getDeletedSymbol, hasInsideMixMax, replaceAt } from './utils'
import { type DateInputProps, DateTimeControlName } from './types'

type DateInputState = {
    value: string | undefined,
}

const inputStyle = { flexGrow: 1 }
const dashStyle = { alignSelf: 'center' }

export class DateInput extends React.Component<DateInputProps, DateInputState> {
    private inputRef: MaskedInput | undefined

    constructor(props: DateInputProps) {
        super(props)
        const { value, dateFormat } = props

        this.state = {
            value: value?.format(dateFormat),
        }
    }

    setInputRef = (input: MaskedInput) => {
        this.inputRef = input
        const { setControlRef } = this.props

        setControlRef(input)
    }

    updateValue = (value: string, callback?: () => void) => {
        const { onInputChange, name, dateFormat, max = '', min = '' } = this.props

        if (value === '') {
            onInputChange?.(null, name)

            return
        }

        const parsedValue = dayjs(value, dateFormat)
        const isValidValue = parsedValue.format(dateFormat) === value &&
            hasInsideMixMax(value, { max, min }, dateFormat)

        if (isValidValue) {
            onInputChange?.(parsedValue, name)

            return
        }

        this.setState({ value }, () => {
            if (callback) {
                callback()
            }
        })
    }

    onChange: ChangeEventHandler<HTMLInputElement> = (e) => {
        // TODO: Проверить метод
        const { value } = e.target

        this.updateValue(value)
    }

    onFocus: FocusEventHandler<HTMLInputElement> = (e) => {
        const { setVisibility, onFocus, openOnFocus } = this.props

        if (onFocus) {
            onFocus(e)
        }
        if (openOnFocus) {
            setVisibility(true)
        }
    }

    onBlur: FocusEventHandler<HTMLInputElement> = (e) => {
        const { value } = e.target as HTMLInputElement
        const { name, outputFormat, onBlur } = this.props

        if (value === '') {
            onBlur?.(null, name)

            return
        }
        if (dayjs(value).format(outputFormat) === value) {
            onBlur?.(dayjs(value), name)
        }
    }

    /**
     * Показывается попап при нажатии на кнопку с иконкой календаря
     */
    onButtonClick = () => {
        const { setVisibility } = this.props

        setVisibility(true)
    }

    onInputClick: MouseEventHandler<HTMLInputElement> = (event) => {
        const { setVisibility, onClick } = this.props

        setVisibility(true)
        if (onClick) {
            onClick(event)
        }
    }

    setCursorPosition(cursorPosition: number) {
        const input = this.inputRef?.inputElement as HTMLInputElement

        input.setSelectionRange(cursorPosition, cursorPosition)
    }

    onKeyDown: KeyboardEventHandler<HTMLInputElement> = (e) => {
        const { value = '' } = this.state
        const target = e.target as HTMLInputElement
        const cursorPos = Number(target.selectionStart)
        const keyCode = Number(e.keyCode)
        const deletedChar = Number(getDeletedSymbol(value, cursorPos)) || 0

        const { onKeyDown } = this.props

        if (onKeyDown) {
            onKeyDown(e)
        }

        if (keyCode === 8 && cursorPos !== 0 && !isNaN(deletedChar)) {
            e.preventDefault()

            const newValue = replaceAt(value, cursorPos, '_')

            this.updateValue(newValue, () => this.setCursorPosition(cursorPos - 1))
        }
    }

    render() {
        const {
            disabled,
            placeholder,
            name,
            autoFocus,
            dateFormat,
            inputClassName,
        } = this.props
        const { value } = this.state

        return (
            <div
                className={cx('n2o-date-input', {
                    'n2o-date-input-first': name === DateTimeControlName.BEGIN,
                    'n2o-date-input-last': name === DateTimeControlName.END,
                })}
            >
                {name === DateTimeControlName.END && (
                    <span style={dashStyle}>-</span>
                )}
                <MaskedInput
                    ref={this.setInputRef}
                    onKeyDown={this.onKeyDown}
                    value={value}
                    type="text"
                    mask={formatToMask(dateFormat)}
                    className={cx('form-control', inputClassName)}
                    placeholder={placeholder}
                    disabled={disabled}
                    onChange={this.onChange}
                    onClick={this.onInputClick}
                    keepCharPositions
                    style={inputStyle}
                    onFocus={this.onFocus}
                    onBlur={this.onBlur}
                    autoFocus={autoFocus}
                    render={(ref, props) => <input ref={(input: HTMLInputElement) => ref(input)} {...omit(props, ['defaultValue'])} />}
                />
                {(name === DateTimeControlName.DEFAULT_NAME ||
                    name === DateTimeControlName.END) && (
                        <button
                            type="button"
                            aria-label="calendar-button"
                            disabled={disabled}
                            onClick={this.onButtonClick}
                            className="btn n2o-calendar-button"
                            tabIndex={-1}
                        >
                            <i className="fa fa-calendar" aria-hidden="true" />
                        </button>
                )}
            </div>
        )
    }

    // eslint-disable-next-line react/no-deprecated
    componentWillReceiveProps(props: DateInputProps) {
        const { value, dateFormat } = props

        if (props.value) {
            this.setState({ value: value?.format(dateFormat) })
        } else {
            this.setState({ value: '' })
        }
    }
}
