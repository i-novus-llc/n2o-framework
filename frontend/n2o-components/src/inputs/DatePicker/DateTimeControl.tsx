import React, { ElementType, FocusEventHandler, KeyboardEvent } from 'react'
import { findDOMNode } from 'react-dom'
import every from 'lodash/every'
import isUndefined from 'lodash/isUndefined'
import isFunction from 'lodash/isFunction'
import isNull from 'lodash/isNull'
import moment, { Moment } from 'moment/moment'
import { Manager, Popper, Reference, RefHandler } from 'react-popper'
import { WrapperInstance } from 'react-onclickoutside'
import { isArray } from 'lodash'

import { TBaseInputProps, TBaseProps } from '../../types'

import { buildDateFormat, mapToDefaultTime, mapToValue } from './utils'
import { DateInputGroup } from './DateInputGroup'
import { PopUp } from './PopUp'
import {
    DateTimeControlName, DateType,
    DatePickerValue,
    OnInputChangeHandler, PopperPlacement,
    PopperPositioningStrategy,
    DefaultTime,
} from './types'

import '../../styles/components/DatePicker.scss'

type DateTimeControlProps = TBaseProps & Omit<TBaseInputProps<DatePickerValue>, 'onChange' | 'onBlur'> & {
    configLocale?: 'en' | 'ru',
    dateDivider?: string,
    dateFormat?: string,
    defaultTime?: string,
    max?: string,
    min?: string,
    onBlur?(value: string | null | Array<string | null>): void,
    onChange?(value: string | null | Array<string | null>): void,
    onFocus?: FocusEventHandler,
    onKeyDown?(evt: KeyboardEvent<HTMLInputElement>): void,
    openOnFocus?: boolean,
    outputFormat?: string,
    popupPlacement?: PopperPlacement,
    strategy: PopperPositioningStrategy,
    timeFormat?: string,
    type?: string,
    utc?: boolean,
    value: DatePickerValue
}

type DateTimeControlState = {
    inputs: Record<string, Moment | null>,
    isFocus: boolean,
    isPopUpVisible: boolean,
    isTimeSet: Record<string, boolean | undefined>
}

export const ControlType = {
    DATE_PICKER: 'date-picker',
    DATE_INTERVAL: 'date-interval',
}

export class DateTimeControl extends React.Component<DateTimeControlProps, DateTimeControlState> {
    format: string

    defaultTime: DefaultTime

    datePicker: HTMLDivElement | null = null

    inputGroup: HTMLDivElement | null = null

    private control: null | ElementType = null

    private popUp: WrapperInstance<unknown, unknown> | null = null

    constructor(props: DateTimeControlProps) {
        super(props)
        const {
            value,
            dateFormat = 'DD.MM.YYYY',
            configLocale = 'ru',
            timeFormat,
            dateDivider = ' ',
            outputFormat = 'DD.MM.YYYY HH:mm:ss',
            defaultTime = '00:00',
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
            dateFormat = 'DD.MM.YYYY',
            configLocale = 'ru',
            timeFormat,
            dateDivider = ' ',
            outputFormat = 'DD.MM.YYYY HH:mm:ss',
            defaultTime = '00:00',
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
        const { outputFormat = 'DD.MM.YYYY HH:mm:ss', utc } = this.props

        if (isNull(date) || typeof date === 'string') {
            return date
        }

        if (date instanceof Date) {
            return date.toString()
        }

        return utc
            ? moment.utc(date).format(outputFormat)
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
    select = (day: Moment | null, inputName: string, close = true) => {
        const { inputs } = this.state
        const { type } = this.props

        if (
            inputName === DateTimeControlName.DEFAULT_NAME ||
            inputName === DateTimeControlName.BEGIN ||
            (inputName === DateTimeControlName.END &&
                !inputs[DateTimeControlName.BEGIN]) ||
            (inputName === DateTimeControlName.END &&
                moment(day).isSameOrAfter(inputs[DateTimeControlName.BEGIN]))
        ) {
            const inputValue = () => {
                if (
                    inputName === DateTimeControlName.BEGIN &&
                    inputs[DateTimeControlName.END] &&
                    moment(day).isAfter(inputs[DateTimeControlName.END])
                ) {
                    return {
                        [inputName]: day,
                        [DateTimeControlName.END]: null,
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

    onInputBlur = (date: Moment | null, inputName: string) => {
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

        if (!isNull(begin) && moment(begin).isAfter(moment(end))) {
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

                    if (isArray(valueToBlur) && every(valueToBlur, value => value)) {
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
     * Приводит min, max к moment объекту, текущему формату
     */
    parseRange(range?: string) {
        const { dateFormat = 'DD.MM.YYYY' } = this.props

        // @ts-ignore @fixme не определяется библиотечный метод
        // eslint-disable-next-line no-underscore-dangle
        return !isUndefined(moment(range)._f)
            ? moment(range)
            : moment(range, dateFormat)
    }

    /**
     * Рендер попапа
     */
    renderPopUp() {
        const { isPopUpVisible } = this.state

        if (!isPopUpVisible) { return null }

        const { inputs, isTimeSet } = this.state
        const { max, min, configLocale = 'ru', timeFormat, dateFormat = 'DD.MM.YYYY', type } = this.props

        return (
            <PopUp
                dateFormat={dateFormat}
                time={this.defaultTime}
                type={type}
                isTimeSet={isTimeSet}
                markTimeAsSet={this.markTimeAsSet}
                timeFormat={timeFormat}
                ref={(popUp) => {
                    this.popUp = popUp

                    return popUp
                }}
                value={inputs}
                select={this.select}
                max={this.parseRange(max)}
                min={this.parseRange(min)}
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
            outputFormat = 'DD.MM.YYYY HH:mm:ss',
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
                                    dateFormat={this.format}
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
