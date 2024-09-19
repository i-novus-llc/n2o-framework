import React, { Component, KeyboardEvent, MouseEventHandler, RefObject, ComponentType } from 'react'
import { findDOMNode } from 'react-dom'
import dayjs, { Dayjs } from 'dayjs'
import customParseFormat from 'dayjs/plugin/customParseFormat'
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

import '../styles/controls/TimePicker.scss'
import { TBaseInputProps, TBaseProps } from '../types'
import { InputIcon } from '../display/InputIcon'

import { InputText } from './InputText'

dayjs.extend(customParseFormat)

const HOUR = 'hour'
const MINUTE = 'minute'
const SECOND = 'second'

const DIGIT = 'digit'
const SYMBOLS = 'symbols'

const reference: Record<Mode, Record<string, string | number>> = {
    [HOUR]: {
        format: 'HH',
        en: 'h_hours',
        ru: 'ч_часы',
        values: 24,
    },
    [MINUTE]: {
        format: 'mm',
        en: 'min_minutes',
        ru: 'мин_минуты',
        values: 60,
    },
    [SECOND]: {
        format: 'ss',
        en: 'sec_seconds',
        ru: 'сек_секунды',
        values: 60,
    },
}

const toTime = (value: number, noZero?: boolean) => (value < 10 && !noZero ? `0${value}` : value)

const handlePrevent: MouseEventHandler<HTMLButtonElement> = (e) => { e.preventDefault() }

type Mode = 'hour' | 'minute' | 'second'
type OldMode = 'hours' | 'minutes' | 'seconds'

type TimePickerProps = Pick<TBaseProps, 'disabled'> & TBaseInputProps<string> & {
    defaultValue?: string,
    disabled?: boolean,
    format?: 'digit' | 'symbols',
    locale?: 'en' | 'ru',
    mode: Mode[],
    noZero?: boolean,
    onChange(value: string): void,
    onKeyDown?(evt: KeyboardEvent<HTMLInputElement>): void,
    placeholder?: string,
    prefix?: string,
    timeFormat?: string
}

type TimePickerState = {
    'hour': number,
    'minute': number,
    open: boolean,
    'second': number
}

class TimePickerBody extends Component<TimePickerProps, TimePickerState> {
    value: Dayjs | null = null

    controlRef: RefObject<HTMLDivElement> | null = null

    hoursRef: RefObject<HTMLDivElement> | null = null

    minutesRef: RefObject<HTMLDivElement> | null = null

    secondsRef: RefObject<HTMLDivElement> | null = null

    constructor(props: TimePickerProps) {
        super(props)
        const { value, defaultValue, timeFormat = 'HH:mm:ss' } = props

        this.value = dayjs(value || defaultValue, timeFormat)

        this.state = {
            open: false,
            hour: this.value.hour(),
            minute: this.value.minute(),
            second: this.value.second(),
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
            this.value = dayjs(value, timeFormat)
            this.setState({
                hour: this.value.hour(),
                minute: this.value.minute(),
                second: this.value.second(),
            })
        }

        each([HOUR, MINUTE, SECOND], (mode) => {
            const ref = this[`${mode}Ref` as keyof TimePickerBody]

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

    getVisibilityConfig = () => {
        const { mode } = this.props

        return {
            hour: includes(mode, HOUR),
            minute: includes(mode, MINUTE),
            second: includes(mode, SECOND),
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
        const { format = 'symbols', timeFormat = 'HH:mm:ss', mode } = this.props

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

    handleChangeValue = (mode: Mode, value: number): MouseEventHandler<HTMLButtonElement> => (e) => {
        const {
            [HOUR]: hour,
            [MINUTE]: minute,
            [SECOND]: second,
        } = this.state

        e.preventDefault()

        const prevState = {
            [HOUR]: hour || 0,
            [MINUTE]: minute || 0,
            [SECOND]: second || 0,
        }

        if (!this.value?.isValid()) {
            this.value = dayjs()

            this.value = this.value.set(HOUR, prevState[HOUR])
            this.value = this.value.set(MINUTE, prevState[MINUTE])
            this.value = this.value.set(SECOND, prevState[SECOND])
        }

        this.value = this.value.set(mode, value)

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
            <button
                type="button"
                key={index}
                ref={index === modeValue ? this[`${mode}Ref` as keyof TimePickerBody] : null}
                className={cn('dropdown-item n2o-time-picker__panel__item', {
                    active: index === modeValue,
                })}
                onMouseDown={this.handleChangeValue(mode, index)}
                onClick={handlePrevent}
            >
                {toTime(index, noZero)}
            </button>
        ))
    }

    render() {
        const { placeholder, disabled, onKeyDown } = this.props
        const { open } = this.state
        const visibilityConfig = this.getVisibilityConfig()
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
                                placeholder={placeholder}
                                disabled={disabled}
                                value={readyValue}
                                onClick={this.handleToggle}
                                onKeyDown={onKeyDown}
                                readOnly
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
                                        {visibilityConfig.hour && (
                                            <Col>
                                                <div className="n2o-time-picker__header">
                                                    {this.getLocaleText(HOUR, 1)}
                                                </div>
                                                <div className="n2o-time-picker__panel">
                                                    {this.renderPanelItems(HOUR)}
                                                </div>
                                            </Col>
                                        )}
                                        {visibilityConfig.minute && (
                                            <Col>
                                                <div className="n2o-time-picker__header">
                                                    {this.getLocaleText(MINUTE, 1)}
                                                </div>
                                                <div className="n2o-time-picker__panel">
                                                    {this.renderPanelItems(MINUTE)}
                                                </div>
                                            </Col>
                                        )}
                                        {visibilityConfig.second && (
                                            <Col>
                                                <div className="n2o-time-picker__header last">
                                                    {this.getLocaleText(SECOND, 1)}
                                                </div>
                                                <div className="n2o-time-picker__panel">
                                                    {this.renderPanelItems(SECOND)}
                                                </div>
                                            </Col>
                                        )}
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

const MODE_MAP = {
    hours: 'hour',
    minutes: 'minute',
    seconds: 'second',
    hour: 'hour',
    minute: 'minute',
    second: 'second',
} as const

// Для сохранения обратной совместимости, в ранних версиях используется множественное число ['hours', 'minutes', 'seconds']
const patchMode = (mode?: Mode[] | OldMode[]): Mode[] => (mode
    ? mode.map(m => MODE_MAP[m])
    : [MODE_MAP.hour, MODE_MAP.minute, MODE_MAP.second])

function PatchMode(Component: ComponentType<TimePickerProps>) {
    function Wrapper(props: TimePickerProps) {
        const { mode } = props

        return <Component {...props} mode={patchMode(mode)} />
    }

    return Wrapper
}

export const TimePicker = PatchMode(TimePickerBody)
