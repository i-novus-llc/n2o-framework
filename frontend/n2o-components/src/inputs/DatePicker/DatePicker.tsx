import React, { FocusEventHandler } from 'react'

import { TBaseInputProps, TBaseProps } from '../../types'

import { DateTimeControl } from './DateTimeControl'
import { DatePickerValue } from './types'

type DatePickerProps = TBaseProps & TBaseInputProps<DatePickerValue> & {
    configLocale?: 'en' | 'ru',
    date?: DatePickerValue,
    dateFormat?: string,
    defaultTime?: string,
    defaultValue?: DatePickerValue,
    locale?: 'en' | 'ru',
    max?: string,
    min?: string,
    onBlur?(value: string | null | Array<string | null>): void,
    onChange?(value: string | null | Array<string | null>): void,
    onFocus?: FocusEventHandler,
    openOnFocus?: boolean,
    outputFormat?: string,
    t?(str: string): string,
    timeFormat: string
}

export const DatePicker = ({ value, defaultValue, configLocale = 'ru', ...props }: DatePickerProps) => {
    return (
        <DateTimeControl
            strategy="fixed"
            {...props}
            configLocale={configLocale}
            value={value || defaultValue || ''}
            type="date-picker"
        />
    )
}
