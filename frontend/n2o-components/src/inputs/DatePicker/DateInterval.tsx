import React from 'react'
import defaults from 'lodash/defaults'

import { DateTimeControl } from './DateTimeControl'
import { type DateIntervalProps, DateTimeControlName } from './types'

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
    strategy = 'fixed',
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
            strategy={strategy}
            value={mappedValue}
            onChange={handleChange}
            onBlur={handleBlur}
            type="date-interval"
            disabled={disabled}
        />
    )
}
