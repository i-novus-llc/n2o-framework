import React from 'react'
import isNull from 'lodash/isNull'
import dayjs, { Dayjs, UnitType, isDayjs } from 'dayjs'
import localeData from 'dayjs/plugin/localeData'
import 'dayjs/locale/ru'
import classNames from 'classnames'

import { Day } from './Day'
import { CalendarHeader } from './CalendarHeader'
import { weeks, isDateFromPrevMonth, isDateFromNextMonth, addTime, objFromTime } from './utils'
import { CalendarType, DateTimeControlName, ControlType, type CalendarProps } from './types'

import '../../styles/components/Calendar.scss'

dayjs.extend(localeData)
dayjs.locale('ru')

type CalendarState = {
    calendarType: CalendarType,
    displayesMonth: Dayjs,
    tempTimeObj: CalendarProps['time']
}

const TIME_UNIT = 'n2o-calendar-time-unit'
const MONTH_ITEM = 'month-item'

export class Calendar extends React.Component<CalendarProps, CalendarState> {
    private hourRef: HTMLDivElement | null = null

    private minuteRef: HTMLDivElement | null = null

    private secondRef: HTMLDivElement | null = null

    constructor(props: CalendarProps) {
        super(props)
        this.state = {
            displayesMonth: props.value
                ? props.value.clone().startOf('month')
                : dayjs().startOf('month'),
            calendarType: CalendarType.BY_DAYS,
            tempTimeObj: props.value ? objFromTime(props.value) : props.time,
        }
    }

    changeCalendarType = (type: CalendarType) => {
        this.setState({ calendarType: type })
    }

    // eslint-disable-next-line react/no-deprecated
    componentWillReceiveProps(props: CalendarProps) {
        if (props.value) {
            this.setState({
                displayesMonth: props.value
                    ? props.value.clone().startOf('month')
                    : dayjs().startOf('month'),
                tempTimeObj: props.value ? objFromTime(props.value) : props.time,
            })
        }
    }

    setHourRef = (el: HTMLDivElement) => {
        this.hourRef = el
    }

    setMinuteRef = (el: HTMLDivElement) => {
        this.minuteRef = el
    }

    setSecondRef = (el: HTMLDivElement) => {
        this.secondRef = el
    }

    renderHeader() {
        const { displayesMonth, calendarType } = this.state
        const { locale = 'ru', t } = this.props
        const {
            nextMonth,
            nextYear,
            prevMonth,
            prevYear,
            nextDecade,
            prevDecade,
            changeCalendarType,
        } = this

        return (
            <CalendarHeader
                nextMonth={nextMonth}
                nextYear={nextYear}
                prevMonth={prevMonth}
                prevYear={prevYear}
                nextDecade={nextDecade}
                prevDecade={prevDecade}
                displayesMonth={displayesMonth}
                locale={locale}
                calendarType={calendarType}
                changeCalendarType={changeCalendarType}
                t={t}
            />
        )
    }

    prevMonth = () => {
        const { displayesMonth } = this.state

        this.setState({ displayesMonth: displayesMonth.subtract(1, 'months') })
    }

    nextMonth = () => {
        const { displayesMonth } = this.state

        this.setState({ displayesMonth: displayesMonth.add(1, 'months') })
    }

    setMonth = (month: Dayjs) => {
        this.setState({ displayesMonth: month })
    }

    prevYear = () => {
        const { displayesMonth } = this.state

        this.setState({ displayesMonth: displayesMonth.subtract(1, 'years') })
    }

    nextYear = () => {
        const { displayesMonth } = this.state

        this.setState({ displayesMonth: displayesMonth.add(1, 'years') })
    }

    nextDecade = () => {
        const { displayesMonth } = this.state

        this.setState({ displayesMonth: displayesMonth.add(10, 'years') })
    }

    prevDecade = () => {
        const { displayesMonth } = this.state

        this.setState({ displayesMonth: displayesMonth.subtract(10, 'years') })
    }

    setDate(unit: UnitType, value: number | string) {
        const { displayesMonth } = this.state

        this.setState({ displayesMonth: displayesMonth.set(unit, Number(value)) })
    }

    renderNameOfDays() {
        const { locale = 'ru' } = this.props
        const nameOfDays = locale === 'ru'
            ? ['Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб', 'Вс']
            : ['Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa', 'Su']

        return (
            <tr>
                {nameOfDays.map((day, i) => (
                    // eslint-disable-next-line react/no-array-index-key
                    <td key={i}>{day}</td>
                ))}
            </tr>
        )
    }

    renderWeeks() {
        const { displayesMonth, tempTimeObj } = this.state
        const { hours, minutes, seconds } = tempTimeObj || {}
        const firstDay = addTime(
            displayesMonth.clone().startOf('week'),
            hours,
            minutes,
            seconds,
        )

        return weeks(firstDay).map((week, i) => this.renderWeek(week, i))
    }

    renderWeek(week: Dayjs[], i: number) {
        return <tr key={i}>{week.map((day, i) => this.renderDay(day, i))}</tr>
    }

    // eslint-disable-next-line complexity
    renderDay(day: Dayjs, i: number) {
        const {
            min,
            max,
            value,
            select,
            inputName,
            type,
            index,
            values,
            dateFormat,
        } = this.props

        let disabled = false

        if (min && max) {
            disabled = day.isBefore(min) || day.isAfter(max)
        } else if (min) {
            disabled = day.isBefore(min)
        } else if (max) {
            disabled = day.isAfter(max)
        }

        if (type === ControlType.DATE_INTERVAL) {
            const { begin } = values

            const disableDaysBefore = (range?: Dayjs | string | Date | null) => day.isBefore(range)
            const disableDaysAfter = (range?: Dayjs | string | Date | null) => day.isAfter(range)

            const disabledDaysBeyondTheScopeMinMax = disableDaysBefore(min) || disableDaysAfter(max)
            const disabledDaysBeyondTheScopeBeginMax = disableDaysBefore(begin) || disableDaysAfter(max)

            // eslint-disable-next-line no-unused-vars,no-return-assign, @typescript-eslint/no-unused-vars
            const rangeMinMax = (disabledDays: boolean) => (disabledDays = disabledDaysBeyondTheScopeMinMax)

            // если не выбрана дата(begin) ||  min or max
            const hasMinOrMaxAndNullBegin = (isNull(begin) && (min || max)) || (isNull(begin) && (min && max))

            // если hasMinOrMaxAndNullBegin, устанавливаем default range (min or max)
            if (hasMinOrMaxAndNullBegin) {
                rangeMinMax(disabled)
            } else if (min && max) {
                disabled = index === 0
                    ? disabledDaysBeyondTheScopeMinMax
                    : disabledDaysBeyondTheScopeBeginMax
            } else if (min) {
                disabled = index === 0
                    ? disableDaysBefore(min)
                    : index === 1 && disableDaysBefore(begin)
            } else if (max) {
                disabled = index === 0
                    ? disableDaysAfter(max)
                    : index === 1 && disableDaysAfter(begin)
            } else {
                // не указан range (min, max)
                disabled = index === 1 && disableDaysBefore(begin)
            }
        }

        const { displayesMonth } = this.state
        const otherMonth = isDateFromNextMonth(day, displayesMonth) ||
            isDateFromPrevMonth(day, displayesMonth)
        const selected = day.isSame(value)
        const current = day.format('DD.MM.YYYY') === dayjs().format('DD.MM.YYYY')
        const props = {
            day,
            otherMonth,
            selected,
            disabled,
            select,
            inputName,
            current,
            dateFormat,
        }

        return <Day key={i} {...props} />
    }

    renderTime() {
        const { value, hasDefaultTime, timeFormat, t } = this.props

        if (hasDefaultTime) {
            if (value) { return value.format(timeFormat) }

            return '00:00:00'
        }

        if (t) { return t('chooseTime') }

        return null
    }

    renderByDays() {
        const { timeFormat } = this.props

        return (
            <>
                <table className="n2o-calendar-table">
                    <thead>{this.renderNameOfDays()}</thead>
                    <tbody>{this.renderWeeks()}</tbody>
                </table>
                {timeFormat && (
                    <a
                        className="n2o-calendar-time-container"
                        href="/test"
                        onClick={(e) => {
                            e.preventDefault()
                            this.changeCalendarType(CalendarType.TIME_PICKER)
                        }}
                    >
                        {this.renderTime()}
                    </a>
                )}
            </>
        )
    }

    renderByMonths() {
        const months = Array.from({ length: 12 }, (_, i) => dayjs().month(i).format('MMMM'))

        return (
            <div className="n2o-calendar-body">
                {this.renderList(months, MONTH_ITEM)}
            </div>
        )
    }

    onItemClick = (itemType: string, item: string | number, i: number) => {
        if (itemType === MONTH_ITEM) {
            this.setDate('month', i)
        } else {
            this.setDate('year', item)
        }
        this.changeCalendarType(CalendarType.BY_DAYS)
    }

    renderList(list: Array<string | number>, className: string) {
        const { value } = this.props

        const isActive = (className: string, item: string | number, i: number) => {
            if (!value) { return false }
            if (className !== MONTH_ITEM) { return item === value.year() }

            return i === value.month()
        }
        const isOtherDecade = (i: number) => className === 'year-item' && (i === 0 || i === 11)

        return list.map((item, i) => (
            <div
                className={classNames('n2o-calendar-body-item', className, {
                    active: isActive(className, item, i),
                    'other-decade': isOtherDecade(i),
                })}
                onClick={() => this.onItemClick(className, item, i)}
            >
                {item}
            </div>
        ))
    }

    renderByYears() {
        const { displayesMonth } = this.state
        const decadeStart = parseInt(String(Number(displayesMonth.format('YYYY')) / 10), 10) * 10
        const years = Array.from(
            new Array(12),
            (val, index) => decadeStart + index - 1,
        )

        return (
            <div className="n2o-calendar-body">
                {this.renderList(years, 'year-item')}
            </div>
        )
    }

    componentDidUpdate() {
        const { calendarType } = this.state
        const { type, inputName } = this.props

        // Логика переключения end до 1ого доступноного месяца, относительно begin
        if (type === ControlType.DATE_INTERVAL && inputName === DateTimeControlName.END) {
            const { values } = this.props
            const beginDisplayesMonth = values[DateTimeControlName.BEGIN]
            const { displayesMonth } = this.state

            if (isDayjs(beginDisplayesMonth) && displayesMonth.isBefore(beginDisplayesMonth, 'month')) {
                const diffInMonths = beginDisplayesMonth.diff(displayesMonth, 'months')

                this.setState({ displayesMonth: displayesMonth.add(diffInMonths, 'months') })
            }
        }

        if (calendarType === CalendarType.TIME_PICKER) {
            if (this.minuteRef) {
                this.minuteRef.scrollIntoView()
            }
            if (this.secondRef) {
                this.secondRef.scrollIntoView()
            }
            if (this.hourRef) {
                this.hourRef.scrollIntoView()
            }
        }
    }

    setTimeUnit = (value: number, unit: string) => {
        const { tempTimeObj } = this.state

        this.setState({
            tempTimeObj: { ...tempTimeObj, [unit]: value },
        })
    }

    setTime = () => {
        const { value, inputName, markTimeAsSet, select } = this.props
        const { tempTimeObj } = this.state
        const { hours, minutes, seconds } = tempTimeObj || {}
        const copyValue = value || dayjs()

        this.changeCalendarType(CalendarType.BY_DAYS)
        markTimeAsSet(inputName)

        select(
            addTime(copyValue.clone().startOf('day'), hours, minutes, seconds),
            inputName,
            false,
        )
    }

    renderTimePicker() {
        const { tempTimeObj } = this.state
        const { minutes, seconds, hours } = tempTimeObj || {}

        /* eslint-disable react/jsx-one-expression-per-line */
        return (
            <div>
                <div className="n2o-calendar-timepicker">
                    <div className="n2o-calendar-picker hour-picker">
                        {Array.from(new Array(24), (val, index) => (
                            <div
                                className={classNames(TIME_UNIT, {
                                    active: index === hours,
                                })}
                                onClick={() => this.setTimeUnit(index, 'hours')}
                                ref={index === hours ? this.setHourRef : null}
                            >
                                {index}
                            </div>
                        ))}
                    </div>
                    <div className="n2o-calendar-picker minute-picker">
                        {Array.from(new Array(60), (val, index) => (
                            <div
                                className={classNames(TIME_UNIT, {
                                    active: index === minutes,
                                })}
                                ref={index === minutes ? this.setMinuteRef : null}
                                onClick={() => this.setTimeUnit(index, 'minutes')}
                            >
                                {index}
                            </div>
                        ))}
                    </div>
                    <div className="n2o-calendar-picker second-picker">
                        {Array.from(new Array(60), (val, index) => (
                            <div
                                className={classNames(TIME_UNIT, {
                                    active: index === seconds,
                                })}
                                ref={index === seconds ? this.setSecondRef : null}
                                onClick={() => this.setTimeUnit(index, 'seconds')}
                            >
                                {index}
                            </div>
                        ))}
                    </div>
                </div>
                <div className="n2o-calendar-time-buttons">
                    <button
                        type="button"
                        className="btn btn-secondary btn-sm"
                        onClick={() => this.changeCalendarType(CalendarType.BY_DAYS)}
                    >
                        {' '}

                        Назад
                    </button>
                    <button className="btn btn-primary btn-sm" onClick={this.setTime} type="button">
                        {' '}

                        Выбрать
                    </button>
                </div>
            </div>
        )
    }

    renderBody(type: string) {
        switch (type) {
            case CalendarType.BY_MONTHS: return this.renderByMonths()
            case CalendarType.BY_YEARS: return this.renderByYears()
            case CalendarType.TIME_PICKER: return this.renderTimePicker()
            case CalendarType.BY_DAYS:
            default: return this.renderByDays()
        }
    }

    render() {
        const { calendarType } = this.state
        const { timeFormat } = this.props

        return (
            <div className={classNames('n2o-calendar', { time: timeFormat })}>
                {this.renderHeader()}
                {this.renderBody(calendarType)}
            </div>
        )
    }
}
