import React, { ElementType, FocusEventHandler } from 'react'
import { findDOMNode } from 'react-dom'
import every from 'lodash/every'
import isFunction from 'lodash/isFunction'
import isNull from 'lodash/isNull'
import dayjs, { Dayjs } from 'dayjs'
import { Manager, Popper, Reference, RefHandler } from 'react-popper'

import { buildDateFormat, createDefaultTime, mapToDefaultTime, mapToValue } from './utils'
import { DateInputGroup } from './DateInputGroup'
import { PopUp } from './PopUp'
import {
    DateTimeControlName,
    DateType,
    OnInputChangeHandler,
    DefaultTime,
    DateTimeControlProps,
    ControlType,
} from './types'

import '../../styles/components/DatePicker.scss'

type DateTimeControlState = {
    inputs: Record<string, Dayjs | null>,
    isFocus: boolean,
    isPopUpVisible: boolean,
    isTimeSet: Record<string, boolean | undefined>
}

const DEFAULT_DATE_FORMAT = 'DD.MM.YYYY'
const DEFAULT_OUTPUT_FORMAT = `${DEFAULT_DATE_FORMAT}  HH:mm:ss`

export class DateTimeControl extends React.Component<DateTimeControlProps, DateTimeControlState> {
    format: string

    defaultTime: DefaultTime

    datePicker: HTMLDivElement | null = null

    inputGroup: HTMLDivElement | null = null

    private control: null | ElementType = null

    constructor(props: DateTimeControlProps) {
        super(props)
        const {
            value,
            dateFormat = DEFAULT_DATE_FORMAT,
            configLocale = 'ru',
            timeFormat,
            dateDivider = ' ',
            outputFormat = DEFAULT_OUTPUT_FORMAT,
            defaultTime = createDefaultTime(timeFormat),
        } = props

        this.format = buildDateFormat(dateFormat, timeFormat, dateDivider)

        this.defaultTime = mapToDefaultTime(
            value,
            defaultTime,
            DateTimeControlName.DEFAULT_NAME,
            timeFormat,
        )

        this.state = {
            inputs: mapToValue(
                value,
                this.defaultTime,
                outputFormat,
                configLocale,
                DateTimeControlName.DEFAULT_NAME,
            ),
            isPopUpVisible: false,
            isTimeSet: {},
            isFocus: false,
        }

        // eslint-disable-next-line no-underscore-dangle
        this.control = null
    }

    /**
     * Обработка новых пропсов
     */
    // eslint-disable-next-line react/no-deprecated
    componentWillReceiveProps(props: DateTimeControlProps) {
        const {
            value,
            dateFormat = DEFAULT_DATE_FORMAT,
            configLocale = 'ru',
            timeFormat,
            dateDivider = ' ',
            outputFormat = DEFAULT_OUTPUT_FORMAT,
            defaultTime = createDefaultTime(timeFormat),
        } = props

        this.format = buildDateFormat(dateFormat, timeFormat, dateDivider)

        this.defaultTime = mapToDefaultTime(
            value,
            defaultTime,
            DateTimeControlName.DEFAULT_NAME,
            timeFormat,
        )

        this.setState({
            inputs: mapToValue(
                value,
                this.defaultTime,
                outputFormat,
                configLocale,
                DateTimeControlName.DEFAULT_NAME,
            ),
        })
    }

    markTimeAsSet = (inputName: string) => {
        const { isTimeSet } = this.state

        this.setState({
            isTimeSet: { ...isTimeSet, [inputName]: true },
        })
    }

    /**
     * Приведение к строке
     */
    dateToString(date: DateType | null) {
        const { outputFormat = DEFAULT_OUTPUT_FORMAT, utc } = this.props

        if (isNull(date) || typeof date === 'string') {
            return date
        }

        if (date instanceof Date) {
            return date.toString()
        }

        return utc
            ? dayjs(date).format(outputFormat)
            : date.format(outputFormat)
    }

    getValue = (inputName: string | null): [string | null, string | null] | string | null => {
        const { inputs } = this.state

        if (inputName === DateTimeControlName.DEFAULT_NAME) {
            return this.dateToString(inputs[inputName])
        }

        return [
            this.dateToString(inputs[DateTimeControlName.BEGIN]),
            this.dateToString(inputs[DateTimeControlName.END]),
        ]
    }

    /**
     * вызов onChange
     */
    onChange = (inputName: string) => {
        const { onChange } = this.props
        const value = this.getValue(inputName)

        onChange?.(value)
    }

    /**
     * Выбор даты, прокидывается в календарь
     */
    select = (day: Dayjs | null, inputName: string, close = true) => {
        const { inputs } = this.state
        const { type } = this.props

        if (
            inputName === DateTimeControlName.DEFAULT_NAME ||
            inputName === DateTimeControlName.BEGIN ||
            (inputName === DateTimeControlName.END &&
                !inputs[DateTimeControlName.BEGIN]) ||
            (inputName === DateTimeControlName.END &&
                (dayjs(day).isSame(inputs[DateTimeControlName.BEGIN]) ||
                    dayjs(day).isAfter(inputs[DateTimeControlName.BEGIN])))
        ) {
            const inputValue = () => {
                if (
                    inputName === DateTimeControlName.BEGIN &&
                    // eslint-disable-next-line @typescript-eslint/no-use-before-define
                    inputs[DateTimeControlName.END] &&
                    // eslint-disable-next-line @typescript-eslint/no-use-before-define
                    dayjs(day).isAfter(inputs[DateTimeControlName.END])
                ) {
                    return {
                        [inputName]: day,
                        [DateTimeControlName.END]: null,
                    }
                }

                return { [inputName]: day }
            }
            const { inputs } = this.state

            this.setState(
                {
                    inputs: { ...inputs, ...inputValue() },
                    isPopUpVisible:
                        inputName === DateTimeControlName.BEGIN ||
                        inputName === DateTimeControlName.END ||
                        !close,
                },
                () => {
                    this.onChange(inputName)
                    if (type === ControlType.DATE_PICKER) {
                        const { onBlur } = this.props

                        onBlur?.(this.getValue(inputName))
                    }
                },
            )
        }
    }

    /**
     * Выбор даты, прокидывается в инпут
     * @todo объединить методы select и onInputChange в 1 метод
     */
    onInputChange: OnInputChangeHandler = (date, inputName, callback?) => {
        const { inputs } = this.state

        const singleDate = inputs[DateTimeControlName.DEFAULT_NAME]

        if (date && singleDate === undefined) {
            if (inputName === DateTimeControlName.BEGIN) {
                const end = inputs[DateTimeControlName.END]

                if (date.isAfter(end)) {
                    this.setState({
                        inputs: { [DateTimeControlName.BEGIN]: date, [DateTimeControlName.END]: null },
                    }, () => this.onChange(inputName))

                    return
                }
            }

            const begin = inputs[DateTimeControlName.BEGIN]

            if (inputName === 'end' && date.isBefore(begin)) {
                this.setState({
                    inputs: { [DateTimeControlName.BEGIN]: null, [DateTimeControlName.END]: date },
                }, () => this.onChange(inputName))

                return
            }
        }

        this.setState(
            {
                inputs: { ...inputs, [inputName]: date },
            },
            () => (isFunction(callback) ? callback?.() : this.onChange(inputName)),
        )
    }

    onInputBlur = (date: Dayjs | null, inputName: string) => {
        const { onBlur } = this.props
        const { isPopUpVisible } = this.state

        if (isPopUpVisible) { return }

        this.onInputChange(date, inputName, () => onBlur?.(this.getValue(inputName)))
    }

    /**
     * Устанавливает видимость попапа
     */
    setVisibility = (visible: boolean) => {
        this.setState({
            isPopUpVisible: visible,
            isFocus: visible,
        })
    }

    /**
     * Навешивание листенеров для появления / исчезания попапа
     */
    componentDidMount() {
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
            document.removeEventListener('touchstart', this.onClickOutside.bind(this))
        }
    }

    componentDidUpdate() {
        const { inputs } = this.state
        const { begin, end } = inputs

        if (!isNull(begin) && dayjs(begin).isAfter(dayjs(end))) {
            this.setState({ inputs: { ...inputs, end: null } })
        }
    }

    /**
     * Обработка клика за пределами попапа
     */
    onClickOutside: EventListener = (e) => {
        const { isPopUpVisible } = this.state

        if (!isPopUpVisible) { return }

        // eslint-disable-next-line react/no-find-dom-node
        const datePicker = findDOMNode(this.datePicker)

        if (!datePicker) { return }

        const { type, onBlur } = this.props
        const { isFocus } = this.state
        const target = e.target as HTMLElement

        // eslint-disable-next-line react/no-find-dom-node
        const dateInput = findDOMNode(this.inputGroup)

        if (
            target.className.includes('n2o-pop-up') ||
            (!datePicker.contains(target) && !dateInput?.contains(target))
        ) {
            if (isFocus) {
                if (type === 'date-interval') {
                    const valueToBlur = this.getValue(null)

                    if (Array.isArray(valueToBlur) && every(valueToBlur, value => value)) {
                        onBlur?.(valueToBlur)
                    }
                } else {
                    onBlur?.(this.getValue(DateTimeControlName.DEFAULT_NAME))
                }
            }
            this.setVisibility(false)
        }
    }

    /**
     * Рендер попапа
     */
    renderPopUp() {
        const { isPopUpVisible } = this.state

        if (!isPopUpVisible) { return null }

        const { inputs, isTimeSet } = this.state
        const { max, min, configLocale = 'ru', timeFormat, dateFormat = DEFAULT_DATE_FORMAT, type } = this.props

        return (
            <PopUp
                dateFormat={dateFormat}
                time={this.defaultTime}
                type={type}
                isTimeSet={isTimeSet}
                markTimeAsSet={this.markTimeAsSet}
                timeFormat={timeFormat}
                value={inputs}
                select={this.select}
                max={max ? dayjs(max) : undefined}
                min={min ? dayjs(min) : undefined}
                locale={configLocale}
            />
        )
    }

    onFocus: FocusEventHandler<HTMLInputElement> = (e) => {
        const { onFocus } = this.props

        this.setState(
            { isFocus: true },
            () => onFocus?.(e),
        )
    }

    setInputRef = (popperRef: RefHandler) => (r: HTMLDivElement) => {
        this.inputGroup = r
        popperRef(r)
    }

    setControlRef = (el: ElementType) => { this.control = el }

    render() {
        const {
            disabled,
            placeholder,
            className,
            autoFocus,
            openOnFocus = false,
            popupPlacement = 'bottom-start',
            outputFormat = DEFAULT_OUTPUT_FORMAT,
            strategy = 'absolute',
            max,
            min,
            onKeyDown,
        } = this.props

        const { inputs, isPopUpVisible } = this.state

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
                                    ref={this.setInputRef(ref as RefHandler)}
                                    dateFormat={this.props.dateFormat}
                                    timeFormat={this.props.timeFormat}
                                    disabled={disabled}
                                    placeholder={placeholder}
                                    value={inputs}
                                    onInputChange={this.onInputChange}
                                    inputClassName={className}
                                    setVisibility={this.setVisibility}
                                    onFocus={this.onFocus}
                                    onBlur={this.onInputBlur}
                                    onKeyDown={onKeyDown}
                                    autoFocus={autoFocus}
                                    openOnFocus={openOnFocus}
                                    outputFormat={outputFormat}
                                    max={max}
                                    min={min}
                                />
                            )}
                        </Reference>
                        {isPopUpVisible && (
                            <Popper
                                placement={popupPlacement}
                                strategy={strategy}
                            >
                                {({ ref, style, placement }) => (
                                    <div
                                        ref={ref}
                                        style={style}
                                        data-placement={placement}
                                        className="n2o-pop-up"
                                    >
                                        {this.renderPopUp()}
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
