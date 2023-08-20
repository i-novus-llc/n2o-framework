import React, { Component, KeyboardEvent, MouseEventHandler, RefObject } from 'react'
import { findDOMNode } from 'react-dom'
import moment, { Moment } from 'moment/moment'
import includes from 'lodash/includes'
import pick from 'lodash/pick'
import keys from 'lodash/keys'
import join from 'lodash/join'
import map from 'lodash/map'
import get from 'lodash/get'
import each from 'lodash/each'
import split from 'lodash/split'
import cn from 'classnames'
import { Row, Col } from 'reactstrap'
import { Manager, Reference, Popper } from 'react-popper'
import scrollIntoView from 'scroll-into-view-if-needed'

import { InputIcon } from '../../display/InputIcon'
import { TBaseInputProps, TBaseProps } from '../../types'

import '../../styles/controls/TimePicker.scss'

import { InputText } from '../InputText'

const HOURS = 'hours'
const MINUTES = 'minutes'
const SECONDS = 'seconds'

const DIGIT = 'digit'
const SYMBOLS = 'symbols'

const reference: Record<Mode, Record<string, string | number>> = {
    [HOURS]: {
        format: 'HH',
        en: 'h_hours',
        ru: 'ч_часы',
        values: 24,
    },
    [MINUTES]: {
        format: 'mm',
        en: 'min_minutes',
        ru: 'мин_минуты',
        values: 60,
    },
    [SECONDS]: {
        format: 'ss',
        en: 'sec_seconds',
        ru: 'сек_секунды',
        values: 60,
    },
}

const toTime = (value: number, noZero?: boolean) => (value < 10 && !noZero ? `0${value}` : value)

const handlePrevent: MouseEventHandler<HTMLAnchorElement> = (e) => {
    e.preventDefault()
}

type Mode = 'hours' | 'minutes' | 'seconds'

type TimePickerProps = Pick<TBaseProps, 'disabled'> & TBaseInputProps<string> & {
    defaultValue?: string,
    disabled?: boolean,
    format?: 'digit' | 'symbols',
    locale?: 'en' | 'ru',
    mode?: Mode[],
    noZero?: boolean,
    onChange(value: string): void,
    onKeyDown?(evt: KeyboardEvent<HTMLInputElement>): void,
    placeholder?: string,
    prefix?: string,
    timeFormat?: string
}

type TimePickerState = {
    'hours': number,
    'minutes': number,
    open: boolean,
    'seconds': number
}

export class TimePicker extends Component<TimePickerProps, TimePickerState> {
    value: Moment | null = null

    controlRef: RefObject<HTMLDivElement> | null = null

    hoursRef: RefObject<HTMLDivElement> | null = null

    minutesRef: RefObject<HTMLDivElement> | null = null

    secondsRef: RefObject<HTMLDivElement> | null = null

    constructor(props: TimePickerProps) {
        super(props)
        const { value, defaultValue, timeFormat = 'HH:mm:ss' } = props

        this.value = moment(value || defaultValue, timeFormat)
        this.state = {
            open: false,
            hours: this.value.hours(),
            minutes: this.value.minutes(),
            seconds: this.value.seconds(),
        }
        this.hoursRef = React.createRef()
        this.minutesRef = React.createRef()
        this.secondsRef = React.createRef()
        this.controlRef = React.createRef()
    }

    componentDidMount() {
        if (typeof window !== 'undefined') {
            document.addEventListener('mousedown', this.onClickOutside)
            document.addEventListener('touchstart', this.onClickOutside)
        }
    }

    componentDidUpdate(prevProps: TimePickerProps, prevState: TimePickerState) {
        const { open } = this.state
        const { value, timeFormat } = this.props

        const hasChangeVisible = open !== prevState.open

        if (prevProps.value !== value) {
            this.value = moment(value, timeFormat)
            this.setState({
                hours: this.value.hours(),
                minutes: this.value.minutes(),
                seconds: this.value.seconds(),
            })
        }

        each([HOURS, MINUTES, SECONDS], (mode) => {
            const ref = this[`${mode}Ref` as keyof TimePicker]

            if (ref?.current) {
                scrollIntoView(ref.current, {
                    behavior: hasChangeVisible ? 'auto' : 'smooth',
                    block: 'start',
                    boundary: ref.current.parentElement,
                })
            }
        })
    }

    componentWillUnmount() {
        if (typeof window !== 'undefined') {
            document.removeEventListener('mousedown', this.onClickOutside)
            document.removeEventListener('touchstart', this.onClickOutside)
        }
    }

    onClickOutside: EventListener = (e) => {
        const { open } = this.state
        const target = e.target as HTMLElement

        if (open) {
            // eslint-disable-next-line react/no-find-dom-node
            const controlEl = findDOMNode(this.controlRef?.current)

            if (target.className.includes('n2o-pop-up') || !controlEl?.contains(target)) {
                this.handleClose()
            }
        }
    }

    getTimeConfig = () => {
        const { mode } = this.props

        return {
            showHour: includes(mode, 'hours'),
            showMinute: includes(mode, 'minutes'),
            showSecond: includes(mode, 'seconds'),
        }
    }

    getLocaleText = (mode: Mode, index: number) => {
        const { locale = 'ru' } = this.props
        const searchString = get(reference, `[${mode}][${locale}]`, '') as string
        const localesArr = split((searchString), '_')

        return localesArr[index]
    }

    getTime = (format?: string) => (this.value?.isValid() ? this.value.format(format) : '')

    // eslint-disable-next-line consistent-return
    getValue = () => {
        const { format = 'symbols', timeFormat = 'HH:mm:ss', mode = ['hours', 'minutes', 'seconds'] } = this.props

        if (format === DIGIT) {
            return this.getTime(timeFormat)
        }

        if (format === SYMBOLS) {
            return this.getTime(
                join(
                    map(
                        keys(pick(reference, mode)),
                        (mode: Mode) => `${reference[mode].format} [${this.getLocaleText(mode, 0)}]`,
                    ),
                    ' ',
                ),
            )
        }
    }

    handleClose = () => {
        const { disabled } = this.props

        if (disabled) { return }
        this.setState({ open: false })
    }

    handleToggle = () => {
        const { disabled } = this.props

        if (disabled) { return }
        this.setState(state => ({ ...state, open: !state.open }))
    }

    handleChangeValue = (mode: Mode, value: number): MouseEventHandler<HTMLAnchorElement> => (e) => {
        const {
            [HOURS]: hours,
            [MINUTES]: minutes,
            [SECONDS]: seconds,
        } = this.state

        e.preventDefault()

        const prevState = {
            [HOURS]: hours || 0,
            [MINUTES]: minutes || 0,
            [SECONDS]: seconds || 0,
        }

        if (!this.value?.isValid()) {
            this.value = moment()
            this.value.set(prevState)
        }

        this.value.set(mode, value)

        this.setState(
            {
                ...prevState,
                [mode]: value,
            },
            () => {
                const { timeFormat, onChange } = this.props

                onChange(this.getTime(timeFormat))
            },
        )
    }

    renderPrefix = () => {
        const { prefix } = this.props

        if (prefix) {
            return <span className="time-prefix">{prefix}</span>
        }

        return null
    }

    renderPanelItems = (mode: Mode) => {
        const countersArray = new Array(get(reference, `[${mode}].values`, []))
        const { noZero } = this.props
        const { [mode]: modeValue } = this.state

        return map(countersArray, (val, index) => (
            // eslint-disable-next-line jsx-a11y/anchor-is-valid
            <a
                key={index}
                ref={index === modeValue ? this[`${mode}Ref` as keyof TimePicker] : null}
                href="#"
                className={cn('dropdown-item n2o-time-picker__panel__item', {
                    active: index === modeValue,
                })}
                onMouseDown={this.handleChangeValue(mode, index)}
                onClick={handlePrevent}
            >
                {toTime(index, noZero)}
            </a>
        ))
    }

    render() {
        const { placeholder, disabled, onKeyDown } = this.props
        const { open } = this.state
        const timeConfig = this.getTimeConfig()
        const readyValue = this.getValue()
        const prefixCmp = this.renderPrefix()

        return (
            <div className="n2o-time-picker" ref={this.controlRef}>
                <Manager>
                    <Reference>
                        {({ ref }) => (
                            <InputText
                                inputRef={ref}
                                className="n2o-time-picker__input"
                                prefix={prefixCmp}
                                suffix={(
                                    <InputIcon clickable hoverable onClick={this.handleToggle}>
                                        <span
                                            className="fa fa-chevron-down"
                                            style={{
                                                transition: 'transform 150ms linear',
                                                transform: open ? 'rotate(-180deg)' : 'rotate(0deg)',
                                            }}
                                        />
                                    </InputIcon>
                                )}
                                readOnly
                                placeholder={placeholder}
                                disabled={disabled}
                                value={readyValue}
                                onClick={this.handleToggle}
                                onKeyDown={onKeyDown}
                            />
                        )}
                    </Reference>
                    {open ? (
                        <Popper
                            placement="bottom-end"
                            strategy="fixed"
                        >
                            {({ ref, style, placement }) => (
                                <div
                                    ref={ref}
                                    style={style}
                                    data-placement={placement}
                                    className="n2o-pop-up"
                                >
                                    <Row noGutters>
                                        {timeConfig.showHour ? (
                                            <Col>
                                                <div className="n2o-time-picker__header">
                                                    {this.getLocaleText(HOURS, 1)}
                                                </div>
                                                <div className="n2o-time-picker__panel">
                                                    {this.renderPanelItems(HOURS)}
                                                </div>
                                            </Col>
                                        ) : null}
                                        {timeConfig.showMinute ? (
                                            <Col>
                                                <div className="n2o-time-picker__header">
                                                    {this.getLocaleText(MINUTES, 1)}
                                                </div>
                                                <div className="n2o-time-picker__panel">
                                                    {this.renderPanelItems(MINUTES)}
                                                </div>
                                            </Col>
                                        ) : null}
                                        {timeConfig.showSecond ? (
                                            <Col>
                                                <div className="n2o-time-picker__header last">
                                                    {this.getLocaleText(SECONDS, 1)}
                                                </div>
                                                <div className="n2o-time-picker__panel">
                                                    {this.renderPanelItems(SECONDS)}
                                                </div>
                                            </Col>
                                        ) : null}
                                    </Row>
                                </div>
                            )}
                        </Popper>
                    ) : null}
                </Manager>
            </div>
        )
    }
}
