import React, { forwardRef } from 'react'

import { NOOP_FUNCTION } from '../../utils/emptyTypes'

import { DateInput } from './DateInput'
import { type DateInputGroupProps } from './types'

export const DateInputGroup = forwardRef<HTMLDivElement, DateInputGroupProps>(({
    dateFormat,
    disabled,
    placeholder,
    value,
    inputClassName,
    onInputChange,
    setVisibility,
    onFocus = NOOP_FUNCTION,
    onBlur = NOOP_FUNCTION,
    onKeyDown = NOOP_FUNCTION,
    autoFocus = false,
    openOnFocus = false,
    setControlRef,
    outputFormat,
    max,
    min,
}, ref) => {
    const componentStyle = { display: 'flex', flexGrow: 1 }
    const wrapperStyle = {
        display: 'flex',
        justifyContent: 'space-between',
        flexGrow: 1,
    }

    return (
        <div ref={ref} style={wrapperStyle}>
            <div style={componentStyle}>
                {Object.keys(value).map((input, i) => (
                    <DateInput
                        /* eslint-disable-next-line react/no-array-index-key */
                        key={i}
                        dateFormat={dateFormat}
                        placeholder={placeholder}
                        name={input}
                        disabled={disabled}
                        value={value[input]}
                        onInputChange={onInputChange}
                        setVisibility={setVisibility}
                        onFocus={onFocus}
                        onBlur={onBlur}
                        onKeyDown={onKeyDown}
                        setControlRef={setControlRef}
                        autoFocus={autoFocus}
                        openOnFocus={openOnFocus}
                        inputClassName={inputClassName}
                        outputFormat={outputFormat}
                        max={max}
                        min={min}
                    />
                ))}
            </div>
        </div>
    )
})
