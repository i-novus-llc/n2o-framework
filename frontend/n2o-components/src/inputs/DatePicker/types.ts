import { FocusEventHandler, KeyboardEvent, MouseEventHandler, Component, ReactNode } from 'react'
import { Dayjs } from 'dayjs'
import { PopperProps } from 'react-popper'

import { TBaseInputProps, TBaseProps } from '../../types'

export enum CalendarType {
    BY_DAYS = 'by_days',
    BY_MONTHS = 'by_months',
    BY_YEARS = 'by_years',
    TIME_PICKER = 'time_picker',
}

export enum DateTimeControlName {
    BEGIN = 'begin',
    DEFAULT_NAME = 'singleDate',
    END = 'end',
}

export enum TIME_FORMAT {
    FULL = 'HH:mm:ss',
    SHORT = 'HH:mm',
    HOURS_ONLY = 'HH',
    WITH_MILLISECONDS = 'HH:mm:ss.SSS',
    FULL_WITH_AMPM = 'HH:mm:ss a',
    SHORT_WITH_AMPM = 'h:mm a',
}

export enum ControlType {
    DATE_PICKER = 'date-picker',
    DATE_INTERVAL = 'date-interval',
}

export type DatePickerValue = Array<{ defaultTime?: string; name: string | null; value: string | null }> | string | null
export type DateType = string | Dayjs | Date

export type Time = {
    hours: number
    minutes: number
    seconds: number
}

export type TimeExtended = Time & {
    hasDefaultTime: boolean
}

export type DefaultTime = Record<string, TimeExtended>

export type OnInputChangeHandler = (date: Dayjs | null, inputName: string, callback?: () => void) => void

export type PopperPositioningStrategy = PopperProps<unknown>['strategy']
export type PopperPlacement = PopperProps<unknown>['placement']

export type Locale = 'ru' | 'en'

type BaseDateProps = TBaseProps & {
    configLocale?: Locale
    dateFormat: string
    defaultTime?: string
    max?: string
    min?: string
    openOnFocus?: boolean
    outputFormat?: string
    timeFormat?: TIME_FORMAT
}

export type DateTimeControlProps = BaseDateProps & Omit<TBaseInputProps<DatePickerValue>, 'onChange' | 'onBlur'> & {
    dateDivider?: string
    onBlur?(value: string | null | Array<string | null>): void
    onChange?(value: string | null | Array<string | null>): void
    onFocus?: FocusEventHandler
    onKeyDown?(evt: KeyboardEvent<HTMLInputElement>): void
    popupPlacement?: PopperPlacement
    strategy: PopperPositioningStrategy
    type?: string
    utc?: boolean
    value: DatePickerValue
}

export type DatePickerProps = BaseDateProps & TBaseInputProps<DatePickerValue> & {
    date?: DatePickerValue
    defaultValue?: DatePickerValue
    locale?: Locale
    onBlur?(value: string | null | Array<string | null>): void
    onChange?(value: string | null | Array<string | null>): void
    onFocus?: FocusEventHandler
    t?(str: string): string
}

type IntervalValue = {
    [DateTimeControlName.BEGIN]: string | null
    [DateTimeControlName.END]: string | null
}

export type DateIntervalProps = BaseDateProps & TBaseInputProps<IntervalValue> & {
    dateDivider?: string
    defaultValue?: IntervalValue
    locale?: Locale
    onBlur?(value: IntervalValue): void
    onChange?(value: IntervalValue): void
}

type Value = Record<string, Dayjs | null>

export type DateInputGroupProps = BaseDateProps & Omit<TBaseInputProps<Value>, 'onBlur' | 'onFocus'> & {
    inputClassName?: string
    onBlur?(value: Dayjs | null, name: string): void
    onFocus?: FocusEventHandler<HTMLInputElement>
    onInputChange?: OnInputChangeHandler
    onKeyDown?(evt: KeyboardEvent<HTMLInputElement>): void
    setControlRef(el?: unknown): void
    setVisibility(visible: boolean): void
    value: Value
}

export type DayProps = TBaseProps & {
    current?: boolean
    day: Dayjs
    inputName: string
    otherMonth?: boolean
    select(day: Dayjs, name: string): void
    selected?: boolean
}

export type PopUpProps = {
    dateFormat: string
    isTimeSet: Record<string, boolean | undefined>
    locale: Locale
    markTimeAsSet(str: string): void
    max?: Dayjs | Date | string
    min?: Dayjs | Date | string
    select(day: Dayjs | null, inputName: string, close?: boolean): void
    time: DefaultTime
    timeFormat?: string
    type?: string
    value: Record<string, Dayjs | null>
}

export type CalendarProps = {
    auto?: boolean
    dateFormat: string
    hasDefaultTime?: boolean
    index: number
    inputName: string
    locale: 'en' | 'ru'
    markTimeAsSet(str: string): void
    max?: Dayjs | Date | string
    min?: Dayjs | Date | string
    select(day: Dayjs, name: string, type?: boolean): void
    setPlacement?(): void
    setVisibility?(): void
    t(key: string): ReactNode
    time: Time
    timeFormat?: string
    type?: string
    value: Dayjs | null
    values: Record<string, Dayjs | null>
}
