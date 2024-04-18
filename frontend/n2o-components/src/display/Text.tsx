import React from 'react'
import classNames from 'classnames'

import { TBaseProps } from '../types'
// @ts-ignore import from js file
import { parseFormatter } from '../utils/parseFormatter'

type Props = TBaseProps & {
    format: string,
    // формат времени
    preLine?: boolean,
    text: string // флаг переноса текста
}

export function Text({ text, format, preLine, className, ...rest }: Props) {
    return (
        <span
            className={classNames('n2o-snippet', className, {
                'white-space-pre-line': preLine,
            })}
            {...rest}
        >
            {parseFormatter(text, format)}
        </span>
    )
}

Text.defaultProps = {
    preLine: false,
} as Props
