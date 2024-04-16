import React from 'react'
import classNames from 'classnames'

import { TBaseProps } from '../types'
import { parseFormatter } from '../utils/parseFormatter'

type Props = TBaseProps & {
    format: string,
    // формат времени
    preLine?: boolean,
    text: string // флаг переноса текста
}

export function Text({ text, format, preLine, className, ...rest }: Props) {
    // @ts-ignore import from js file
    const parsedText = parseFormatter(text, format)

    return (
        <span
            className={classNames('n2o-snippet', className, {
                'white-space-pre-line': preLine,
            })}
            {...rest}
        >
            {parsedText}
        </span>
    )
}

Text.defaultProps = {
    preLine: false,
} as Props
