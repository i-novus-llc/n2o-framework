import React, { useMemo } from 'react'

import { parseFormatter } from '../utils/parseFormatter'

import { Text, TextProps } from './Text'

export type FormattedTextProps = {
    children: string | number
    format?: string
} & Omit<TextProps, 'children'>

export function FormattedText({
    children = '',
    format,
    ...attributes
}: FormattedTextProps) {
    const text: string | null = useMemo(() => {
        if (!children && children !== 0) { return null }
        if (!format) { return children.toString() }

        return parseFormatter(children.toString(), format)
    }, [children, format])

    return (
        <Text {...attributes}>
            {text}
        </Text>
    )
}

FormattedText.displayName = '@n2o-components/Typography/FormattedText'
