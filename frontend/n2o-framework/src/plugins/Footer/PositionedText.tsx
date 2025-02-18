import classNames from 'classnames'
import React from 'react'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'

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
            <Text>{text}</Text>
        </div>
    )
}
