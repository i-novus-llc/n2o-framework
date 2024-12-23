// @ts-ignore import from js file react-big-calendar
import { dayjsLocalizer } from 'react-big-calendar'
import { CSSProperties } from 'react'
import get from 'lodash/get'
import isNumber from 'lodash/isNumber'
import isEmpty from 'lodash/isEmpty'
import split from 'lodash/split'
import dayjs from 'dayjs'

export function isDayOff(day: Date): boolean {
    return [0, 6].includes(day.getDay())
}

export function isCurrentDay(day: Date): boolean {
    const currentDate = new Date()

    return (
        day.getDate() === currentDate.getDate() &&
        day.getMonth() === currentDate.getMonth() &&
        day.getFullYear() === currentDate.getFullYear()
    )
}

export function formatsMap(formats: Record<string, string> = {}) {
    const localizerFormat = (date: Date, format: string): string => dayjsLocalizer(dayjs).format(date, format)

    const rangeFormat = (startFormat: string, endFormat: string) => ({ start, end }: { start: Date; end: Date }) => `${localizerFormat(start, startFormat)} — ${localizerFormat(end, endFormat)}`

    const {
        dateFormat = 'DD',
        dayFormat = 'DD dd',
        weekdayFormat = 'dd',
        timeStartFormat = 'HH:mm',
        timeEndFormat = 'HH:mm',
        dayStartFormat = 'DD MMM',
        dayEndFormat = 'DD MMM',
        timeGutterFormat = 'LT',
        monthHeaderFormat = 'MMMM YYYY',
        dayHeaderFormat = 'dddd MMM DD',
        agendaDateFormat = 'dd MMM DD',
        agendaTimeFormat = 'LT',
    } = formats

    return {
        dateFormat,
        dayFormat: (date: Date) => localizerFormat(date, dayFormat),
        weekdayFormat: (date: Date) => localizerFormat(date, weekdayFormat),
        timeGutterFormat: (date: Date) => localizerFormat(date, timeGutterFormat),
        selectRangeFormat: rangeFormat(timeStartFormat, timeEndFormat),
        eventTimeRangeFormat: rangeFormat(timeStartFormat, timeEndFormat),
        monthHeaderFormat: (date: Date) => localizerFormat(date, monthHeaderFormat),
        dayHeaderFormat: (date: Date) => localizerFormat(date, dayHeaderFormat),
        dayRangeHeaderFormat: rangeFormat(dayStartFormat, dayEndFormat),
        agendaHeaderFormat: rangeFormat(dayStartFormat, dayEndFormat),
        agendaDateFormat: (date: Date) => localizerFormat(date, agendaDateFormat),
        agendaTimeFormat: (date: Date) => localizerFormat(date, agendaTimeFormat),
        agendaTimeRangeFormat: rangeFormat(timeStartFormat, timeEndFormat),
    }
}

export function eventLessHour(date: Record<string, string>, step: number): boolean {
    if (isNumber(step)) {
        const begin = new Date(get(date, 'begin'))
        const end = new Date(get(date, 'end'))
        const difference = Math.abs(end.getTime() - begin.getTime()) / (1000 * 3600)

        return difference <= (step / 60) * 2
    }

    return false
}

// eslint-disable-next-line consistent-return
export function timeParser(min: string, max: string): { min: Date; max: Date } | undefined {
    if (!isEmpty(min) && !isEmpty(max)) {
        const minTime = split(min, ':')
        const maxTime = split(max, ':')

        return {
            min: new Date(0, 0, 0, Number(minTime[0]), Number(minTime[1]), Number(minTime[2]), 0),
            max: new Date(0, 0, 0, Number(maxTime[0]), Number(maxTime[1]), Number(maxTime[2]), 0),
        }
    }
}

export function isAllDay(start: string, end: string): boolean {
    const startDate = dayjs(start, 'YYYY-MM-DD HH:mm')
    const endDate = dayjs(end, 'YYYY-MM-DD HH:mm')
    const dateDiff = endDate.diff(startDate, 'days')

    return dateDiff >= 1
}

export const mapStyle = (color: string, lessHour: boolean, { height, top, width }: CSSProperties = {}): CSSProperties => ({
    position: 'absolute',
    height: `${height}%`,
    top: `${top}%`,
    width: `${width}%`,
    backgroundColor: color,
    padding: lessHour ? '0 5px' : '2px 5px',
    lineHeight: lessHour ? '1' : '1.5',
    flexFlow: lessHour ? 'nowrap' : 'none',
})

export const monthEventStyle = (color: string): CSSProperties => ({
    backgroundColor: color,
})
