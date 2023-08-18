import React, { FocusEventHandler, KeyboardEvent, forwardRef } from 'react'
import { Moment } from 'moment/moment'

import { TBaseInputProps, TBaseProps } from '../../types'

import { DateInput } from './DateInput'
import { OnInputChangeHandler } from './types'

type Value = Record<string, Moment | null>

type DateInputGroupProps = TBaseProps & Omit<TBaseInputProps<Value>, 'onBlur' | 'onFocus'> & {
    dateFormat: string,
    inputClassName?: string,
    max?: string,
    min?: string,
    onBlur?(value: Moment | null, name: string): void,
    onFocus?: FocusEventHandler<HTMLInputElement>,
    onInputChange?: OnInputChangeHandler,
    onKeyDown?(evt: KeyboardEvent<HTMLInputElement>): void,
    openOnFocus: boolean,
    outputFormat: string,
    setControlRef(el?: unknown): void,
    setVisibility(visible: boolean): void,
    value: Value
}

export const DateInputGroup = forwardRef<HTMLDivElement, DateInputGroupProps>(({
    dateFormat,
    disabled,
    placeholder,
    value,
    inputClassName,
    onInputChange,
    setVisibility,
    onFocus = () => {},
    onBlur = () => {},
    onKeyDown = () => {},
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
