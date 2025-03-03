import React, { memo } from 'react'
import classNames from 'classnames'

import { TBaseProps } from '../types'
import { FormattedText } from '../Typography/FormattedText'

import { Icon } from './Icon'

import '../styles/components/OutputText.scss'

type OutputTextProps = TBaseProps & {
    format?: string
    icon?: string
    iconPosition?: string
    value: string
}

export const OutputText = memo(({
    iconPosition = 'left',
    className = 'n2o',
    style,
    icon = '',
    value,
    format,
}: OutputTextProps) => {
    return (
        <div
            className={classNames('n2o-output-text', className, iconPosition)}
            style={style}
        >
            {icon && <Icon className="icon" name={icon} />}
            {value && (
                <div className="text">
                    <FormattedText format={format}>{value}</FormattedText>
                </div>
            )}
        </div>
    )
})
