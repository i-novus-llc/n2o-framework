import React from 'react'
import defaults from 'lodash/defaults'

import { TBaseInputProps, TBaseProps } from '../../types'

import { DateTimeControl } from './DateTimeControl'
import { DateTimeControlName } from './types'

type IntervalValue = {
    [DateTimeControlName.BEGIN]: string | null,
    [DateTimeControlName.END]: string | null,
}

export type DateIntervalProps = TBaseProps & TBaseInputProps<IntervalValue> & {
    configLocale?: 'en' | 'ru',
    dateDivider?: string,
    dateFormat?: string,
    defaultTime?: string,
    defaultValue?: IntervalValue,
    locale?: 'en' | 'ru',
    max?: string,
    min?: string,
    onBlur?(value: IntervalValue): void,
    onChange?(value: IntervalValue): void,
    openOnFocus?: boolean,
    outputFormat?: string,
    timeFormat: string
}

export const DateInterval = ({
    value,
    defaultValue = {
        [DateTimeControlName.BEGIN]: null,
        [DateTimeControlName.END]: null,
    },
    onChange = () => {},
    onFocus = () => {},
    onBlur = () => {},
    dateFormat = 'DD/MM/YYYY',
    placeholder = '',
    disabled = false,
    dateDivider = ' ',
    min,
    max,
    className = '',
    configLocale = 'ru',
    openOnFocus = false,
    defaultTime,
    ...rest
}: DateIntervalProps) => {
    const newValue = defaults({ ...value }, defaultValue)

    const handleChange = (data: [string | null, string | null]) => {
        onChange({
            [DateTimeControlName.BEGIN]: data[0],
            [DateTimeControlName.END]: data[1],
        })
    }

    const handleBlur = (data: [string | null, string | null]) => {
        onBlur({
            [DateTimeControlName.BEGIN]: data[0],
            [DateTimeControlName.END]: data[1],
        })
    }

    const mappedValue = [
        {
            name: DateTimeControlName.BEGIN,
            value: newValue[DateTimeControlName.BEGIN],
            defaultTime,
        },
        {
            name: DateTimeControlName.END,
            value: newValue[DateTimeControlName.END],
            defaultTime,
        },
    ]

    return (
        <DateTimeControl
            {...rest}
            min={min}
            max={max}
            dateFormat={dateFormat}
            strategy="fixed"
            value={mappedValue}
            onChange={handleChange}
            onBlur={handleBlur}
            type="date-interval"
            disabled={disabled}
        />
    )
}
