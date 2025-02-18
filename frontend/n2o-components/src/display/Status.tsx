import React from 'react'
import classNames from 'classnames'

import { TBaseProps } from '../types'
import { Text } from '../Typography/Text'

type Props = TBaseProps & {
    icon: string,
    iconDirection: string,
    text: string
}

export function Status({
    text,
    icon,
    className,
    iconDirection = 'left',
}: Props) {
    if (!icon && !text) {
        return null
    }

    return (
        <div
            className={classNames('n2o-status', className, {
                'status-icon-left': iconDirection === 'left',
                'status-icon-right': iconDirection === 'right',
            })}
        >
            {icon && (
                <i
                    className={classNames('n2o-status__icon', icon, {
                        'with-text': text,
                    })}
                />
            )}
            {text && (
                <div className={classNames('n2o-status__text', { 'with-icon': icon })}>
                    <Text>{text}</Text>
                </div>
            )}
        </div>
    )
}
