import React from 'react'
import omit from 'lodash/omit'
import classNames from 'classnames'

import { withRightPlaceholder } from '../helpers/withRightPlaceholder'
import { TBaseInputProps, TBaseProps } from '../types'

import '../styles/controls/TextArea.scss'

type TextAreaProps = TBaseProps & Omit<TBaseInputProps<string>, 'onBlur' | 'onFocus'> & {
    maxRows?: number,
    rows?: number
}

const TextAreaComponent = ({
    className,
    style,
    disabled,
    placeholder,
    rows = 3,
    maxRows = 3,
    value = '',
    onChange,
    ...rest
}: TextAreaProps) => {
    const max = rows > maxRows ? rows : maxRows

    return (
        <textarea
            className={classNames('n2o-text-area form-control', className)}
            style={style}
            disabled={disabled}
            placeholder={placeholder}
            data-min-rows={rows}
            data-max-rows={max}
            value={value === null ? '' : value}
            onChange={event => onChange?.(event.target.value)}
            {...omit(rest, 'id')}
        />
    )
}

export const TextArea = withRightPlaceholder(TextAreaComponent)
