import React from 'react'

import { DateTimeControl } from './DateTimeControl'
import { type DatePickerProps } from './types'

export const DatePicker = ({ value, defaultValue, configLocale = 'ru', ...props }: DatePickerProps) => (
    <DateTimeControl
        strategy="fixed"
        {...props}
        configLocale={configLocale}
        value={value || defaultValue || ''}
        type="date-picker"
    />
)
