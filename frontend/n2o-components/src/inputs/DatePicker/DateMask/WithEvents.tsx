import React, { ChangeEvent, ComponentType, FC, FocusEvent, MouseEvent } from 'react'
import dayjs from 'dayjs'

import { type WithEventsProps, DateMaskProps } from './types'

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
        ...rest
    }) => {
        const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
            const { value } = e.target

            if (value === '') {
                onInputChange?.(null, name)

                return
            }

            const parsedValue = dayjs(value, fullFormat)
            const isValidValue = parsedValue.format(fullFormat) === value

            if (isValidValue) {
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
            />
        )
    }

    return Wrapper
}
