import dayjs, { Dayjs } from 'dayjs'
import map from 'lodash/map'
import isNull from 'lodash/isNull'
import customParseFormat from 'dayjs/plugin/customParseFormat'

import { DateType, DatePickerValue, DefaultTime, TIME_FORMAT } from './types'

dayjs.extend(customParseFormat)

export const DEFAULT_DATE_FORMAT = 'DD.MM.YYYY'
export const DEFAULT_OUTPUT_FORMAT = `${DEFAULT_DATE_FORMAT}  HH:mm:ss`

/**
 * Дата (date) была после конца месяца другой даты(displayedMonth) или нет
 */
export const isDateFromNextMonth = (date: Dayjs, displayedMonth: Dayjs) => (
    date.year() > displayedMonth.year() ||
    (date.year() === displayedMonth.year() &&
        date.month() > displayedMonth.month())
)

/**
 * Дата (date) была до начала месяца другой даты(displayedMonth) или нет
 */
export const isDateFromPrevMonth = (date: Dayjs, displayedMonth: Dayjs) => (
    date.year() < displayedMonth.year() ||
    (date.year() === displayedMonth.year() &&
        date.month() < displayedMonth.month())
)

/**
 * Возвращает массив из недель(каждая неделя - массив из дней)
 */
export const weeks = (firstDay: Dayjs) => {
    const lastDay = firstDay.clone().add(35, 'days')
    let day = firstDay.clone()
    const weeks = []

    while (day <= lastDay) {
        const week = []

        for (let i = 0; i < 7; i++) {
            week.push(day.clone())
            if (i === 6) {
                weeks.push(week)
            }
            day = day.add(1, 'days')
        }
    }

    return weeks
}

/**
 * добавить часы и минуты к дате
 */
export const addTime = (date: Dayjs, hours: number, minutes: number, secs: number, milliseconds = 0) => date
    .clone()
    .add(hours, 'hour')
    .add(minutes, 'minute')
    .add(secs, 'second')
    .add(milliseconds, 'millisecond')

export const withLocale = (date: Dayjs, locale: string) => date.clone().locale(locale)

/**
 * преобразовать дату к dayjs
 */
export const parseDate = (value: DateType | string, format?: string): Dayjs => {
    if (value instanceof Date || dayjs.isDayjs(value)) {
        return dayjs(value)
    }

    if (typeof value === 'string') {
        let date = dayjs(value)

        if (!date.isValid() && format) {
            const escapedFormat = format.replace(/T/g, '[T]')

            date = dayjs(value, escapedFormat)
        }

        if (!date.isValid()) {
            console.warn('Invalid date', value, format)
        }

        return date
    }

    return dayjs(value)
}

export const mapToValue = (
    val: DatePickerValue,
    defaultTime: DefaultTime,
    outputFormat: string, // переименовал для ясности, это формат строки
    locale: string,
    defaultName: string,
) => {
    const res: Record<string, Dayjs | null> = {}

    if (Array.isArray(val)) {
        map(val, ({ value, name }) => {
            if (!name) { name = '' }
            if (!value) {
                res[name] = null
            } else {
                res[name] = addTime(
                    withLocale(parseDate(value, outputFormat), locale).startOf('day'),
                    defaultTime[name].hours,
                    defaultTime[name].minutes,
                    defaultTime[name].seconds,
                    defaultTime[name].milliseconds ?? 0,
                )
            }
        })

        return res
    }

    if (!val) {
        return { [defaultName]: null }
    }

    res[defaultName] = addTime(
        withLocale(parseDate(val, outputFormat), locale).startOf('day'),
        defaultTime[defaultName].hours,
        defaultTime[defaultName].minutes,
        defaultTime[defaultName].seconds,
        defaultTime[defaultName].milliseconds ?? 0,
    )

    return res
}
export const mapToDefaultTime = (
    val: DatePickerValue,
    defaultTime: string,
    defaultName: string,
    timeFormat = 'HH:mm',
    outputFormat?: string,
) => {
    if (Array.isArray(val)) {
        const result: DefaultTime = {}

        map(val, ({ name, defaultTime, value }) => {
            if (isNull(name)) { name = '' }

            const parsedValue = value ? parseDate(value, outputFormat) : null
            const parsedDefault = defaultTime ? dayjs(defaultTime, timeFormat) : null

            let hours = 0
            let minutes = 0
            let seconds = 0
            let milliseconds = 0

            if (parsedDefault) {
                hours = parsedDefault.hour()
                minutes = parsedDefault.minute()
                seconds = parsedDefault.second()
                milliseconds = parsedDefault.millisecond()
            } else if (parsedValue) {
                hours = parsedValue.hour()
                minutes = parsedValue.minute()
                seconds = parsedValue.second()
                milliseconds = parsedValue.millisecond()
            }

            result[name] = {
                hours,
                minutes,
                seconds,
                milliseconds,
                hasDefaultTime: false,
            }

            if (
                result[name].hours ||
                result[name].minutes ||
                result[name].seconds ||
                result[name].milliseconds ||
                timeFormat
            ) {
                result[name].hasDefaultTime = true
            }
        })

        return result
    }

    if (val) {
        const parsed = parseDate(val, outputFormat)

        return {
            [defaultName]: {
                hours: parsed.hour(),
                minutes: parsed.minute(),
                seconds: parsed.second(),
                milliseconds: parsed.millisecond(),
                hasDefaultTime: true,
            },
        }
    }

    const parsedDefaultTime = dayjs(defaultTime || '00:00', timeFormat)

    const result = {
        [defaultName]: {
            hours: parsedDefaultTime.hour(),
            minutes: parsedDefaultTime.minute(),
            seconds: parsedDefaultTime.second(),
            milliseconds: parsedDefaultTime.millisecond(),
            hasDefaultTime: false,
        },
    }

    if (
        result[defaultName].hours ||
        result[defaultName].minutes ||
        result[defaultName].seconds ||
        result[defaultName].milliseconds
    ) {
        result[defaultName].hasDefaultTime = true
    }

    return result
}
export const buildDateFormat = (dateFormat?: string, timeFormat?: string, divider?: string) => `${dateFormat || ''}${dateFormat && timeFormat ? divider : ''}${timeFormat || ''}`

export const objFromTime = (date: Dayjs) => {
    return {
        minutes: date.minute(),
        seconds: date.second(),
        hours: date.hour(),
        milliseconds: date.millisecond(),
    }
}

export function createDefaultTime(timeFormat?: TIME_FORMAT): string {
    if (!timeFormat) { return '00:00' }

    switch (timeFormat) {
        case TIME_FORMAT.FULL: return '00:00:00'
        case TIME_FORMAT.SHORT: return '00:00'
        case TIME_FORMAT.HOURS_ONLY: return '00'
        case TIME_FORMAT.WITH_MILLISECONDS: return '00:00:00.000'
        case TIME_FORMAT.FULL_WITH_AMPM: return '12:00:00 AM'
        case TIME_FORMAT.SHORT_WITH_AMPM: return '12:00 AM'
        default:
            console.warn(`Unknown time format: ${timeFormat}`)

            return ''
    }
}

/** Формирует итоговый формат (outputFormat) т.к. outputFormat с бэка приходит без milliseconds, но они есть в timeFormat **/
export const resolveOutputFormat = (
    outputFormat: string | undefined,
    dateFormat: string,
    timeFormat?: string,
) => {
    let format = outputFormat || (timeFormat ? `${dateFormat}  ${timeFormat}` : DEFAULT_OUTPUT_FORMAT)

    // Если в timeFormat есть миллисекунды (S), но в outputFormat их нет добавляем
    if (timeFormat && format) {
        const msMatch = timeFormat.match(/S+/)

        if (msMatch && !format.includes('S')) {
            format = `${format}.${msMatch[0]}`
        }
    }

    return format
}
