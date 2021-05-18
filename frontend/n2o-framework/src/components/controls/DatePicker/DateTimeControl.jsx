import React from 'react'
import { findDOMNode } from 'react-dom'
import pick from 'lodash/pick'
import every from 'lodash/every'
import isUndefined from 'lodash/isUndefined'
import isFunction from 'lodash/isFunction'
import isNull from 'lodash/isNull'
import PropTypes from 'prop-types'
import moment from 'moment'
import { Manager, Reference, Popper } from 'react-popper'

import {
    mapToValue,
    mapToDefaultTime,
    buildDateFormat,
    MODIFIERS,
} from './utils'
// eslint-disable-next-line import/no-cycle
import DateInputGroup from './DateInputGroup'
// eslint-disable-next-line import/no-cycle
import PopUp from './PopUp'

export const ControlType = {
    DATE_PICKER: 'date-picker',
    DATE_INTERVAL: 'date-interval',
}

/**
 * Компонент DateTimeControl
 * @reactProps {string} type
 * @reactProps {string} defaultTime
 * @reactProps {moment|date|string} value
 * @reactProps {moment|date|string} min
 * @reactProps {moment|date|string} max
 * @reactProps {string} dateDivider
 * @reactProps {function} onChange
 * @reactProps {string} dateFormat
 * @reactProps {string} timeFormat
 * @reactProps {string} outputFormat
 * @reactProps {boolean} disabled
 * @reactProps {string} placeholder
 * @reactProps {string} locale
 * @reactProps {string} timeFormat
 * @reactProps {string} openOnFocus
 */
class DateTimeControl extends React.Component {
    constructor(props) {
        super(props)
        const { value, dateFormat, configLocale, timeFormat, dateDivider, outputFormat } = props

        this.format = buildDateFormat(dateFormat, timeFormat, dateDivider)

        this.defaultTime = mapToDefaultTime(
            value,
            props.defaultTime,
            DateTimeControl.defaultInputName,
            timeFormat,
            outputFormat,
        )

        const { defaultTime } = this

        this.state = {
            inputs: mapToValue(
                value,
                defaultTime,
                outputFormat,
                configLocale,
                DateTimeControl.defaultInputName,
            ),
            isPopUpVisible: false,
            isTimeSet: {},
            focused: false,
        }

        // eslint-disable-next-line no-underscore-dangle
        this._control = null

        this.select = this.select.bind(this)
        this.onInputChange = this.onInputChange.bind(this)
        this.onChange = this.onChange.bind(this)
        this.setVisibility = this.setVisibility.bind(this)
        this.setPlacement = this.setPlacement.bind(this)
        this.onClickOutside = this.onClickOutside.bind(this)
        this.markTimeAsSet = this.markTimeAsSet.bind(this)
        this.setInputRef = this.setInputRef.bind(this)
        this.onFocus = this.onFocus.bind(this)
        this.getValue = this.getValue.bind(this)
        this.onInputBlur = this.onInputBlur.bind(this)
        this.setControlRef = this.setControlRef.bind(this)
    }

    /**
     * Обработка новых пропсов
     */
    componentWillReceiveProps(props) {
        const { value, dateFormat, configLocale, timeFormat, dateDivider, outputFormat } = props

        this.format = buildDateFormat(dateFormat, timeFormat, dateDivider)

        this.defaultTime = mapToDefaultTime(
            value,
            props.defaultTime,
            DateTimeControl.defaultInputName,
            timeFormat,
            outputFormat,
        )

        this.setState({
            inputs: mapToValue(
                value,
                this.defaultTime,
                outputFormat,
                configLocale,
                DateTimeControl.defaultInputName,
            ),
        })
    }

    markTimeAsSet(inputName) {
        const { isTimeSet } = this.state

        this.setState({
            isTimeSet: { ...isTimeSet, [inputName]: true },
        })
    }

    /**
     * Приведение к строке
     */
    dateToString(date) {
        const { outputFormat, utc } = this.props

        if (date instanceof Date) {
            return date.toString()
        } if (date instanceof moment) {
            return utc
                ? moment.utc(date).format(outputFormat)
                : date.format(outputFormat)
        }

        return date
    }

    getValue(inputName) {
        const { inputs } = this.state

        if (inputName === DateTimeControl.defaultInputName) {
            return this.dateToString(inputs[inputName])
        }

        return [
            this.dateToString(inputs[DateTimeControl.beginInputName]),
            this.dateToString(inputs[DateTimeControl.endInputName]),
        ]
    }

    /**
     * вызов onChange
     */
    onChange(inputName) {
        const { onChange } = this.props
        const value = this.getValue(inputName)

        onChange(value)
    }

    /**
     * Выбор даты, прокидывается в календарь
     */
    select(day, inputName, close = true) {
        const { inputs } = this.state
        const { type } = this.props

        if (
            inputName === DateTimeControl.defaultInputName ||
      inputName === DateTimeControl.beginInputName ||
      (inputName === DateTimeControl.endInputName &&
        !inputs[DateTimeControl.beginInputName]) ||
      (inputName === DateTimeControl.endInputName &&
        moment(day).isSameOrAfter(inputs[DateTimeControl.beginInputName]))
        ) {
            const inputValue = () => {
                if (
                    inputName === DateTimeControl.beginInputName &&
                    inputs[DateTimeControl.endInputName] &&
                    moment(day).isAfter(inputs[DateTimeControl.endInputName])
                ) {
                    return {
                        [inputName]: day,
                        [DateTimeControl.endInputName]: null,
                    }
                }

                return {
                    [inputName]: day,
                }
            }
            const { inputs } = this.state

            this.setState(
                {
                    inputs: { ...inputs, ...inputValue() },
                    isPopUpVisible:
            inputName === DateTimeControl.beginInputName ||
            inputName === DateTimeControl.endInputName ||
            !close,
                },
                () => {
                    this.onChange(inputName)
                    if (type === ControlType.DATE_PICKER) {
                        const { onBlur } = this.props

                        onBlur(this.getValue(inputName))
                    }
                },
            )
        }
    }

    /**
     * Выбор даты, прокидывается в инпут
     * @todo объеденить методы select и onInputChange в 1 метод
     */
    onInputChange(date, inputName, callback = null) {
        const { inputs } = this.state

        this.setState(
            {
                inputs: { ...inputs, [inputName]: date },
            },
            () => (isFunction(callback) ? callback() : this.onChange(inputName)),
        )
    }

    onInputBlur(date, inputName) {
        const { onBlur } = this.props

        this.onInputChange(date, inputName, () => onBlur(this.getValue(inputName)))
    }

    /**
     * Устанавливает видимость попапа
     */
    setVisibility(visible) {
        this.setState({
            isPopUpVisible: visible,
            focused: visible,
        })
    }

    /**
     * Устанавливает положение попапа
     */
    setPlacement(placement) {
        this.setState({ placement })
    }

    /**
     * Навешивание листенеров для появления / исчезания попапа
     */
    componentWillMount() {
        if (typeof window !== 'undefined') {
            document.addEventListener('mousedown', this.onClickOutside.bind(this))
            document.addEventListener('touchstart', this.onClickOutside.bind(this))
        }
    }

    /**
     * Удаление листенеров для появления / исчезания попапа после анмаунта
     */
    componentWillUnmount() {
        if (typeof window !== 'undefined') {
            document.removeEventListener('mousedown', this.onClickOutside.bind(this))
            document.removeEventListener(
                'touchstart',
                this.onClickOutside.bind(this),
            )
        }
    }

    componentDidUpdate() {
        const { inputs } = this.state
        const { begin, end } = inputs

        if (!isNull(begin) && moment(begin).isAfter(moment(end))) {
            this.setState({
                inputs: { ...inputs, end: null },
            })
        }
    }

    /**
     * Обработка клика за пределами попапа
     */
    onClickOutside(e) {
        const { type, onBlur } = this.props
        const { focused, isPopUpVisible } = this.state

        if (isPopUpVisible) {
            // eslint-disable-next-line react/no-find-dom-node
            const datePicker = findDOMNode(this.datePicker)
            // eslint-disable-next-line react/no-find-dom-node
            const dateInput = findDOMNode(this.inputGroup)

            if (!datePicker) { return }
            if (
                e.target.className.includes('n2o-pop-up') ||
        (!datePicker.contains(e.target) && !dateInput.contains(e.target))
            ) {
                if (focused) {
                    if (type === 'date-interval') {
                        const valueToBlur = this.getValue()

                        if (every(valueToBlur, value => value)) {
                            onBlur(valueToBlur)
                        }
                    } else {
                        onBlur(this.getValue(DateTimeControl.defaultInputName))
                    }
                }
                this.setVisibility(false)
            }
        }
    }

    /**
     * Приводит min, max к moment оъекту, текущему формату
     */

    parseRange(range) {
        const { dateFormat } = this.props

        // eslint-disable-next-line no-underscore-dangle
        return !isUndefined(moment(range)._f)
            ? moment(range)
            : moment(range, dateFormat)
    }

    /**
     * Рендер попапа
     */
    renderPopUp() {
        const { max, min, configLocale, timeFormat, dateFormat, type, date } = this.props
        const { inputs, isPopUpVisible, placement, isTimeSet } = this.state

        return (
            isPopUpVisible && (
                <PopUp
                    dateFormat={dateFormat}
                    time={this.defaultTime}
                    type={type}
                    isTimeSet={isTimeSet}
                    markTimeAsSet={this.markTimeAsSet}
                    timeFormat={timeFormat}
                    inputGroup={this.inputGroup}
                    ref={(popUp) => {
                        this.popUp = popUp

                        return popUp
                    }}
                    placement={placement}
                    value={inputs}
                    select={this.select}
                    setPlacement={this.setPlacement}
                    setVisibility={this.setVisibility}
                    max={this.parseRange(max)}
                    min={this.parseRange(min)}
                    date={date}
                    locale={configLocale}
                />
            )
        )
    }

    onFocus(e) {
        const { onFocus } = this.props

        this.setState(
            {
                focused: true,
                isPopUpVisible: true,
            },
            () => onFocus(e),
        )
    }

    setInputRef(poperRef) {
        return (r) => {
            this.inputGroup = r
            poperRef(r)
        }
    }

    setControlRef(el) {
        // eslint-disable-next-line no-underscore-dangle
        this._control = el
    }

    /**
     * Базовый рендер
     */
    render() {
        const {
            disabled,
            placeholder,
            className,
            autoFocus,
            openOnFocus,
            popupPlacement,
            outputFormat,
            strategy,
        } = this.props

        const { inputs, isPopUpVisible } = this.state
        const dateInputGroupProps = pick(this.props, ['max', 'min'])

        return (
            <div className="n2o-date-picker-container">
                <div
                    className="n2o-date-picker"
                    ref={(picker) => {
                        this.datePicker = picker

                        return picker
                    }}
                >
                    <Manager>
                        <Reference>
                            {({ ref }) => (
                                <DateInputGroup
                                    setControlRef={this.setControlRef}
                                    inputRef={this.setInputRef(ref)}
                                    dateFormat={this.format}
                                    disabled={disabled}
                                    placeholder={placeholder}
                                    value={inputs}
                                    onInputChange={this.onInputChange}
                                    inputClassName={className}
                                    setVisibility={this.setVisibility}
                                    setWidth={this.setWidth}
                                    onFocus={this.onFocus}
                                    onBlur={this.onInputBlur}
                                    autoFocus={autoFocus}
                                    openOnFocus={openOnFocus}
                                    outputFormat={outputFormat}
                                    {...dateInputGroupProps}
                                />
                            )}
                        </Reference>
                        {isPopUpVisible && (
                            <Popper
                                placement={popupPlacement}
                                modifiers={MODIFIERS}
                                strategy={strategy || 'absolute'}
                            >
                                {({ ref, style, placement }) => (
                                    <div
                                        ref={ref}
                                        style={style}
                                        data-placement={placement}
                                        className="n2o-pop-up"
                                    >
                                        {this.renderPopUp(this.width)}
                                    </div>
                                )}
                            </Popper>
                        )}
                    </Manager>
                </div>
            </div>
        )
    }
}

DateTimeControl.defaultInputName = 'singleDate'
DateTimeControl.beginInputName = 'begin'
DateTimeControl.endInputName = 'end'

DateTimeControl.defaultProps = {
    onFocus: () => {},
    onBlur: () => {},
    onChange: () => {},
    dateDivider: ' ',
    dateFormat: 'DD.MM.YYYY',
    outputFormat: 'DD.MM.YYYY HH:mm:ss',
    configLocale: 'ru',
    autoFocus: false,
    openOnFocus: false,
    popupPlacement: 'bottom-start',
}

DateTimeControl.propTypes = {
    onFocus: PropTypes.func,
    onBlur: PropTypes.func,
    type: PropTypes.oneOf([ControlType.DATE_INTERVAL, ControlType.DATE_PICKER]),
    defaultTime: PropTypes.string,
    value: PropTypes.oneOfType([
        PropTypes.instanceOf(moment),
        PropTypes.instanceOf(Date),
        PropTypes.string,
        PropTypes.array,
    ]),
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
    dateDivider: PropTypes.string,
    onChange: PropTypes.func,
    dateFormat: PropTypes.string,
    timeFromat: PropTypes.string,
    outputFormat: PropTypes.string,
    disabled: PropTypes.bool,
    placeholder: PropTypes.string,
    configLocale: PropTypes.oneOf(['en', 'ru']),
    timeFormat: PropTypes.string,
    autoFocus: PropTypes.bool,
    openOnFocus: PropTypes.bool,
    utc: PropTypes.bool,
    className: PropTypes.string,
    date: PropTypes.any,
    popupPlacement: PropTypes.string,
    strategy: PropTypes.string,
}

export default DateTimeControl
