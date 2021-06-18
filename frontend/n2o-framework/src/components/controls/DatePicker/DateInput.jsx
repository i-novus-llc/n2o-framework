import React from 'react'
import PropTypes from 'prop-types'
import moment from 'moment'
import cx from 'classnames'
import isNaN from 'lodash/isNaN'
import omit from 'lodash/omit'
import isObject from 'lodash/isObject'
import get from 'lodash/get'
import MaskedInput from 'react-text-mask'

// eslint-disable-next-line import/no-cycle
import DateTimeControl from './DateTimeControl'
import { formatToMask, hasInsideMixMax } from './utils'

const inputStyle = { flexGrow: 1 }
const dashStyle = { alignSelf: 'center' }

/**
 * Компонент DateInput
 * @reactProps {string} dateFormat
 * @reactProps {string} defaultTime
 * @reactProps {string} placeholder
 * @reactProps {boolean} disabled
 * @reactProps {moment} value
 * @reactProps {function} inputOnClick
 * @reactProps {string} inputClassName
 * @reactProps {string} name
 * @reactProps {function} onClick
 */
class DateInput extends React.Component {
    constructor(props) {
        super(props)
        const { value, dateFormat } = props

        this.state = {
            value: value && value.format(dateFormat),
        }
        this.onChange = this.onChange.bind(this)
        this.onFocus = this.onFocus.bind(this)
        this.onBlur = this.onBlur.bind(this)
        this.onButtonClick = this.onButtonClick.bind(this)
        this.onInputClick = this.onInputClick.bind(this)
        this.onKeyDown = this.onKeyDown.bind(this)
        this.setInputRef = this.setInputRef.bind(this)
        this.getDeletedSymbol = this.getDeletedSymbol.bind(this)
    }

    setInputRef(input) {
        this.inputRef = input
        const { setControlRef } = this.props

        setControlRef(input)
    }

    onChange(e, callback) {
        const value = isObject(e) ? get(e, 'target.value', '') : e
        const { dateFormat, name, onInputChange } = this.props

        if (value === '') {
            onInputChange(null, name)
        } else if (
            moment(value, dateFormat).format(dateFormat) === value &&
            hasInsideMixMax(value, this.props, dateFormat)
        ) {
            onInputChange(moment(value, dateFormat), name)
        } else {
            this.setState({ value }, () => {
                if (callback) { callback() }
            })
        }
    }

    onFocus(e) {
        const { setVisibility, onFocus, openOnFocus } = this.props

        if (onFocus) {
            onFocus(e)
        }
        if (openOnFocus) {
            setVisibility(true)
        }
    }

    onBlur(e) {
        const { value } = e.target
        const { name, outputFormat, onBlur } = this.props

        if (value === '') {
            onBlur(null, name)
        } else if (moment(value).format(outputFormat) === value) {
            onBlur(moment(value), name)
        }
    }

    /**
     * Показывается попап при нажатии на кнопку с иконкой календаря
     */
    onButtonClick() {
        const { setVisibility } = this.props

        setVisibility(true)
    }

    onInputClick(event) {
        const { setVisibility, onClick } = this.props

        setVisibility(true)
        if (onClick) {
            onClick(event)
        }
    }

    replaceAt = (string, index, replacement) => (
        string.substring(0, index - 1) +
            replacement +
            string.substring(index, string.length)
    )

    setCursorPosition(cursorPosition) {
        this.inputRef.inputElement.setSelectionRange(cursorPosition, cursorPosition)
    }

    getDeletedSymbol(index) {
        const { value } = this.state

        return value.substring(index - 1, index)
    }

    onKeyDown(e) {
        const cursorPos = Number(e.target.selectionStart)
        const keyCode = Number(e.keyCode)
        const deletedChar = +this.getDeletedSymbol(cursorPos)

        if (keyCode === 8 && cursorPos !== 0 && !isNaN(deletedChar)) {
            e.preventDefault()

            const { value } = this.state
            const newValue = this.replaceAt(value, cursorPos, '_')

            this.onChange(newValue, () => this.setCursorPosition(cursorPos - 1))
        }
    }

    /**
     * Базовый рендер
     */
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
                    'n2o-date-input-first': name === DateTimeControl.beginInputName,
                    'n2o-date-input-last': name === DateTimeControl.endInputName,
                })}
            >
                {name === DateTimeControl.endInputName && (
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
                    render={(ref, props) => <input ref={ref} {...omit(props, ['defaultValue'])} />}
                />
                {(name === DateTimeControl.defaultInputName ||
                    name === DateTimeControl.endInputName) && (
                    // eslint-disable-next-line react/button-has-type
                    <button
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

    componentWillReceiveProps(props) {
        const { value, dateFormat } = props

        if (props.value) {
            this.setState({ value: value.format(dateFormat) })
        } else {
            this.setState({ value: '' })
        }
    }
}

DateInput.defaultProps = {
    value: moment(),
    dateFormat: 'DD/MM/YYYY',
    autoFocus: false,
    onFocus: () => {},
    onBlur: () => {},
    openOnFocus: false,
}

DateInput.propTypes = {
    outputFormat: PropTypes.string,
    setVisibility: PropTypes.func,
    setControlRef: PropTypes.func,
    onInputChange: PropTypes.func,
    onFocus: PropTypes.func,
    onBlur: PropTypes.func,
    dateFormat: PropTypes.string,
    defaultTime: PropTypes.string,
    placeholder: PropTypes.oneOfType([PropTypes.string, PropTypes.bool]),
    disabled: PropTypes.bool,
    value: PropTypes.instanceOf(moment),
    inputOnClick: PropTypes.func,
    inputClassName: PropTypes.string,
    name: PropTypes.string,
    onClick: PropTypes.func,
    autoFocus: PropTypes.bool,
    openOnFocus: PropTypes.bool,
    min: PropTypes.oneOfType([
        PropTypes.instanceOf(moment),
        PropTypes.instanceOf(Date),
        PropTypes.string,
    ]),
    max: PropTypes.oneOfType([
        PropTypes.instanceOf(moment),
        PropTypes.instanceOf(Date),
        PropTypes.string,
    ]),
}
export default DateInput
