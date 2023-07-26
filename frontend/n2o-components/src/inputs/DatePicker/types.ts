import { Moment } from 'moment'
import { PopperProps } from 'react-popper'

export const enum CalendarType {
    BY_DAYS = 'by_days',
    BY_MONTHS = 'by_months',
    BY_YEARS = 'by_years',
    TIME_PICKER = 'time_picker',
}

export const enum DateTimeControlName {
    BEGIN = 'begin',
    DEFAULT_NAME = 'singleDate',
    END = 'end'
}

export type DatePickerValue =
    Array<{ defaultTime?: string, name: string | null, value: string | null }>
    | string
    | null

export type DateType = string | Moment | Date

export type Time = {
    hours: number,
    minutes: number,
    seconds: number
}

export type TimeExtended = Time & {
    hasDefaultTime: boolean,
}

export type DefaultTime = Record<string, TimeExtended>

export type OnInputChangeHandler = (date: Moment | null, inputName: string, callback?: () => void) => void

export type PopperPositioningStrategy = PopperProps<unknown>['strategy']
export type PopperPlacement = PopperProps<unknown>['placement']
