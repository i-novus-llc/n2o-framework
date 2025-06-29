import React, { ComponentType, FC } from 'react'

import { buildDateFormat } from '../utils'

import { type WithFormatProps, type DateMaskProps } from './types'

export function getDateModeInfo(format: string): { separator: string; mode: string } {
    const separatorMatch = format.match(/[^A-Za-z]/)
    const separator = separatorMatch ? separatorMatch[0] : ''

    const parts = format.split(separator).filter(Boolean)

    const convertedParts = parts.map((part) => {
        const lowerPart = part.toLowerCase()

        if (lowerPart.includes('d')) { return 'dd' }
        if (lowerPart.includes('m')) { return 'mm' }
        if (lowerPart.includes('y')) {
            return part.length <= 2 ? 'yy' : 'yyyy'
        }

        return part
    })

    const mode = convertedParts.join('/')

    return { separator, mode }
}

export const WithFormat = <P extends DateMaskProps>(Component: ComponentType<P>) => {
    const Wrapper: FC<P & WithFormatProps> = ({
        dateFormat,
        timeFormat,
        min,
        max,
        value,
        ...rest
    }) => {
        const { separator: dateSeparator, mode: dateMode } = getDateModeInfo(dateFormat)
        const timeMode = timeFormat ? timeFormat.toUpperCase() : null
        const fullFormat = buildDateFormat(dateFormat, timeFormat, ' ')

        return (
            <Component
                {...rest as P}
                value={value?.format(fullFormat)}
                fullFormat={fullFormat}
                dateMode={dateMode}
                timeMode={timeMode}
                dateSeparator={dateSeparator}
                min={min ? new Date(min) : undefined}
                max={max ? new Date(max) : undefined}
            />
        )
    }

    return Wrapper
}
