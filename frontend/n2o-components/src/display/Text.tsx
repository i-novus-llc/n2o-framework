import React from 'react'
import classNames from 'classnames'

import { TBaseProps } from '../types'
import { FormattedText } from '../Typography/FormattedText'

type Props = TBaseProps & {
    format: string,
    text: string // флаг переноса текста
}

export function Text({ text, format, className, ...rest }: Props) {
    return (
        <span className={classNames('n2o-snippet', className)}>
            <FormattedText
                format={format}
                {...rest}
            >
                {text}
            </FormattedText>
        </span>
    )
}
