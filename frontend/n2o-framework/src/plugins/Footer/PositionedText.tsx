import classNames from 'classnames'
import React from 'react'

import { withNewlines } from './helpers'

interface PositionedTextProps {
    text?: string | null
    position: 'left' | 'right'
}

export function PositionedText({ text, position }: PositionedTextProps) {
    if (!text) {
        return null
    }

    return (
        <div className={classNames('w-100 ml-1 n2o-footer__text', `text-${position}`)}>
            {withNewlines(text)}
        </div>
    )
}
