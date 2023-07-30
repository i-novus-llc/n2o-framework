import React, { ReactNode } from 'react'
import type { Moment } from 'moment'
import 'moment/locale/ru'

import { withLocale } from './utils'
import { CalendarType } from './types'

import '../../styles/components/CalendarHeader.scss'

type CalendarHeaderProps = {
    calendarType: CalendarType,
    changeCalendarType(type?: CalendarType): void,
    displayesMonth: Moment,
    locale: string,
    nextDecade(): void,
    nextMonth(): void,
    nextYear(): void,
    prevDecade(): void,
    prevMonth(): void,
    prevYear(): void,
    t(str: string): ReactNode
}

export class CalendarHeader extends React.Component<CalendarHeaderProps> {
    renderHeaderValue(displayesMonth: Moment, locale: string, type = CalendarType.BY_DAYS) {
        if (type === CalendarType.BY_DAYS) {
            return (
                <>
                    <button
                        type="button"
                        className="n2o-calendar-header-month-title capitalize link-button"
                        onClick={(e) => {
                            e.preventDefault()
                            const { changeCalendarType } = this.props

                            changeCalendarType(CalendarType.BY_MONTHS)
                        }}
                    >
                        {withLocale(displayesMonth, locale).format('MMMM')}
                    </button>

          &nbsp;
                    <button
                        type="button"
                        className="n2o-calendar-header-year-title link-button"
                        onClick={(e) => {
                            e.preventDefault()
                            const { changeCalendarType } = this.props

                            changeCalendarType(CalendarType.BY_YEARS)
                        }}
                    >
                        {displayesMonth.format('YYYY')}
                    </button>
                </>
            )
        }

        if (type === CalendarType.BY_MONTHS) {
            return withLocale(displayesMonth, locale).format('MMMM')
        }

        if (type === CalendarType.BY_YEARS) {
            const decadeStart = parseInt(String(+displayesMonth.format('YYYY') / 10), 10) * 10

            return (
                <button
                    type="button"
                    className="link-button"
                    onClick={(e) => {
                        e.preventDefault()
                    }}
                >
                    {`${decadeStart}-${decadeStart + 9}`}
                </button>
            )
        }
        if (type === CalendarType.TIME_PICKER) {
            const { t } = this.props

            return (
                <button
                    type="button"
                    className="link-button"
                    onClick={(e) => {
                        e.preventDefault()
                    }}
                >
                    {t('chooseTime')}
                </button>
            )
        }

        return null
    }

    static nextType = (type: CalendarType) => {
        switch (type) {
            case CalendarType.BY_YEARS:
            case CalendarType.BY_MONTHS:
                return CalendarType.BY_YEARS
            case CalendarType.BY_DAYS:
                return CalendarType.BY_MONTHS
            case CalendarType.TIME_PICKER:
                return CalendarType.TIME_PICKER
            default:
                return undefined
        }
    }

    nextView = (type: CalendarType) => {
        const { nextYear, nextMonth, nextDecade } = this.props

        switch (type) {
            case CalendarType.BY_MONTHS:
                nextYear()

                break
            case CalendarType.BY_YEARS:
                nextDecade()

                break
            default:
                nextMonth()
        }
    }

    prevView = (type: CalendarType) => {
        const { prevYear, prevMonth, prevDecade } = this.props

        switch (type) {
            case CalendarType.BY_MONTHS:
                prevYear()

                break
            case CalendarType.BY_YEARS:
                prevDecade()

                break
            default:
                prevMonth()
        }
    }

    render() {
        const {
            displayesMonth,
            locale,
            changeCalendarType,
            calendarType,
        } = this.props
        const dateStyle =
      calendarType !== CalendarType.BY_YEARS &&
      calendarType !== CalendarType.TIME_PICKER
          ? { cursor: 'pointer' }
          : { cursor: 'text' }

        return (
            <div className="n2o-calendar-header">
                {calendarType !== CalendarType.TIME_PICKER && (
                    <i
                        className="fa fa-angle-left n2o-calendar-icon"
                        aria-hidden="true"
                        onClick={() => this.prevView(calendarType)}
                    />
                )}
                <div
                    className="n2o-calendar-current-date"
                    style={dateStyle}
                    onClick={() => calendarType !== CalendarType.BY_DAYS &&
            changeCalendarType(CalendarHeader.nextType(calendarType))
                    }
                >
                    {this.renderHeaderValue(displayesMonth, locale, calendarType)}
                </div>
                {calendarType !== CalendarType.TIME_PICKER && (
                    <i
                        className="fa fa-angle-right n2o-calendar-icon"
                        aria-hidden="true"
                        onClick={() => this.nextView(calendarType)}
                    />
                )}
            </div>
        )
    }
}
