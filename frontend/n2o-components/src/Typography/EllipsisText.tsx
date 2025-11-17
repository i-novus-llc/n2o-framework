import React, { memo, ComponentProps } from 'react'
import classNames from 'classnames'

import { encodeAttribute } from './utils'

export type FormattedTextProps = {
    children: string | number
} & Omit<ComponentProps<'span'>, 'children'>

export const EllipsisText = memo(({
    children = '',
    className,
    ...attributes
}: FormattedTextProps) => {
    const text = children?.toString()

    if (!text) { return null }

    let title
    const isMultiline = text.includes('\n')

    if (isMultiline) { title = encodeAttribute(text) }

    return (
        <span title={title} className={classNames('d-inline-block text-truncate w-100', className)} {...attributes}>
            {
                isMultiline
                    // фиктивное троеточие для многострочного текста, если первая строка короткая
                    ? `${text.split('\n')[0]}...`
                    : text
            }
        </span>
    )
})

EllipsisText.displayName = '@n2o-components/Typography/EllipsisText'
