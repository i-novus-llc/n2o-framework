import dayjs, { Dayjs } from 'dayjs'
import flattenDeep from 'lodash/flattenDeep'
import map from 'lodash/map'
import isNull from 'lodash/isNull'

import { DateType, DatePickerValue, DefaultTime } from './types'

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
export const addTime = (date: Dayjs, hours: number, minutes: number, secs: number) => date
    .clone()
    .add(hours, 'hour')
    .add(minutes, 'minute')
    .add(secs, 'second')

export const withLocale = (date: Dayjs, locale: string) => date.clone().locale(locale)

/**
 * преобразовать дату к dayjs
 */
export const parseDate = (value: DateType) => {
    if (value instanceof Date || typeof value === 'string') {
        const date = dayjs(value)

        if (!date.isValid()) {
            // eslint-disable-next-line no-console
            console.warn('Invalid date')
        }

        return date
    }

    return value
}

export const mapToValue = (
    val: DatePickerValue,
    defaultTime: DefaultTime,
    dateFormat: string,
    locale: string,
    defaultName: string,
) => {
    const res: Record<string, Dayjs | null> = {}

    if (Array.isArray(val)) {
        map(val, ({ value, name }) => {
            if (!name) {
                name = ''
            }
            if (!value) {
                res[name] = null
            } else {
                res[name] = addTime(
                    withLocale(parseDate(value), locale).startOf('day'),
                    defaultTime[name].hours,
                    defaultTime[name].minutes,
                    defaultTime[name].seconds,
                )
            }
        })

        return res
    }

    if (!val) {
        return { [defaultName]: null }
    }

    res[defaultName] = addTime(
        withLocale(parseDate(val), locale).startOf('day'),
        defaultTime[defaultName].hours,
        defaultTime[defaultName].minutes,
        defaultTime[defaultName].seconds,
    )

    return res
}

export const mapToDefaultTime = (
    val: DatePickerValue,
    defaultTime: string,
    defaultName: string,
    timeFormat = 'HH:mm',
) => {
    if (Array.isArray(val)) {
        const result: DefaultTime = {}

        map(val, ({ name, defaultTime, value }) => {
            if (isNull(name)) {
                name = ''
            }
            result[name] = {
                hours: defaultTime
                    ? dayjs(defaultTime || '00:00', timeFormat).hour()
                    : (value && dayjs(value).hour()) || 0,
                minutes: defaultTime
                    ? dayjs(defaultTime || '00:00', timeFormat).minute()
                    : (value && dayjs(value).minute()) || 0,
                seconds: defaultTime
                    ? dayjs(defaultTime || '00:00', timeFormat).second()
                    : (value && dayjs(value).second()) || 0,
                hasDefaultTime: false,
            }

            if (
                result[name].hours ||
        result[name].minutes ||
        result[name].seconds ||
        timeFormat
            ) {
                result[name].hasDefaultTime = true
            }
        })

        return result
    }

    if (val) {
        return {
            [defaultName]: {
                hours: dayjs(val).hour(),
                minutes: dayjs(val).minute(),
                seconds: dayjs(val).second(),
                hasDefaultTime: true,
            },
        }
    }

    const result = {
        [defaultName]: {
            hours: dayjs(defaultTime, timeFormat).hour(),
            minutes: dayjs(defaultTime, timeFormat).minute(),
            seconds: dayjs(defaultTime, timeFormat).second(),
            hasDefaultTime: false,
        },
    }

    if (
        result[defaultName].hours ||
    result[defaultName].minutes ||
    result[defaultName].seconds
    ) {
        result[defaultName].hasDefaultTime = true
    }

    return result
}

export const buildDateFormat = (dateFormat?: string, timeFormat?: string, divider?: string) => `${dateFormat || ''}${dateFormat && timeFormat ? divider : ''}${timeFormat || ''}`

export const replaceDictionary = (tmp: string | RegExp) => {
    switch (tmp) {
        case 'DD':
            return [/[0-3]/, /\d/]
        case 'MM':
            return [/[01]/, /\d/]
        case 'YY':
            return [/\d/, /\d/]
        case 'YYYY':
            return [/[0-2]/, /\d/, /\d/, /\d/]
        case 'H':
        case 'HH':
            return [/[0-2]/, /\d/]
        case 'h':
        case 'hh':
            return [/[01]/, /\d/]
        case 'k':
        case 'kk':
            return [/[12]/, /\d/]
        case 's':
        case 'm':
        case 'ss':
        case 'mm':
            return [/[0-5]/, /\d/]
        default:
            return [/\d/]
    }
}

export const formatToMask = (format: string) => {
    const splitedFormat = format.split(/\b/gi)

    return flattenDeep(
        map(splitedFormat, (item: string): (RegExp | string) | Array<RegExp | string> => {
            if (item.search(/([a-z])\1+/gi) !== -1) {
                return replaceDictionary(item)
            }

            return item
        }),
    )
}

/**
 * Функция проверки находится ли дата в промежутке max и min
 */
export const hasInsideMixMax = (date: string, { max, min }: { max?: string, min?: string }, dateFormat: string): boolean => {
    if (!min && !max) { return true }

    let isAfterMin = true
    let isBeforeMax = true

    const formattedDate = dayjs(date, dateFormat)

    if (!formattedDate.isValid()) { return false }

    if (min) {
        const minDate = dayjs(min)

        isAfterMin = formattedDate.isAfter(minDate) || formattedDate.isSame(minDate)
    }

    if (max) {
        const maxDate = dayjs(max)

        isBeforeMax = formattedDate.isBefore(maxDate) || formattedDate.isSame(maxDate)
    }

    return isAfterMin && isBeforeMax
}

export const getDeletedSymbol = (value: string, index: number) => value.substring(index - 1, index)

export const replaceAt = (string: string, index: number, replacement: string) => (
    string.substring(0, index - 1) +
        replacement +
        string.substring(index, string.length)
)

export const objFromTime = (date: Dayjs) => {
    return {
        minutes: date.minute(),
        seconds: date.second(),
        hours: date.hour(),
    }
}
