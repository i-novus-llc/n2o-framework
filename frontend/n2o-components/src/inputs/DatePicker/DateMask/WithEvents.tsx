import React, { ChangeEvent, ComponentType, FC, FocusEvent, MouseEvent } from 'react'
import dayjs from 'dayjs'

import { type WithEventsProps, DateMaskProps } from './types'
import { getFloatingPlaceholder } from './helpers'

export const WithEvents = <P extends DateMaskProps>(Component: ComponentType<P>) => {
    const Wrapper: FC<P & WithEventsProps> = ({
        onInputChange,
        onClick,
        onFocus,
        setVisibility,
        onBlur,
        name,
        openOnFocus,
        outputFormat,
        max,
        min,
        fullFormat,
        dateMode,
        timeMode,
        value: storedValue,
        ...rest
    }) => {
        const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
            const { value } = e.target

            const floatingPlaceholder = getFloatingPlaceholder(dateMode, timeMode)
            const isEmptyMask = value === floatingPlaceholder

            if (!storedValue && isEmptyMask) { return }

            if (value === '' || isEmptyMask) {
                onInputChange?.(null, name)

                return
            }

            const parsedValue = dayjs(value, fullFormat)
            const isValidValue = parsedValue.format(fullFormat) === value

            if (isValidValue) {
                if (min && parsedValue.isBefore(min)) {
                    onInputChange?.(dayjs(min), name)

                    return
                }

                if (max && parsedValue.isAfter(max)) {
                    onInputChange?.(dayjs(max), name)

                    return
                }

                onInputChange?.(parsedValue, name)
            }
        }

        const handleClick = (e: MouseEvent<HTMLInputElement>) => {
            setVisibility(true)
            if (onClick) { onClick(e) }
        }

        const handleFocus = (e: FocusEvent<HTMLInputElement>) => {
            onFocus(e, name)
            if (openOnFocus) { setVisibility(true) }
        }

        const handleBlur = (e: FocusEvent<HTMLInputElement>) => {
            const { value } = e.target as HTMLInputElement

            if (value === '') {
                onBlur?.(null, name)

                return
            }

            if (dayjs(value).format(outputFormat) === value) {
                onBlur?.(dayjs(value), name)
            }
        }

        return (
            <Component
                onInput={handleChange}
                onClick={handleClick}
                onFocus={handleFocus}
                onBlur={handleBlur}
                min={min}
                max={max}
                name={name}
                {...rest as P}
                dateMode={dateMode}
                timeMode={timeMode}
                value={storedValue}
            />
        )
    }

    return Wrapper
}
