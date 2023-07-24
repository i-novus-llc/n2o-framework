import React from 'react'
import classNames from 'classnames'

import { TBaseProps } from '../../types'

import { Base } from './Base'

type Props = TBaseProps & {
    format?: string,
    preLine?: boolean,
    text?: string
}

export function Text({
    preLine = false,
    className,
    text,
    ...rest
}: Props) {
    const tag = 'span'

    return (
        <Base
            tag={tag}
            className={classNames(className, {
                'white-space-pre-line': preLine,
            })}
            text={text}
            {...rest}
        />
    )
}
