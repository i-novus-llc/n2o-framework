import moment, { LocaleSpecifier, Moment, MomentFormatSpecification, MomentInput } from 'moment/moment'
import flattenDeep from 'lodash/flattenDeep'
import map from 'lodash/map'
import isUndefined from 'lodash/isUndefined'
import isNull from 'lodash/isNull'

import { DateType, DatePickerValue, DefaultTime } from './types'

/**
 * Дата (date) была после конца месяца другой даты(displayedMonth) или нет
 */
export const isDateFromNextMonth = (date: Moment, displayedMonth: Moment) => (
    date.year() > displayedMonth.year() ||
    (date.year() === displayedMonth.year() &&
        date.month() > displayedMonth.month())
)

/**
 * Дата (date) была до начала месяца другой даты(displayedMonth) или нет
 */
export const isDateFromPrevMonth = (date: Moment, displayedMonth: Moment) => (
    date.year() < displayedMonth.year() ||
    (date.year() === displayedMonth.year() &&
        date.month() < displayedMonth.month())
)

/**
 * Возвращает массив из недель(каждая неделя - массив из дней)
 */
export const weeks = (firstDay: Moment) => {
    const lastDay = firstDay.clone().add(35, 'days')
    const day = firstDay.clone()
    const weeks = []

    while (day <= lastDay) {
        const week = []

        for (let i = 0; i < 7; i++) {
            week.push(day.clone())
            if (i === 6) {
                weeks.push(week)
            }
            day.add(1, 'days')
        }
    }

    return weeks
}

/**
 * добавить часы и минуты к дате
 */
export const addTime = (date: Moment, hours: number, minutes: number, secs: number) => date
    .clone()
    .add(hours, 'h')
    .add(minutes, 'm')
    .add(secs, 's')

export const withLocale = (date: Moment, locale: LocaleSpecifier) => date.clone().locale(locale)

/**
 * преобразовать дату к moment-объекту
 */
export const parseDate = (value: DateType, dateFormat: MomentFormatSpecification) => {
    if (value instanceof Date) {
        value = moment(value)
    } else if (typeof value === 'string') {
        value = moment(value, dateFormat)
        if (!value.isValid()) {
            // eslint-disable-next-line no-console
            console.warn('Invalid date')
        }
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
    const res: Record<string, Moment | null> = {}

    if (Array.isArray(val)) {
        map(val, ({ value, name }) => {
            if (!name) {
                name = ''
            }
            if (!value) {
                res[name] = null
            } else {
                res[name] = addTime(
                    withLocale(parseDate(value, dateFormat), locale).startOf('day'),
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
        withLocale(parseDate(val, dateFormat), locale).startOf('day'),
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
    timeFormat = 'HH:mm:ss',
) => {
    if (Array.isArray(val)) {
        const result: DefaultTime = {}

        map(val, ({ name, defaultTime, value }) => {
            if (isNull(name)) {
                name = ''
            }
            result[name] = {
                hours: defaultTime
                    ? moment(defaultTime || '00:00', timeFormat).hour()
                    : (value && moment(value).hour()) || 0,
                minutes: defaultTime
                    ? moment(defaultTime || '00:00', timeFormat).minute()
                    : (value && moment(value).minute()) || 0,
                seconds: defaultTime
                    ? moment(defaultTime || '00:00', timeFormat).second()
                    : (value && moment(value).second()) || 0,
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
                hours: moment(val).hour(),
                minutes: moment(val).minute(),
                seconds: moment(val).second(),
                hasDefaultTime: true,
            },
        }
    }

    const result = {
        [defaultName]: {
            hours: moment(defaultTime, timeFormat).hour(),
            minutes: moment(defaultTime, timeFormat).minute(),
            seconds: moment(defaultTime, timeFormat).second(),
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
export const hasInsideMixMax = (
    date: string,
    { max, min }: Record<string, string>,
    dateFormat: MomentFormatSpecification,
) => {
    const currentDate = moment(date, dateFormat)
    // @ts-ignore @fixme не определяется библиотечный метод _f
    // eslint-disable-next-line no-underscore-dangle
    const hasFormat = (range: MomentInput) => !isUndefined(moment(range)._f)

    const lessOrEqual = (range: MomentInput, dateFormat: MomentFormatSpecification) => (hasFormat(range)
        ? moment(range) <= currentDate
        : moment(range, dateFormat) <= currentDate)

    const moreOrEqual = (range: MomentInput, dateFormat: MomentFormatSpecification) => (
        hasFormat(range)
            ? moment(range) >= currentDate
            : moment(range, dateFormat) >= currentDate)

    if (!max && !min) { return true }

    return !!((!max && min && lessOrEqual(min, dateFormat)) ||
        (max && !min && moreOrEqual(max, dateFormat)) ||
        (max && min && lessOrEqual(min, dateFormat) && moreOrEqual(max, dateFormat)))
}

export const getDeletedSymbol = (value: string, index: number) => value.substring(index - 1, index)

export const replaceAt = (string: string, index: number, replacement: string) => (
    string.substring(0, index - 1) +
        replacement +
        string.substring(index, string.length)
)

export const objFromTime = (date: Moment) => ({
    minutes: date.minutes(),
    seconds: date.seconds(),
    hours: date.hours(),
})
